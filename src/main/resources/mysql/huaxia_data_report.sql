DROP TABLE IF EXISTS `huaxia_data_report`;
CREATE TABLE `huaxia_data_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `date` varchar(10) NOT NULL DEFAULT '' COMMENT '日期',
  `data_source` varchar(10) NOT NULL DEFAULT '' COMMENT '数据来源',
  `pc_count` int(11) NOT NULL DEFAULT 0 COMMENT '总转化-PC',
  `wap_count` int(11) NOT NULL DEFAULT 0 COMMENT '总转化-WAP',
  `weixin_count` int(11) NOT NULL DEFAULT 0 COMMENT '微信端',
  `xiaomi_count` int(11) NOT NULL DEFAULT 0 COMMENT '小米',
  `valid_count` int(11) NOT NULL DEFAULT 0 COMMENT '有效',
  `flag_count` int(11) NOT NULL DEFAULT 0 COMMENT '营销标记电话',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='华夏日报-基础数量数据表';