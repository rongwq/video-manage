-- ----------------------------
-- 注册活动配置表
-- ----------------------------
drop table if exists v_reg_activity_config;
create table v_reg_activity_config
(
    id              bigint(20)      not null auto_increment    comment 'ID',
    activity_name   varchar(100)    not null                   comment '活动名称',
    activity_status char(1)         not null default '0'       comment '活动开关（0关闭 1开启）',
    give_integral   bigint(20)      not null default 0         comment '赠送积分',
    start_time      datetime                                   comment '活动开始时间',
    end_time        datetime                                   comment '活动结束时间',
    remark          varchar(500)    default null               comment '备注',
    create_by       varchar(64)     default ''                 comment '创建者',
    create_time     datetime                                   comment '创建时间',
    update_by       varchar(64)     default ''                 comment '更新者',
    update_time     datetime                                   comment '更新时间',
    primary key (id)
) engine=innodb auto_increment=1 comment='注册活动配置表';

-- ----------------------------
-- 插入默认活动配置（默认关闭）
-- ----------------------------
insert into v_reg_activity_config (activity_name, activity_status, give_integral, start_time, end_time, remark, create_time)
values ('新用户注册赠送100积分活动', '0', 100, now(), date_add(now(), interval 30 day), '新用户注册免费赠送100积分', now());

-- ----------------------------
-- 菜单SQL（若依框架菜单权限）
-- ----------------------------
-- 查看父菜单ID
select @parentId := menu_id from sys_menu where menu_name = '视频管理';

-- 插入菜单
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, is_refresh, perms, icon, create_by, create_time, update_by, update_time, remark)
values ('注册活动配置', @parentId, '10', '/video/regActivityConfig', 'C', '0', '1', 'video:regActivityConfig:view', '#', 'admin', now(), '', now(), '注册活动配置菜单');

-- 查看按钮父菜单ID
select @parentId := last_insert_id();

-- 按钮父菜单ID
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, is_refresh, perms, icon, create_by, create_time, update_by, update_time, remark)
values ('注册活动配置查询', @parentId, '1', '#', 'F', '0', '1', 'video:regActivityConfig:list', '#', 'admin', now(), '', now(), '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, is_refresh, perms, icon, create_by, create_time, update_by, update_time, remark)
values ('注册活动配置新增', @parentId, '2', '#', 'F', '0', '1', 'video:regActivityConfig:add', '#', 'admin', now(), '', now(), '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, is_refresh, perms, icon, create_by, create_time, update_by, update_time, remark)
values ('注册活动配置修改', @parentId, '3', '#', 'F', '0', '1', 'video:regActivityConfig:edit', '#', 'admin', now(), '', now(), '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, is_refresh, perms, icon, create_by, create_time, update_by, update_time, remark)
values ('注册活动配置删除', @parentId, '4', '#', 'F', '0', '1', 'video:regActivityConfig:remove', '#', 'admin', now(), '', now(), '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, is_refresh, perms, icon, create_by, create_time, update_by, update_time, remark)
values ('注册活动配置导出', @parentId, '5', '#', 'F', '0', '1', 'video:regActivityConfig:export', '#', 'admin', now(), '', now(), '');
