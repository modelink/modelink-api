DROP TABLE IF EXISTS `estimate`;
CREATE TABLE `estimate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `date` varchar(10) NOT NULL DEFAULT '' COMMENT '时间',
  `platform_name` varchar(20) NOT NULL DEFAULT '' COMMENT '渠道归属',
  `advertise_active` varchar(20) NOT NULL DEFAULT '' COMMENT '广告活动',

  `transform_count` int(11) NOT NULL DEFAULT 0 COMMENT '测保转化数',
  `web_browse_count` int(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
  `web_click_count` int(11) NOT NULL DEFAULT 0 COMMENT '点击量',
  `arrive_count` int(11) NOT NULL DEFAULT 0 COMMENT '到达量',
  `arrive_user_count` int(11) NOT NULL DEFAULT 0 COMMENT '到达用户',
  `arrive_rate` varchar(10) NOT NULL DEFAULT '' COMMENT '到达率',
  `again_count` int(11) NOT NULL DEFAULT 0 COMMENT '二跳量',
  `again_rate` varchar(10) NOT NULL DEFAULT '' COMMENT '二跳率',
  `average_stay_time` varchar(10) NOT NULL DEFAULT 0 COMMENT '平均停留时间',

  `media_show_count` int(11) NOT NULL DEFAULT 0 COMMENT '展示数',
  `media_click_count` int(11) NOT NULL DEFAULT 0 COMMENT '点击数',
  `media_click_rate` varchar(10) NOT NULL DEFAULT '' COMMENT '点击率',
  `cpc` varchar(32) NOT NULL DEFAULT '' COMMENT 'cpc',
  `cpm` varchar(32) NOT NULL DEFAULT '' COMMENT 'cpm',
  `total_amount` varchar(32) NOT NULL DEFAULT '' COMMENT '总花费',
  `direct_transform_cost` varchar(32) NOT NULL DEFAULT '' COMMENT '直接转化成本',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='测保数据表';