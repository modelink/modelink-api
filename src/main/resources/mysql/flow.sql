DROP TABLE IF EXISTS `flow`;
CREATE TABLE `flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `loggerDate` varchar(10) NOT NULL DEFAULT '' COMMENT '时间',
  `merchant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '合作商户（如小米、华夏）',
  `platform_name` varchar(20) NOT NULL DEFAULT '' COMMENT '渠道归属（PC、移动端）',
  `website` varchar(32) NOT NULL DEFAULT '' COMMENT '网站来源',
  `source` varchar(20) NOT NULL DEFAULT '' COMMENT '来源类型',

  `browse_count` int(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
  `inflow_count` int(11) NOT NULL DEFAULT 0 COMMENT '流入量',
  `user_count` int(11) NOT NULL DEFAULT 0 COMMENT '用户数',
  `again_click_count` int(11) NOT NULL DEFAULT 0 COMMENT '二跳量',
  `again_click_rate` varchar(8) NOT NULL DEFAULT 0 COMMENT '二跳率',
  `average_stay_time` varchar(8) NOT NULL DEFAULT '' COMMENT '平均停留时间',
  `average_browse_page_count` varchar(8) NOT NULL DEFAULT '' COMMENT '平均浏览页面',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='流量统计总表';