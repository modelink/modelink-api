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

INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000001,'流量分析',1,'/admin/auth',0,'一级菜单：流量分析',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000002,'综合信息',1,'/admin/dashboard/flow',10000001,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000003,'外部来源',1,'/admin/auth',10000002,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000004,'地域统计',1,'/admin/auth',10000002,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000005,'时段统计',1,'/admin/auth',10000002,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000006,'客户端信息',1,'/admin/auth',10000002,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000007,'网页项目分析',1,'/admin/auth',10000001,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000008,'页面流向分析',1,'/admin/auth',10000001,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000009,'专题页面分析',1,'/admin/auth',10000001,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000010,'营销分析',1,'/admin/auth',0,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000011,'营销驾驶仓',1,'/admin/dashboard',10000010,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000012,'营销模式分析',1,'/admin/auth',10000010,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000013,'预约购买模式',1,'/admin/auth',10000012,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000014,'综合信息',1,'/admin/dashboard/summary',10000013,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000015,'关键词分析',1,'/admin/dashboard/keyword',10000013,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000016,'客户分布分析',1,'/admin/dashboard/customer',10000013,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000017,'地域分析',1,'/admin/dashboard/area',10000013,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000018,'时间段分析',1,'/admin/dashboard/time',10000013,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000019,'客户端分析',1,'/admin/dashboard/client',10000013,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000020,'测算模式',1,'/admin/auth',10000012,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000021,'测算分析',1,'/admin/dashboard/estimate',10000020,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000022,'在线购买模式',1,'/admin/auth',10000012,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000023,'品牌营销分析',1,'/admin/auth',10000012,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000024,'营销解决方案',1,'/admin/auth',10000010,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000025,'媒体渠道综合分析',1,'/admin/dashboard/media',10000024,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000026,'财务分析',1,'/admin/auth',10000024,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000027,'客户关系管理分析',1,'/admin/auth',0,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000028,'用户权限管理',1,'/admin/auth',0,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000029,'消息功能',1,'/admin/auth',0,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000030,'公共数据表',1,'/admin/auth',0,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000031,'基础数据表',1,'/admin/auth',0,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000032,'错误日志明细',1,'/admin/exceptionLogger',10000029,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000033,'区域数据明细',1,'/admin/area',10000030,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000034,'预约数据明细',1,'/admin/reservation',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000035,'媒体策略调整',1,'/admin/mediaTactics',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000036,'华夏保费数据',1,'/admin/permiums',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000037,'承保数据明细',1,'/admin/underwrite',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000038,'退保数据明细',1,'/admin/repellent',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000039,'媒体数据明细',1,'/admin/mediaItem',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000040,'测保数据明细',1,'/admin/estimate',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000041,'流量总表数据',1,'/admin/flow',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000042,'地区流量数据',1,'/admin/flowArea',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000043,'预约流量数据',1,'/admin/flowReserve',10000031,'',1);
INSERT INTO `permission` (`id`,`name`,`type`,`value`,`parent_id`,`description`,`available`) VALUES (10000044,'异常数据明细',1,'/admin/abnormal',10000031,'',1);


