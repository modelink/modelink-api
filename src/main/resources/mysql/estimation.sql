DROP TABLE IF EXISTS `estimation`;
CREATE TABLE `estimation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '测保预约ID',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '预约用户',
  `gender` varchar(2) NOT NULL DEFAULT '' COMMENT '用户性别',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '用户手机号',
  `birthday` varchar(10) NOT NULL DEFAULT '' COMMENT '用户生日',
  `source_type` int(11) NOT NULL DEFAULT 0 COMMENT '资源类型',
  `duration` int(11) NOT NULL DEFAULT 0 COMMENT '交费期限',
  `insurance_amount` int(11) NOT NULL DEFAULT 0 COMMENT '保额',
  `merchant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '合作商户ID',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测保预约数据';