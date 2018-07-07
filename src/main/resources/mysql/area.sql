DROP TABLE IF EXISTS `area`;

CREATE TABLE `area` (
  `area_id` int(11) NOT NULL,
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父级地区',
  `area_name` varchar(32) NOT NULL DEFAULT '' COMMENT '地区名称',
  `area_type` int(11) NOT NULL DEFAULT 1 COMMENT '地区类型',
  `remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注信息',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态：0-正常；1-禁用；',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`area_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='地区';
