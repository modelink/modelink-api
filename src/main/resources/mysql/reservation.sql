DROP TABLE IF EXISTS `reservation`;

CREATE TABLE `reservation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contact_name` varchar(32) NOT NULL COMMENT '预约姓名',
  `contact_time` varchar(10) NOT NULL COMMENT '预约时间',
  `contact_mobile` varchar(20) NOT NULL COMMENT '预约电话',
  `source_type` int(11) NOT NULL DEFAULT '0' COMMENT '渠道入口类型（0-默认）',
  `advertise_position` VARCHAR(32) NOT NULL COMMENT '广告位',
  `channel` bigint(20) NOT NULL DEFAULT '0' COMMENT '预约渠道（如小米、华夏）',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '数据状态（0-正常; 1-无效; 2-其他）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='预约登记表';

