alter table `permission` modify column `value` varchar(256);
alter table `flow` modify column `website` varchar(128);

alter table `underwrite` add column `source_date` varchar(10) not null default '' after `reserve_date`;
alter table `underwrite` add column `keyword` varchar(128) not null default '' after `advertise_active`;
alter table `media_item` add column `fee_type` varchar(8) not null default '预约' after `average_rank`;
alter table `flow_reserve` add column `fee_type` varchar(8) not null default '预约' after `device_type`;

update flow as a, flow as b set a.average_stay_time = b.average_browse_page_count, a.average_browse_page_count = b.average_stay_time where a.id=b.id;


ALTER TABLE `flow_area` ADD INDEX rds_idx_date (`date`);
ALTER TABLE `media_item` ADD INDEX rds_idx_date (`date`);
ALTER TABLE `flow_reserve` ADD INDEX rds_idx_date (`date`);

alter table `flow_reserve` add column `fee_type` varchar(8) not null default '预约' after `device_type`;
alter table `underwrite` add column `advertise_series` varchar(20) not null default '' after `advertise_active`;