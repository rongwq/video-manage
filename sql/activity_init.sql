-- ----------------------------
-- 活动管理表初始化脚本
-- 包含：表结构 + 菜单初始化
-- 注意：与充值活动(RechargeActivity)区分，本脚本创建的是运营推广活动(PromotionActivity)
-- ----------------------------

-- ----------------------------
-- 1、运营活动管理表
-- ----------------------------
drop table if exists v_activity;
create table v_activity (
  id                bigint(20)      not null auto_increment    comment '活动ID',
  title             varchar(100)    not null                   comment '活动标题',
  image_url         varchar(500)    default ''                 comment '活动图片URL',
  link_url          varchar(500)    default ''                 comment '点击跳转链接',
  status            char(1)         default '0'                comment '活动状态（0启用 1停用）',
  expire_time       datetime                                   comment '过期时间',
  sort_order        int(11)         default 0                  comment '排序号',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (id)
) engine=innodb auto_increment=1 comment = '运营活动管理表';

-- ----------------------------
-- 2、菜单权限表数据（运营活动管理）
-- ----------------------------

-- 先删除已存在的运营活动管理菜单（如果存在）
delete from sys_menu where perms like 'video:promotion:%';
delete from sys_menu where menu_name = '运营活动' and parent_id = '2000';

-- 插入运营活动管理菜单（作为video菜单的子菜单）
-- 注意：请将parent_id修改为实际的video菜单ID，这里假设video菜单ID为2000
insert into sys_menu values(
    '2200', 
    '运营活动', 
    '2000',  -- 父菜单ID，请根据实际情况修改为video菜单的实际ID
    '2', 
    'promotion', 
    'video/promotion/index', 
    '', 
    '', 
    1, 
    0, 
    'C', 
    '0', 
    '0', 
    'video:promotion:list', 
    'star', 
    'admin', 
    sysdate(), 
    '', 
    null, 
    '运营活动管理菜单'
);

-- 运营活动管理按钮权限
insert into sys_menu values('2201', '活动查询', '2200', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'video:promotion:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2202', '活动新增', '2200', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'video:promotion:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2203', '活动修改', '2200', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'video:promotion:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2204', '活动删除', '2200', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'video:promotion:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2205', '活动导出', '2200', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'video:promotion:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 3、角色菜单关联（给超级管理员分配运营活动管理权限）
-- ----------------------------
-- 给角色ID为1的超级管理员添加运营活动管理菜单权限
insert into sys_role_menu values('1', '2200');
insert into sys_role_menu values('1', '2201');
insert into sys_role_menu values('1', '2202');
insert into sys_role_menu values('1', '2203');
insert into sys_role_menu values('1', '2204');
insert into sys_role_menu values('1', '2205');

-- ----------------------------
-- 4、插入示例数据
-- ----------------------------
insert into v_activity values(
    1, 
    '新年大促活动', 
    'https://example.com/images/newyear.jpg', 
    'https://example.com/promotion/newyear', 
    '0', 
    date_add(sysdate(), interval 30 day), 
    1, 
    'admin', 
    sysdate(), 
    '', 
    null, 
    '新年促销活动，限时优惠'
);

insert into v_activity values(
    2, 
    '会员充值优惠', 
    'https://example.com/images/recharge.jpg', 
    'https://example.com/promotion/recharge', 
    '0', 
    date_add(sysdate(), interval 60 day), 
    2, 
    'admin', 
    sysdate(), 
    '', 
    null, 
    '会员充值享额外赠送'
);

insert into v_activity values(
    3, 
    '限时折扣活动', 
    'https://example.com/images/discount.jpg', 
    'https://example.com/promotion/discount', 
    '1', 
    date_add(sysdate(), interval 7 day), 
    3, 
    'admin', 
    sysdate(), 
    '', 
    null, 
    '限时折扣，先到先得'
);
