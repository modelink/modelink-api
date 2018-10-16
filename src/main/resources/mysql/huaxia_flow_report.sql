DROP TABLE IF EXISTS `huaxia_flow_report`;
CREATE TABLE `huaxia_flow_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `date` varchar(10) NOT NULL DEFAULT '' COMMENT '日期',
  `data_source` varchar(10) NOT NULL DEFAULT '' COMMENT '数据来源',
  `platform_name` varchar(20) NOT NULL DEFAULT '' COMMENT '渠道归属',
  `advertise_active` varchar(10) NOT NULL DEFAULT '' COMMENT '广告活动',
  `browse_count` int(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
  `click_count` int(11) NOT NULL DEFAULT 0 COMMENT '点击量',
  `arrive_count` int(11) NOT NULL DEFAULT 0 COMMENT '到达量',
  `arrive_user_count` int(11) NOT NULL DEFAULT 0 COMMENT '到达用户',
  `again_count` int(11) NOT NULL DEFAULT 0 COMMENT '二跳量',
  `average_stay_time` varchar(8) NOT NULL DEFAULT '' COMMENT '平均停留时间',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='华夏日报-基础流量数据表';