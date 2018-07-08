DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL COMMENT '权限ID',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '权限名',
  `type` int(11) NOT NULL COMMENT '权限累心：1-菜单；2-按钮',
  `value` varchar(45) NOT NULL DEFAULT '' COMMENT '权限值',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父级权限ID',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '权限描述',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '该权限是否可用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理员权限表';

INSERT INTO `permission` VALUES
    (1,'业务功能',1,'',0,'一级菜单：业务功能',1,'2018-06-03 05:33:58','2018-06-03 06:44:22'),
    (2,'公共数据',1,'',0,'一级菜单：公共数据',1,'2018-06-03 05:33:58','2018-06-03 06:44:22'),
    (10,'预约管理',1,'/admin/reservation',1,'预约管理菜单',1,'2018-06-03 05:33:58','2018-06-03 06:44:22'),
    (11,'效果数据',1,'/admin/insurance',1,'投保数据分析',1,'2018-06-30 13:19:38','2018-07-07 13:43:25'),
    (12,'广告数据',1,'/admin/advertise',1,'广告数据分析',1,'2018-06-30 13:19:38','2018-07-07 13:43:25'),
    (20,'区域数据管理',1,'/admin/insurance',2,'投保数据处理',1,'2018-06-30 13:19:38','2018-07-07 13:43:25');