DROP TABLE IF EXISTS `media_tactics`;
CREATE TABLE `media_tactics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `merchant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '合作商户',
  `month` varchar(10) NOT NULL DEFAULT '' COMMENT '日期（月）',
  `platform_name` varchar(32) NOT NULL DEFAULT '' COMMENT '渠道归属',
  `advertise_active` varchar(32) NOT NULL DEFAULT '' COMMENT '广告活动',

  `speed_cost` varchar(20) NOT NULL DEFAULT '' COMMENT '消费（元）',
  `reserve_count` int(11) NOT NULL DEFAULT 0 COMMENT '预约数量（个）',
  `insurance_count` int(11) NOT NULL DEFAULT 0 COMMENT '承保件数（个）',
  `insurance_fee` varchar(20) NOT NULL DEFAULT '' COMMENT '保费（元）',

  `operate_count` int(11) NOT NULL DEFAULT 0 COMMENT '总操作次数（次）',
  `optimize_key_word` int(11) NOT NULL DEFAULT 0 COMMENT '关键词优化（次）',
  `add_bid` int(11) NOT NULL DEFAULT 0 COMMENT '增加出价（次）',
  `reduce_bid` int(11) NOT NULL DEFAULT 0 COMMENT '降低出价（次）',
  `add_patten` int(11) NOT NULL DEFAULT 0 COMMENT '调宽匹配模式（次）',
  `reduce_patten` int(11) NOT NULL DEFAULT 0 COMMENT '调窄匹配模式（次）',
  `add_key_word` int(11) NOT NULL DEFAULT 0 COMMENT '增加关键词（次）',
  `reduce_key_word` int(11) NOT NULL DEFAULT 0 COMMENT '删除关键词（次）',
  `filte_key_word` int(11) NOT NULL DEFAULT 0 COMMENT '搜索词过滤（次）',
  `optimize_word_idea` int(11) NOT NULL DEFAULT 0 COMMENT '文字创意优化（次）',
  `add_style` int(11) NOT NULL DEFAULT 0 COMMENT '增加图片等高级样式（次）',
  `add_word_idea` int(11) NOT NULL DEFAULT 0 COMMENT '增加文字创意（次）',
  `optimize_image_idea` int(11) NOT NULL DEFAULT 0 COMMENT '展示类图片创意优化（次）',
  `add_image_idea` int(11) NOT NULL DEFAULT 0 COMMENT '增加图片创意（次）',
  `reduce_image_idea` int(11) NOT NULL DEFAULT 0 COMMENT '删除图片创意（次）',
  `modify_image_bid` int(11) NOT NULL DEFAULT 0 COMMENT '调整图片出价（次）',
  `optimize_flow_idea` int(11) NOT NULL DEFAULT 0 COMMENT '信息流文字创意优化（次）',
  `modify_copywrite` int(11) NOT NULL DEFAULT 0 COMMENT '文案调整（次）',
  `optimize_flow_people` int(11) NOT NULL DEFAULT 0 COMMENT '信息流人群优化（次）',
  `modify_key_word` int(11) NOT NULL DEFAULT 0 COMMENT '修改定向关键词（次）',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='媒体策略调整数据表';