DROP TABLE IF EXISTS `abnormal`;
CREATE TABLE `abnormal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `loggerDate` varchar(10) NOT NULL DEFAULT '' COMMENT '反馈时间',
  `org_name` varchar(20) NOT NULL DEFAULT '' COMMENT '机构名称',
  `tsr_name` varchar(20) NOT NULL DEFAULT '' COMMENT 'TSR姓名',

  `source` varchar(20) NOT NULL DEFAULT '' COMMENT '数据来源',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号码',
  `reserve_date` varchar(10) NOT NULL DEFAULT '' COMMENT '预约日期',
  `arrange_date` varchar(10) NOT NULL DEFAULT '' COMMENT '数据下发日期',
  `call_date` varchar(10) NOT NULL DEFAULT '' COMMENT '首拨日期',
  `call_result` varchar(32) NOT NULL DEFAULT '' COMMENT '首拨状态',
  `last_result` varchar(32) NOT NULL DEFAULT '' COMMENT '最终状态',
  `problem_data` varchar(8) NOT NULL DEFAULT '' COMMENT '是否问题数据',
  `call_count` int(11) NOT NULL DEFAULT 0 COMMENT '拨打次数',
  `source_media` varchar(32) NOT NULL DEFAULT '' COMMENT '内部媒体',
  `device_name` varchar(32) NOT NULL DEFAULT '' COMMENT '设备名称',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='异常数据表';