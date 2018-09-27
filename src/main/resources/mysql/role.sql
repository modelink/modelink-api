DROP TABLE IF EXISTS `role`;
SET character_set_client = utf8mb4 ;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '角色名',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '角色描述',
  `permission_ids` varchar(1024) DEFAULT NULL,
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '该角色是否可用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='后台管理员角色表';

INSERT INTO `role` VALUES (1,'管理员','管理员角色','10000001,10000002,10000003,10000004,10000005,10000006,10000007,10000008,10000009,10000010,10000011,10000012,10000013,10000014,10000015,10000016,10000017,10000018,10000019,10000020,10000021,10000022,10000023,10000024,10000025,10000026,10000027,10000028,10000029,10000030,10000031,10000032,10000033,10000034,10000035,10000036,10000037,10000038,10000039,10000040,10000041,10000042,10000043,10000044',1,'2018-06-03 05:32:44','2018-09-25 06:27:50');
INSERT INTO `role` VALUES (2,'营销分析','营销分析角色','10000010,10000011,10000012,10000013,10000014,10000015,10000016,10000017,10000018,10000019,10000020,10000021,10000022,10000023,10000024,10000025,10000026',1,'2018-07-12 06:35:55','2018-09-25 08:05:06');
INSERT INTO `role` VALUES (3,'流量分析','流量分析角色','10000001,10000002,10000003,10000004,10000005,10000006,10000007,10000008,10000009',1,'2018-09-25 08:01:00','2018-09-25 08:05:06');
INSERT INTO `role` VALUES (4,'基础数据','基础数据角色','10000028,10000029,10000030,10000031,10000032,10000033,10000034,10000035,10000036,10000037,10000038,10000039,10000040,10000041,10000042,10000043,10000044',1,'2018-09-25 08:01:00','2018-09-25 08:05:06');
