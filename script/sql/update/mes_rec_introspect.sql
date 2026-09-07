-- 自省主题与按天明细
create table if not exists mes_rec_introspect (
    id            bigint(20)    not null                   comment '主键',
    tenant_id     varchar(20)   default '000000'           comment '租户编号',
    user_id       bigint(20)    not null                   comment '用户',
    title         varchar(255)  not null                   comment '主题标题',
    status        varchar(32)   default 'inactive'         comment '状态 active生效 inactive失效',
    sort_order    bigint(20)    default 0                  comment '排序',
    deleted_flag  char(1)       default '0'                comment '逻辑删除',
    create_dept   bigint(20)    default null               comment '创建部门',
    create_by     bigint(20)    default null               comment '创建者',
    create_time   datetime                                 comment '创建时间',
    update_by     bigint(20)    default null               comment '更新者',
    update_time   datetime                                 comment '更新时间',
    primary key (id),
    key idx_rec_introspect_user (user_id, status)
) engine=innodb comment = '自省主题';

create table if not exists mes_rec_introspect_item (
    id             bigint(20)    not null                   comment '主键',
    tenant_id      varchar(20)   default '000000'           comment '租户编号',
    introspect_id  bigint(20)    not null                   comment '所属主题',
    user_id        bigint(20)    not null                   comment '用户',
    occur_date     date          not null                   comment '发生日期',
    content        varchar(1024) not null                   comment '情况说明',
    sort_order     bigint(20)    default 0                  comment '当天内排序',
    deleted_flag   char(1)       default '0'                comment '逻辑删除',
    create_dept    bigint(20)    default null               comment '创建部门',
    create_by      bigint(20)    default null               comment '创建者',
    create_time    datetime                                 comment '创建时间',
    update_by      bigint(20)    default null               comment '更新者',
    update_time    datetime                                 comment '更新时间',
    primary key (id),
    key idx_rec_introspect_item_day (user_id, occur_date),
    key idx_rec_introspect_item_theme (introspect_id, occur_date)
) engine=innodb comment = '自省按天明细';

-- 菜单挂到「任务」同级。若 menu_id 冲突请改号。
set @recParentId := (select parent_id from sys_menu where perms = 'rec:recTask:list' and menu_type = 'C' limit 1);
set @recOrder := ifnull((select max(order_num) from sys_menu where parent_id = @recParentId), 0) + 1;

insert into sys_menu values('19310', '自省', @recParentId, @recOrder, 'recIntrospect', 'rec/recIntrospect/index', '', 1, 0, 'C', '0', '0', 'rec:recIntrospect:list', 'guide', 103, 1, sysdate(), null, null, '自省主题菜单');
insert into sys_menu values('19311', '自省查询', '19310', 1, '#', '', '', 1, 0, 'F', '0', '0', 'rec:recIntrospect:query', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('19312', '自省新增', '19310', 2, '#', '', '', 1, 0, 'F', '0', '0', 'rec:recIntrospect:add', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('19313', '自省修改', '19310', 3, '#', '', '', 1, 0, 'F', '0', '0', 'rec:recIntrospect:edit', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('19314', '自省删除', '19310', 4, '#', '', '', 1, 0, 'F', '0', '0', 'rec:recIntrospect:remove', '#', 103, 1, sysdate(), null, null, '');
insert into sys_menu values('19315', '自省导出', '19310', 5, '#', '', '', 1, 0, 'F', '0', '0', 'rec:recIntrospect:export', '#', 103, 1, sysdate(), null, null, '');
