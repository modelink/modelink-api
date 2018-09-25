DROP TABLE IF EXISTS `admin`;
SET character_set_client = utf8mb4 ;
CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `nick_name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '用户密码',
  `role_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '用户密码',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '用户状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='后台管理员表';

LOCK TABLES `admin` WRITE;
INSERT INTO `admin` VALUES (1,'admin','','管理员','shuchi!Q@Wm666','1',1,'2018-09-25 12:00:00','2018-09-25 12:00:00');
INSERT INTO `admin` VALUES (2,'lijia','','李佳','shuchi123','1',1,'2018-09-25 12:00:00','2018-09-25 12:00:00');
INSERT INTO `admin` VALUES (3,'huangcl','','黄春丽','shuchi123','1',1,'2018-09-25 12:00:00','2018-09-25 12:00:00');
INSERT INTO `admin` VALUES (4,'zhangna','','张娜','shuchi123','2,3',1,'2018-09-25 12:00:00','2018-09-25 12:00:00');
INSERT INTO `admin` VALUES (5,'chenwz','','陈婉珍','shuchi123','2',1,'2018-09-25 12:00:00','2018-09-25 12:00:00');
INSERT INTO `admin` VALUES (6,'wangrui','','王蕊','shuchi123','2',1,'2018-09-25 12:00:00','2018-09-25 12:00:00');
INSERT INTO `admin` VALUES (7,'pantm','','潘泰名','shuchi123','4',1,'2018-09-25 12:00:00','2018-09-25 12:00:00');
