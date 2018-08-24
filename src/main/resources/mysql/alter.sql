alter table `permission` modify column `value` varchar(256);
alter table `flow` modify column `website` varchar(128);

alter table `underwrite` add column `source_date` varchar(10) not null default '' after `reserve_date`;
alter table `underwrite` add column `keyword` varchar(128) not null default '' after `advertise_active`;