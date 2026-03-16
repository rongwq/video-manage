-- APP用户表
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `user_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` VARCHAR(50) NOT NULL COMMENT '用户账号',
  `nick_name` VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '用户邮箱',
  `phonenumber` VARCHAR(11) DEFAULT NULL COMMENT '手机号码',
  `sex` CHAR(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` VARCHAR(200) DEFAULT NULL COMMENT '头像地址',
  `password` VARCHAR(100) DEFAULT NULL COMMENT '密码',
  `status` CHAR(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` VARCHAR(128) DEFAULT NULL COMMENT '最后登录IP',
  `login_date` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `device_id` VARCHAR(100) DEFAULT NULL COMMENT '设备ID',
  `vip_level` INT(11) DEFAULT '0' COMMENT 'VIP等级',
  `vip_expire_time` DATETIME DEFAULT NULL COMMENT 'VIP过期时间',
  `integral` BIGINT(20) DEFAULT '0' COMMENT '积分',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_user_name` (`user_name`),
  KEY `idx_phonenumber` (`phonenumber`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='APP用户表';
