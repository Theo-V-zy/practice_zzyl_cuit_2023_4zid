USE `zzyl`;

INSERT INTO `t_family_user` (`account`,`password`,`name`,`phone`,`status`)
VALUES ('family001','123456','张小明','13800000001',1)
ON DUPLICATE KEY UPDATE
`password`=VALUES(`password`),`name`=VALUES(`name`),`phone`=VALUES(`phone`),`status`=1;

INSERT INTO `t_elder` (`elder_no`,`name`,`gender`,`status`,`family_contact`)
VALUES ('ELD202607001','张建国','男','IN','张小明')
ON DUPLICATE KEY UPDATE
`name`=VALUES(`name`),`gender`=VALUES(`gender`),`status`='IN',`family_contact`=VALUES(`family_contact`);

SET @family_id = (SELECT `id` FROM `t_family_user` WHERE `account`='family001' LIMIT 1);
SET @elder_id = (SELECT `id` FROM `t_elder` WHERE `elder_no`='ELD202607001' LIMIT 1);

INSERT INTO `t_family_elder` (`family_id`,`elder_id`,`relation`,`is_default`,`status`)
VALUES (@family_id,@elder_id,'子女',1,1)
ON DUPLICATE KEY UPDATE
`relation`=VALUES(`relation`),`is_default`=1,`status`=1;

INSERT INTO `t_bed`
(`building`,`floor`,`room_no`,`room_type`,`bed_no`,`bed_code`,`bed_price`,`elder_id`,`status`)
VALUES ('A栋','3层','A301','单人间','1号床','BED-A301-1',3000.00,@elder_id,'OCCUPIED')
ON DUPLICATE KEY UPDATE
`elder_id`=@elder_id,`status`='OCCUPIED',`bed_price`=VALUES(`bed_price`);

INSERT INTO `t_nursimg_item`
(`id`,`itemname`,`price`,`unit`,`sort`,`islock`,`description`)
VALUES
(2,'助浴',15.00,'次','1','0','由护理人员协助长者沐浴，做好防滑与保暖照护。'),
(3,'肩颈按摩60分钟',98.90,'小时','2','0','舒缓肩颈疲劳，服务前确认长者身体情况。'),
(4,'按摩',80.00,'小时','3','0','专业护理人员提供舒缓按摩服务。')
ON DUPLICATE KEY UPDATE
`itemname`=VALUES(`itemname`),`price`=VALUES(`price`),`unit`=VALUES(`unit`),
`sort`=VALUES(`sort`),`islock`=VALUES(`islock`),`description`=VALUES(`description`);

INSERT INTO `t_bill`
(`bill_no`,`bill_type`,`elder_id`,`family_id`,`order_id`,`bill_month`,`fee_name`,`total_amount`,`paid_amount`,`status`)
VALUES
('ZD202607001','MONTHLY',@elder_id,@family_id,NULL,'2026-07','床位护理月费',3000.00,3000.00,'PAID'),
('ZD202607002','SERVICE',@elder_id,@family_id,NULL,'2026-07','肩颈按摩服务',75.00,75.00,'PAID'),
('ZD202607003','MONTHLY',@elder_id,@family_id,NULL,'2026-08','床位护理月费',3000.00,0.00,'UNPAID')
ON DUPLICATE KEY UPDATE
`elder_id`=VALUES(`elder_id`),`family_id`=VALUES(`family_id`),`fee_name`=VALUES(`fee_name`),`total_amount`=VALUES(`total_amount`),
`paid_amount`=VALUES(`paid_amount`),`status`=VALUES(`status`);
