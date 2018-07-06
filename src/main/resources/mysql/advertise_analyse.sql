DROP TABLE IF EXISTS `advertise_analyse`;

CREATE TABLE `advertise_analyse` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stat_time` timestamp NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '统计日期',
  `merchant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '预约商户（如小米、华夏）',
  `platform` varchar(20) NOT NULL DEFAULT '' COMMENT '预约平台（微信、PC、WAP、转介绍）',
  `data_type` varchar(20) NOT NULL DEFAULT '' COMMENT '数据来源（SEM、自然流量）',

  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '展现量',
  `click_count` int(11) NOT NULL DEFAULT 0 COMMENT '点击量',
  `browse_count` int(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
  `arrive_count` int(11) NOT NULL DEFAULT 0 COMMENT '到达量',
  `arrive_user_count` int(11) NOT NULL DEFAULT 0 COMMENT '到达用户量',
  `arrive_rate` DECIMAL(5,2) NOT NULL DEFAULT 0 COMMENT '到达率',
  `again_count` int(11) NOT NULL DEFAULT 0 COMMENT '二跳量',
  `again_rate` DECIMAL(5,2) NOT NULL DEFAULT 0 COMMENT '二跳率',
  `average_stay_time` varchar(12) NOT NULL DEFAULT '' COMMENT '平均停留时间',
  `transform_count` int(11) NOT NULL DEFAULT 0 COMMENT '转化量',
  `direct_transform_count` int(11) NOT NULL DEFAULT 0 COMMENT '直接转化量',
  `back_transform_count` int(11) NOT NULL DEFAULT 0 COMMENT '回归转化量',
  `transform_cost` DECIMAL(14,2) NOT NULL DEFAULT 0 COMMENT '转化成本',
  `insurance_fee` DECIMAL(14,2) NOT NULL DEFAULT 0 COMMENT '保险费用',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态',
  `remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='广告分析表';

