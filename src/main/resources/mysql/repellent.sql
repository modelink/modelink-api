DROP TABLE IF EXISTS `repellent`;
CREATE TABLE `repellent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `merchant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '合作商户ID',
  `export_org_name` varchar(64) NOT NULL DEFAULT '' COMMENT '导出数据机构',
  `repellent_no` varchar(14) NOT NULL DEFAULT '' COMMENT '投保单号',
  `insurance_no` varchar(16) NOT NULL DEFAULT '' COMMENT '保单号',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '保单状态',
  `child_status` int(11) NOT NULL DEFAULT 0 COMMENT '保单子状态',
  `insurance_name` varchar(10) NOT NULL DEFAULT '' COMMENT '被保人名称',
  `product_name` varchar(64) NOT NULL DEFAULT '' COMMENT '产品名称',
  `extra_insurance` varchar(64) NOT NULL DEFAULT '' COMMENT '附加险种',
  `insurance_amount` varchar(20) NOT NULL DEFAULT '' COMMENT '保额',
  `year_insurance_fee` varchar(20) NOT NULL DEFAULT '' COMMENT '年化保费',
  `insurance_fee` varchar(20) NOT NULL DEFAULT '' COMMENT '标准保费',

  `tsr_number` varchar(10) NOT NULL DEFAULT '' COMMENT 'tsr工号',
  `tsr_name` varchar(10) NOT NULL DEFAULT '' COMMENT 'tsr姓名',
  `tl_number` varchar(10) NOT NULL DEFAULT '' COMMENT 'tl工号',
  `tl_name` varchar(10) NOT NULL DEFAULT '' COMMENT 'tl姓名',
  `org_name` varchar(64) NOT NULL DEFAULT '' COMMENT '管理机构',
  `department` varchar(64) NOT NULL DEFAULT '' COMMENT '部',
  `region_name` varchar(64) NOT NULL DEFAULT '' COMMENT '区',
  `group_name` varchar(64) NOT NULL DEFAULT '' COMMENT '组',

  `special_case_name` varchar(64) NOT NULL DEFAULT '' COMMENT '专案名称',
  `insurance_date` varchar(20) NOT NULL DEFAULT '' COMMENT '承保日期',
  `pay_type` int(11) NOT NULL DEFAULT 0 COMMENT '交费方式',
  `hesitate_date` varchar(10) NOT NULL DEFAULT '' COMMENT '犹豫日期',
  `pay_interval` int(11) NOT NULL DEFAULT 0 COMMENT '交费期间',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='退保数据明细表';