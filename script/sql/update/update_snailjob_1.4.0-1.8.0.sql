-- SnailJob 1.4.0 -> 1.8.0 增量脚本（MySQL）
-- 执行前请备份 sj_* 表；停 ruoyi-snailjob-server 与业务应用后再执行。
-- 本脚本仅执行一次。若某条 ALTER 报“重复列/索引不存在”，说明该步已做过，跳过即可。

-- ===== 1.5.0：重试表补充 group_id / scene_id 并调整索引 =====
ALTER TABLE sj_retry ADD COLUMN group_id bigint NULL COMMENT '组Id' AFTER group_name;
ALTER TABLE sj_retry ADD COLUMN scene_id bigint NULL COMMENT '场景ID' AFTER scene_name;
ALTER TABLE sj_retry_dead_letter ADD COLUMN group_id bigint NULL COMMENT '组Id' AFTER group_name;
ALTER TABLE sj_retry_dead_letter ADD COLUMN scene_id bigint NULL COMMENT '场景ID' AFTER scene_name;

UPDATE sj_retry r
    INNER JOIN sj_group_config g ON r.namespace_id = g.namespace_id AND r.group_name = g.group_name
SET r.group_id = g.id
WHERE r.group_id IS NULL;

UPDATE sj_retry r
    INNER JOIN sj_retry_scene_config s
    ON r.namespace_id = s.namespace_id AND r.group_name = s.group_name AND r.scene_name = s.scene_name
SET r.scene_id = s.id
WHERE r.scene_id IS NULL;

UPDATE sj_retry_dead_letter r
    INNER JOIN sj_group_config g ON r.namespace_id = g.namespace_id AND r.group_name = g.group_name
SET r.group_id = g.id
WHERE r.group_id IS NULL;

UPDATE sj_retry_dead_letter r
    INNER JOIN sj_retry_scene_config s
    ON r.namespace_id = s.namespace_id AND r.group_name = s.group_name AND r.scene_name = s.scene_name
SET r.scene_id = s.id
WHERE r.scene_id IS NULL;

-- 若仍有 NULL，需先补齐组/场景配置后再执行下面 4 条
ALTER TABLE sj_retry MODIFY COLUMN group_id bigint NOT NULL COMMENT '组Id';
ALTER TABLE sj_retry MODIFY COLUMN scene_id bigint NOT NULL COMMENT '场景ID';
ALTER TABLE sj_retry_dead_letter MODIFY COLUMN group_id bigint NOT NULL COMMENT '组Id';
ALTER TABLE sj_retry_dead_letter MODIFY COLUMN scene_id bigint NOT NULL COMMENT '场景ID';

ALTER TABLE sj_retry DROP INDEX idx_namespace_id_group_name_retry_status;
ALTER TABLE sj_retry DROP INDEX idx_namespace_id_group_name_scene_name;
CREATE INDEX idx_retry_status_bucket_index ON sj_retry (retry_status, bucket_index);
ALTER TABLE sj_retry DROP INDEX uk_name_task_type_idempotent_id_deleted;
ALTER TABLE sj_retry ADD UNIQUE KEY uk_scene_tasktype_idempotentid_deleted (scene_id, task_type, idempotent_id, deleted);

-- ===== 1.6.0：标签、序列化器、执行器表，删除号段表 =====
ALTER TABLE sj_job ADD COLUMN labels varchar(512) DEFAULT '' NULL COMMENT '标签' AFTER owner_id;
ALTER TABLE sj_retry ADD COLUMN serializer_name varchar(32) NOT NULL DEFAULT 'jackson' COMMENT '执行方法参数序列化器名称';
ALTER TABLE sj_retry_dead_letter ADD COLUMN serializer_name varchar(32) NOT NULL DEFAULT 'jackson' COMMENT '执行方法参数序列化器名称';
ALTER TABLE sj_retry_scene_config
    ADD COLUMN owner_id bigint NULL COMMENT '负责人id',
    ADD COLUMN labels varchar(512) DEFAULT '' NULL COMMENT '标签';
ALTER TABLE sj_server_node ADD COLUMN labels varchar(512) DEFAULT '' NULL COMMENT '标签';
ALTER TABLE sj_workflow ADD COLUMN owner_id bigint NULL COMMENT '负责人id';

CREATE TABLE IF NOT EXISTS `sj_job_executor`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `namespace_id`  varchar(64)         NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a' COMMENT '命名空间id',
    `group_name`    varchar(64)         NOT NULL COMMENT '组名称',
    `executor_info` varchar(256)        NOT NULL COMMENT '任务执行器名称',
    `executor_type` varchar(3)          NOT NULL COMMENT '1:java 2:python 3:go',
    `create_dt`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_dt`     datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_namespace_id_group_name` (`namespace_id`, `group_name`),
    KEY `idx_create_dt` (`create_dt`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='任务执行器信息';

DROP TABLE IF EXISTS sj_sequence_alloc;

ALTER TABLE sj_retry_summary MODIFY COLUMN scene_name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '场景名称';
