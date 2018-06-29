DROP TABLE IF EXISTS `insurance`;
CREATE TABLE `insurance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `insurance_no` varchar(32) NOT NULL DEFAULT '' COMMENT '保单编号',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '投保人姓名',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '投保人电话',
  `gender` varchar(6) NOT NULL DEFAULT '' COMMENT '投保人性别',
  `age` int(11) NOT NULL DEFAULT '0' COMMENT '年龄',
  `birthday` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `address` varchar(32) NOT NULL DEFAULT '' COMMENT '投保人地址',
  `province` int(11) NOT NULL DEFAULT '0' COMMENT '省份ID',
  `city` int(11) NOT NULL DEFAULT '0' COMMENT '城市ID',

  `contact_time` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '预约时间',
  `arrange_ime` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '预约时间',
  `channel` bigint(20) NOT NULL DEFAULT '0' COMMENT '预约渠道（如小米、华夏）',
  `platform` int(11) NOT NULL DEFAULT '0' COMMENT '预约平台（微信、PC、WAP、转介绍）',
  `date_type` int(11) NOT NULL DEFAULT '0' COMMENT '数据来源（SEM、自然流量）',
  `source_type` int(11) NOT NULL DEFAULT '0' COMMENT '入口类型',
  
  `org_name` varchar(20) NOT NULL DEFAULT '' COMMENT '机构名称',
  `tsr_name` varchar(20) NOT NULL DEFAULT '' COMMENT '客服名称',
  `first_call` varchar(20) NOT NULL DEFAULT '' COMMENT '第一天拨打回馈',
  `second_call` varchar(20) NOT NULL DEFAULT '' COMMENT '第二天拨打回馈',
  `three_call` varchar(20) NOT NULL DEFAULT '' COMMENT '第三天拨打回馈',
  `call_status` varchar(20) NOT NULL DEFAULT '' COMMENT '拨打状态',
  `problem` TINYINT(1) NOT NULL DEFAULT FALSE COMMENT '是否问题数据',

  `pay_type` int(11) NOT NULL DEFAULT '0' COMMENT '缴费方式（年交、月交）',
  `insurance_amount` DECIMAL NOT NULL DEFAULT '0' COMMENT '保险额度',
  `insurance_fee` DECIMAL NOT NULL DEFAULT '0' COMMENT '保费',
  `finish_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '成单日期',
  `remark` varchar(64) NOT NULL DEFAULT '' COMMENT '备注',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='承保记录表';
