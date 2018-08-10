DROP TABLE IF EXISTS `abnormal`;
CREATE TABLE `abnormal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `date` varchar(10) NOT NULL DEFAULT '' COMMENT '反馈时间',
  `org_name` varchar(20) NOT NULL DEFAULT 0 COMMENT '机构名称',
  `tsr_name` varchar(20) NOT NULL DEFAULT '' COMMENT 'TSR姓名',

  `source` varchar(20) NOT NULL DEFAULT 0 COMMENT '数据来源',
  `mobile` varchar(20) NOT NULL DEFAULT 0 COMMENT '手机号码',
  `arrange_date` varchar(10) NOT NULL DEFAULT 0 COMMENT '数据下发日期',
  `call_date` varchar(10) NOT NULL DEFAULT 0 COMMENT '首拨日期',
  `call_result` varchar(32) NOT NULL DEFAULT 0 COMMENT '首拨状态',
  `first_call_result` varchar(32) NOT NULL DEFAULT 0 COMMENT '第1天拨打结果',
  `second_call_result` varchar(32) NOT NULL DEFAULT '' COMMENT '第2天拨打结果',
  `third_call_result` varchar(32) NOT NULL DEFAULT '' COMMENT '第3天拨打结果',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='异常数据表';