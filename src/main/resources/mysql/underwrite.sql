DROP TABLE IF EXISTS `underwrite`;
CREATE TABLE `underwrite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `merchant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '预约商户（如小米、华夏）',
  `platform_name` varchar(20) NOT NULL DEFAULT '' COMMENT '渠道归属（PC、移动端）',
  `advertise_active` varchar(20) NOT NULL DEFAULT '' COMMENT '广告活动（百度SEM、自然流量）',
  `advertise_series` varchar(20) NOT NULL DEFAULT '' COMMENT '广告系列',
  `keyword` varchar(128) NOT NULL DEFAULT '' COMMENT '广告描述（关键词）',
  `source` varchar(20) NOT NULL DEFAULT '' COMMENT '数据来源（官网、官微、转介绍）',

  `org_name` varchar(32) NOT NULL DEFAULT '' COMMENT '机构名称',
  `product_name` varchar(64) NOT NULL DEFAULT '' COMMENT '产品名称',

  `reserve_mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '投保人电话',
  `gender` varchar(6) NOT NULL DEFAULT '' COMMENT '投保人性别',
  `age` int(11) NOT NULL DEFAULT '0' COMMENT '年龄',
  `birthday` varchar(10) NOT NULL DEFAULT '' COMMENT '投保人生日',
  `address` varchar(256) NOT NULL DEFAULT '' COMMENT '投保人地址',
  `province_id` int(11) NOT NULL DEFAULT '0' COMMENT '省份ID',
  `city_id` int(11) NOT NULL DEFAULT '0' COMMENT '城市ID',

  `reserve_date` varchar(10) NOT NULL DEFAULT '' COMMENT '预约时间',
  `source_date` varchar(10) NOT NULL DEFAULT '' COMMENT '来源日期',
  `finish_date` varchar(10) NOT NULL DEFAULT '' COMMENT '成单日期',

  `pay_type` int(11) NOT NULL DEFAULT '0' COMMENT '缴费方式（年交、月交）',
  `insurance_no` varchar(16) NOT NULL DEFAULT '' COMMENT '保单编号',
  `insurance_amount` varchar(32) NOT NULL DEFAULT '' COMMENT '保险额度',
  `insurance_fee` varchar(32) NOT NULL DEFAULT '' COMMENT '保费',
  `remark` varchar(64) NOT NULL DEFAULT '' COMMENT '备注',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='承保数据明细表';