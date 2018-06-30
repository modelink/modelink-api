DROP TABLE IF EXISTS `merchant`;

CREATE TABLE `merchant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_key` bigint(20) NOT NULL COMMENT 'appKey',
  `name` varchar(32) NOT NULL COMMENT '合作商户名称',
  `app_secret` varchar(32) NOT NULL COMMENT 'appSecret',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0-；1-；2-；',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_key_UNIQUE` (`app_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='合作商户';

INSERT INTO `merchant` VALUES (1,10001,'xiaomi','59688d3f2c875df5921039cf7e1f10a2',1,'2018-05-26 01:37:22','2018-05-26 01:40:30');
