DROP TABLE IF EXISTS `sms`;

CREATE TABLE `sms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone_numbers` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号',
  `template_code` varchar(13) NOT NULL DEFAULT '' COMMENT '短信模板',
  `template_param` varchar(256) NOT NULL DEFAULT '' COMMENT '短信模板参数',
  `sign_name` varchar(12) NOT NULL DEFAULT '' COMMENT '短信签名',
  `out_id` varchar(32) NOT NULL DEFAULT '' COMMENT '外部流水扩展字段',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态：0-初始化；200-正常；404-失败；',
  `result` varchar(256) NOT NULL DEFAULT '' COMMENT '短信发送结果',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='短信明细';
