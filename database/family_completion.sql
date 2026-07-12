USE `zzyl`;

INSERT INTO `t_bill`
(`bill_no`,`bill_type`,`elder_id`,`family_id`,`order_id`,`bill_month`,`fee_name`,`total_amount`,`paid_amount`,`status`)
VALUES
('ZD202607001','MONTHLY',1,1,NULL,'2026-07','床位护理月费',3000.00,3000.00,'PAID'),
('ZD202607002','SERVICE',1,1,1,'2026-07','肩颈按摩服务',75.00,75.00,'PAID'),
('ZD202607003','MONTHLY',1,1,NULL,'2026-08','床位护理月费',3000.00,0.00,'UNPAID')
ON DUPLICATE KEY UPDATE
`fee_name`=VALUES(`fee_name`),`total_amount`=VALUES(`total_amount`),
`paid_amount`=VALUES(`paid_amount`),`status`=VALUES(`status`);

UPDATE `t_nursimg_item` SET `itemname`='助浴',`price`=15.00,`unit`='次',
`description`='由护理人员协助长者沐浴，做好防滑与保暖照护。' WHERE `id`=2;
UPDATE `t_nursimg_item` SET `itemname`='肩颈按摩60分钟',`price`=98.90,`unit`='小时',
`description`='舒缓肩颈疲劳，服务前确认长者身体情况。' WHERE `id`=3;
UPDATE `t_nursimg_item` SET `itemname`='按摩',`price`=80.00,`unit`='小时',
`description`='专业护理人员提供舒缓按摩服务。' WHERE `id`=4;
