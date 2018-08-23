DROP TABLE IF EXISTS `permiums`;
CREATE TABLE `permiums` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `date` varchar(10) NOT NULL DEFAULT '' COMMENT '日期',
  `merchant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '合作商户',

  `valid_count` int(11) NOT NULL DEFAULT 0 COMMENT '有效数据（下发）',
  `transform_count` int(11) NOT NULL DEFAULT 0 COMMENT '总转化',
  `transform_count_nowx` int(11) NOT NULL DEFAULT 0 COMMENT '总转化（不包含微信）',
  `consume_amount` varchar(32) NOT NULL DEFAULT '' COMMENT '总花费(元)',
  `direct_transform_cost` varchar(32) NOT NULL DEFAULT '' COMMENT '直接转化成本',
  `total_transform_cost` varchar(32) NOT NULL DEFAULT '' COMMENT '总转化成本（不含微信）',
  `insurance_count` int(11) NOT NULL DEFAULT 0 COMMENT '保件',
  `insurance_fee` varchar(32) NOT NULL DEFAULT '' COMMENT '保费',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='保费数据表';