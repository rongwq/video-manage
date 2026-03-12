-- ----------------------------
-- 活动管理表
-- ----------------------------
DROP TABLE IF EXISTS v_promotion_activity;
CREATE TABLE v_promotion_activity (
  id              BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '活动ID',
  title           VARCHAR(100)    DEFAULT ''                 COMMENT '活动标题',
  image_url       VARCHAR(500)    DEFAULT ''                 COMMENT '活动图片URL',
  link_url        VARCHAR(500)    DEFAULT ''                 COMMENT '点击跳转链接',
  status          CHAR(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  order_num       INT(4)          DEFAULT 0                  COMMENT '排序',
  expire_time     DATETIME        DEFAULT NULL               COMMENT '过期时间',
  create_by       VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time     DATETIME                                   COMMENT '创建时间',
  update_by       VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time     DATETIME                                   COMMENT '更新时间',
  remark          VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT = '活动管理表';

-- ----------------------------
-- 初始化-活动管理菜单数据
-- ----------------------------
-- 一级菜单：运营管理
INSERT INTO sys_menu VALUES('2000', '运营管理', '0', '5', 'operation', NULL, '', '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', SYSDATE(), '', NULL, '运营管理目录');

-- 二级菜单：活动管理
INSERT INTO sys_menu VALUES('2001', '活动管理', '2000', '1', 'promotion', 'system/promotion/index', '', '', 1, 0, 'C', '0', '0', 'system:promotion:list', 'star', 'admin', SYSDATE(), '', NULL, '活动管理菜单');

-- 活动管理按钮
INSERT INTO sys_menu VALUES('2002', '活动查询', '2001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'system:promotion:query', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('2003', '活动新增', '2001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'system:promotion:add', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('2004', '活动修改', '2001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'system:promotion:edit', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('2005', '活动删除', '2001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'system:promotion:remove', '#', 'admin', SYSDATE(), '', NULL, '');
INSERT INTO sys_menu VALUES('2006', '活动导出', '2001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'system:promotion:export', '#', 'admin', SYSDATE(), '', NULL, '');

-- ----------------------------
-- 为普通角色分配活动管理菜单权限
-- ----------------------------
INSERT INTO sys_role_menu VALUES ('2', '2000');
INSERT INTO sys_role_menu VALUES ('2', '2001');
INSERT INTO sys_role_menu VALUES ('2', '2002');
INSERT INTO sys_role_menu VALUES ('2', '2003');
INSERT INTO sys_role_menu VALUES ('2', '2004');
INSERT INTO sys_role_menu VALUES ('2', '2005');
INSERT INTO sys_role_menu VALUES ('2', '2006');
