DROP TABLE IF EXISTS `exception_logger`;
CREATE TABLE `exception_logger` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `logger_key` varchar(32) NOT NULL DEFAULT '' COMMENT '关键字',
  `logger_date` varchar(10) NOT NULL DEFAULT '' COMMENT '反馈日期',
  `logger_type` varchar(24) NOT NULL DEFAULT '' COMMENT '反馈类型',
  `logger_desc` varchar(256) NOT NULL DEFAULT '' COMMENT '反馈内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='异常日志表';
alter table exception_logger add unique index idx_uniqued(`logger_key`, `logger_date`, `logger_type`);