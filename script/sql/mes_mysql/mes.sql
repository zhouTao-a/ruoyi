use ruoyi;
CREATE TABLE `mes_ip_white_list` (
                                     `id` bigint NOT NULL COMMENT '主键ID',
                                     `ip_address` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP地址或CIDR网段',
                                     `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注说明',
                                     `status` tinyint DEFAULT '1' COMMENT '状态：1-有效，0-无效',
                                     `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编号',
                                     `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
                                     `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
                                     `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                     `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='IP白名单';

CREATE TABLE mes_msg_user (
                              id BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
                              user_name VARCHAR(20) NOT NULL COMMENT '用户名',
                              gender ENUM('M', 'F') NOT NULL COMMENT '性别',
                              user_code VARCHAR(20) NOT NULL UNIQUE COMMENT '用户编码',
                              id_card VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
                              phone_number VARCHAR(11) DEFAULT NULL COMMENT '手机号',
                              birthday DATE DEFAULT NULL COMMENT '出生日期',
                              lunar_birthday DATE DEFAULT NULL COMMENT '农历出生日期',
                              sms_notify_flag ENUM('T', 'F') DEFAULT 'F' NOT NULL COMMENT '是否接收短信通知',
                              email VARCHAR(100) DEFAULT NULL COMMENT '邮箱地址',
                              email_notify_flag ENUM('T', 'F') DEFAULT 'F' NOT NULL COMMENT '是否接收邮箱通知',
                              deleted_flag ENUM('T','F') DEFAULT 'F' NOT NULL COMMENT '逻辑删除标志',
                              tenant_id VARCHAR(20) DEFAULT '000000' COMMENT '租户编号',
                              create_dept BIGINT DEFAULT NULL COMMENT '创建部门',
                              create_by BIGINT DEFAULT NULL COMMENT '创建者',
                              create_time DATETIME DEFAULT NULL COMMENT '创建时间',
                              update_by BIGINT DEFAULT NULL COMMENT '更新者',
                              update_time DATETIME DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息表';



CREATE TABLE mes_msg_group (
                               id BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
                               group_name VARCHAR(20) NOT NULL COMMENT '分组名称',
                               group_code VARCHAR(20) NOT NULL UNIQUE COMMENT '分组编码',
                               default_target_user_id BIGINT DEFAULT NULL COMMENT '默认参考用户ID',
                               tenant_id VARCHAR(20) DEFAULT '000000' COMMENT '租户编号',
                               create_dept BIGINT DEFAULT NULL COMMENT '创建部门',
                               create_by BIGINT DEFAULT NULL COMMENT '创建者',
                               create_time DATETIME DEFAULT NULL COMMENT '创建时间',
                               update_by BIGINT DEFAULT NULL COMMENT '更新者',
                               update_time DATETIME DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='分组信息表';

CREATE TABLE mes_msg_user_group (
                                    id BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
                                    user_id BIGINT NOT NULL COMMENT '用户ID',
                                    group_id BIGINT NOT NULL COMMENT '分组ID',
                                    relative_generation_diff INT DEFAULT 0 COMMENT '与参考用户的代际差',
                                    kinship_level ENUM('close', 'distant') NOT NULL DEFAULT 'close' COMMENT '亲缘关系（close=亲属，distant=远亲, friend=朋友, stranger=陌生人）',
                                    tenant_id VARCHAR(20) DEFAULT '000000' COMMENT '租户编号',
                                    create_dept BIGINT DEFAULT NULL COMMENT '创建部门',
                                    create_by BIGINT DEFAULT NULL COMMENT '创建者',
                                    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
                                    update_by BIGINT DEFAULT NULL COMMENT '更新者',
                                    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
                                    UNIQUE (user_id, group_id),
                                    FOREIGN KEY (user_id) REFERENCES mes_msg_user (id) ON DELETE CASCADE,
                                    FOREIGN KEY (group_id) REFERENCES mes_msg_group (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户与分组关联表';
