DROP TABLE IF EXISTS `flow_reserve`;
CREATE TABLE `flow_reserve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `date` varchar(10) NOT NULL DEFAULT '' COMMENT '日期',
  `time` varchar(10) NOT NULL DEFAULT '' COMMENT '时间',
  `merchant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '合作商户（如小米、华夏）',
  `platform_name` varchar(20) NOT NULL DEFAULT '' COMMENT '渠道归属（PC、移动端）',
  `city_id` int(11) NOT NULL DEFAULT 0 COMMENT '城市ID',
  `province_id` int(11) NOT NULL DEFAULT 0 COMMENT '省份ID',

  `reserve_no` varchar(20) NOT NULL DEFAULT '' COMMENT '预约编号',
  `reserve_mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '预约电话',

  `advertise_active` varchar(20) NOT NULL DEFAULT '' COMMENT '广告活动',
  `advertise_media` varchar(20) NOT NULL DEFAULT '' COMMENT '广告媒体',
  `advertise_series` varchar(20) NOT NULL DEFAULT '' COMMENT '广告系列',
  `advertise_desc` varchar(20) NOT NULL DEFAULT '' COMMENT '广告描述',
  `advertise_time` varchar(20) NOT NULL DEFAULT '' COMMENT '广告点击时间',
  `advertise_transform_time` varchar(20) NOT NULL DEFAULT '' COMMENT '广告转化时间',

  `first_advertise_active` varchar(20) NOT NULL DEFAULT '' COMMENT '首次点击广告活动',
  `first_advertise_media` varchar(20) NOT NULL DEFAULT '' COMMENT '首次点击广告媒体',
  `first_advertise_desc` varchar(20) NOT NULL DEFAULT '' COMMENT '首次点击广告描述',
  `first_advertise_time` varchar(20) NOT NULL DEFAULT '' COMMENT '首次广告点击时间',

  `station_advertise` varchar(20) NOT NULL DEFAULT '' COMMENT '站内广告',
  `station_advertise_time` varchar(20) NOT NULL DEFAULT '' COMMENT '站内广告点击时间',
  `station_advertise_transform_time` varchar(20) NOT NULL DEFAULT '' COMMENT '站内广告转化时间',

  `ip` varchar(20) NOT NULL DEFAULT '' COMMENT 'IP',
  `os` varchar(20) NOT NULL DEFAULT '' COMMENT '操作系统',
  `source` varchar(20) NOT NULL DEFAULT '' COMMENT '访问来源',
  `website` varchar(20) NOT NULL DEFAULT '' COMMENT '网站来源',
  `browser` varchar(20) NOT NULL DEFAULT '' COMMENT '浏览器',
  `search_word` varchar(20) NOT NULL DEFAULT '' COMMENT '搜索词',
  `key_word_group` varchar(20) NOT NULL DEFAULT '' COMMENT '关键词组',
  `device_type` varchar(20) NOT NULL DEFAULT '' COMMENT '设备类型',
  `transform_type` varchar(20) NOT NULL DEFAULT '' COMMENT '转化类型',
  `resolution_ratio` varchar(20) NOT NULL DEFAULT '' COMMENT '分辨率',
  `is_advertise` varchar(10) NOT NULL DEFAULT '' COMMENT '是否广告',
  `is_new_visitor` varchar(1) NOT NULL DEFAULT '' COMMENT '是否新访客',
  `is_make_up` varchar(1) NOT NULL DEFAULT '' COMMENT '是否充量',

  `last2_advertise_active` varchar(20) NOT NULL DEFAULT '' COMMENT '最后2次点击广告活动',
  `last2_advertise_media` varchar(20) NOT NULL DEFAULT '' COMMENT '最后2次点击广告媒体',
  `last2_advertise_desc` varchar(20) NOT NULL DEFAULT '' COMMENT '最后2次点击广告描述',
  `last2_advertise_time` varchar(20) NOT NULL DEFAULT '' COMMENT '最后2次广告点击时间',

  `last3_advertise_active` varchar(20) NOT NULL DEFAULT '' COMMENT '最后3次点击广告活动',
  `last3_advertise_media` varchar(20) NOT NULL DEFAULT '' COMMENT '最后3次点击广告媒体',
  `last3_advertise_desc` varchar(20) NOT NULL DEFAULT '' COMMENT '最后3次点击广告描述',
  `last3_advertise_time` varchar(20) NOT NULL DEFAULT '' COMMENT '最后3次广告点击时间',

  `theme_page` varchar(20) NOT NULL DEFAULT '' COMMENT '专题页面',
  `last2_theme_page` varchar(20) NOT NULL DEFAULT '' COMMENT '最后2次专题页面',
  `last2_theme_page_no` varchar(20) NOT NULL DEFAULT '' COMMENT '最后2次专题页面明细ID',
  `last2_theme_click_time` varchar(20) NOT NULL DEFAULT '' COMMENT '最后2次专题页面点击时间',
  `last2_theme_transform_time` varchar(20) NOT NULL DEFAULT '' COMMENT '最后2次专题页面转化时间',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='预约流量表';