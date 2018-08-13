DROP TABLE IF EXISTS `media_item`;
CREATE TABLE `media_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `date` varchar(10) NOT NULL DEFAULT '' COMMENT '日期',
  `merchant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '预约商户（如小米、华夏）',
  `platform_name` varchar(20) NOT NULL DEFAULT '' COMMENT '渠道归属（PC、移动端）',
  `source_type` varchar(20) NOT NULL DEFAULT '' COMMENT '广告活动（百度SEM、自然流量）',
  `source_media` varchar(20) NOT NULL DEFAULT '' COMMENT '广告媒体',

  `advertise_series` varchar(32) NOT NULL DEFAULT '' COMMENT '广告系列',
  `key_word_group` varchar(32) NOT NULL DEFAULT '' COMMENT '关键词组',

  `advertise_desc` varchar(32) NOT NULL DEFAULT '' COMMENT '广告描述',
  `key_word` varchar(32) NOT NULL DEFAULT '' COMMENT '关键词',
  `popularize_cell` varchar(32) NOT NULL DEFAULT '' COMMENT '推广单元',
  `popularize_plan` varchar(32) NOT NULL DEFAULT '' COMMENT '推广计划',
  `show_count` int(11) NOT NULL DEFAULT 0 COMMENT '展现量',
  `click_count` int(11) NOT NULL DEFAULT 0 COMMENT '点击量',
  `speed_cost` varchar(32) NOT NULL DEFAULT '' COMMENT '花费成本',

  `click_rate` varchar(10) NOT NULL DEFAULT '' COMMENT '点击率',
  `average_click_price` varchar(32) NOT NULL DEFAULT '' COMMENT '平均点击价格',
  `average_rank` varchar(32) NOT NULL DEFAULT '' COMMENT '平均排名',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='媒体数据明细表';