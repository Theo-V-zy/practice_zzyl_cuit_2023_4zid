/*
  ZZYL core database design v1.
  Lean version for group development. Keeps teacher tables compatible.
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `zzyl` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `zzyl`;

DROP TABLE IF EXISTS `t_message`;
DROP TABLE IF EXISTS `t_device`;
DROP TABLE IF EXISTS `t_bill`;
DROP TABLE IF EXISTS `t_order`;
DROP TABLE IF EXISTS `t_nursing_task`;
DROP TABLE IF EXISTS `t_apply`;
DROP TABLE IF EXISTS `t_contract`;
DROP TABLE IF EXISTS `t_bed`;
DROP TABLE IF EXISTS `t_visit`;
DROP TABLE IF EXISTS `t_customer`;
DROP TABLE IF EXISTS `t_family_elder`;
DROP TABLE IF EXISTS `t_family_user`;
DROP TABLE IF EXISTS `t_elder`;
DROP TABLE IF EXISTS `t_role`;
DROP TABLE IF EXISTS `t_post`;
DROP TABLE IF EXISTS `t_department`;
DROP TABLE IF EXISTS `t_plain_item`;
DROP TABLE IF EXISTS `t_nursing_plain`;
DROP TABLE IF EXISTS `t_nursimg_item`;
DROP TABLE IF EXISTS `t_menu`;
DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(255) DEFAULT NULL,
  `upwd` varchar(255) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `job` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `islock` int DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `dept_id` int DEFAULT NULL,
  `post_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_account` (`account`),
  KEY `idx_user_dept` (`dept_id`),
  KEY `idx_user_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` int DEFAULT NULL,
  `mname` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `visible` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_menu_pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_nursimg_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `itemname` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `sort` varchar(255) DEFAULT NULL,
  `islock` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_nursing_plain` (
  `id` int NOT NULL AUTO_INCREMENT,
  `plainname` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `createuser` varchar(255) DEFAULT NULL,
  `islock` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_plain_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `item_id` int NOT NULL,
  `plain_id` int NOT NULL,
  `itemname` varchar(255) DEFAULT NULL,
  `hlsj` varchar(255) DEFAULT NULL,
  `hlzq` varchar(255) DEFAULT NULL,
  `hlpc` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_plain_item_item_id` (`item_id`),
  KEY `idx_plain_item_plain_id` (`plain_id`),
  CONSTRAINT `fk_plain_item_item` FOREIGN KEY (`item_id`) REFERENCES `t_nursimg_item` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_plain_item_plain` FOREIGN KEY (`plain_id`) REFERENCES `t_nursing_plain` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_id` int DEFAULT 0,
  `dept_name` varchar(100) NOT NULL,
  `leader` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `sort` int DEFAULT 1,
  `status` int DEFAULT 1,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_department_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_post` (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_name` varchar(100) NOT NULL,
  `post_code` varchar(50) DEFAULT NULL,
  `sort` int DEFAULT 1,
  `status` int DEFAULT 1,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_code` (`post_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) NOT NULL,
  `role_code` varchar(50) NOT NULL,
  `menu_ids` text,
  `data_scope` varchar(30) DEFAULT 'ALL',
  `status` int DEFAULT 1,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_elder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `elder_no` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `id_card` varchar(30) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'WAIT_CHECKIN',
  `health_status` varchar(50) DEFAULT NULL,
  `ability_level` varchar(50) DEFAULT NULL,
  `nursing_plain_id` int DEFAULT NULL,
  `family_contact` varchar(50) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_elder_no` (`elder_no`),
  KEY `idx_elder_name` (`name`),
  KEY `idx_elder_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_family_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `status` int DEFAULT 1,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_family_account` (`account`),
  KEY `idx_family_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_family_elder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_id` int NOT NULL,
  `elder_id` int NOT NULL,
  `relation` varchar(30) DEFAULT NULL,
  `is_default` int DEFAULT 0,
  `status` int DEFAULT 1,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_family_elder` (`family_id`, `elder_id`),
  KEY `idx_family_elder_elder` (`elder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_no` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `source` varchar(50) DEFAULT NULL,
  `intention_level` varchar(20) DEFAULT NULL,
  `follow_content` varchar(1000) DEFAULT NULL,
  `next_follow_time` datetime DEFAULT NULL,
  `status` varchar(30) DEFAULT 'FOLLOWING',
  `remark` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_no` (`customer_no`),
  KEY `idx_customer_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_visit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `visit_no` varchar(50) NOT NULL,
  `visit_stage` varchar(30) DEFAULT 'RESERVATION',
  `visitor_name` varchar(50) NOT NULL,
  `visitor_phone` varchar(30) DEFAULT NULL,
  `visit_type` varchar(30) DEFAULT NULL,
  `elder_id` int DEFAULT NULL,
  `elder_name` varchar(50) DEFAULT NULL,
  `appointment_time` datetime DEFAULT NULL,
  `arrive_time` datetime DEFAULT NULL,
  `leave_time` datetime DEFAULT NULL,
  `status` varchar(30) DEFAULT 'PENDING',
  `remark` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_visit_no` (`visit_no`),
  KEY `idx_visit_phone` (`visitor_phone`),
  KEY `idx_visit_time` (`appointment_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_bed` (
  `id` int NOT NULL AUTO_INCREMENT,
  `building` varchar(50) DEFAULT NULL,
  `floor` varchar(50) DEFAULT NULL,
  `room_no` varchar(50) NOT NULL,
  `room_type` varchar(50) DEFAULT NULL,
  `bed_no` varchar(50) NOT NULL,
  `bed_code` varchar(50) DEFAULT NULL,
  `bed_price` decimal(10,2) DEFAULT 0.00,
  `elder_id` int DEFAULT NULL,
  `device_no` varchar(50) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'EMPTY',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bed_code` (`bed_code`),
  KEY `idx_bed_room` (`room_no`),
  KEY `idx_bed_elder` (`elder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_contract` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contract_no` varchar(50) NOT NULL,
  `elder_id` int NOT NULL,
  `family_id` int DEFAULT NULL,
  `bed_id` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `deposit` decimal(10,2) DEFAULT 0.00,
  `monthly_fee` decimal(10,2) DEFAULT 0.00,
  `status` varchar(30) DEFAULT 'ACTIVE',
  `file_url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_contract_no` (`contract_no`),
  KEY `idx_contract_elder` (`elder_id`),
  KEY `idx_contract_family` (`family_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_apply` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apply_no` varchar(50) NOT NULL,
  `apply_type` varchar(30) NOT NULL,
  `elder_id` int DEFAULT NULL,
  `family_id` int DEFAULT NULL,
  `apply_user_id` int DEFAULT NULL,
  `current_step` varchar(50) DEFAULT NULL,
  `reason` varchar(500) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'PENDING',
  `approve_user_id` int DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL,
  `approve_comment` varchar(500) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_apply_no` (`apply_no`),
  KEY `idx_apply_type_status` (`apply_type`, `status`),
  KEY `idx_apply_elder` (`elder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_nursing_task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `task_no` varchar(50) NOT NULL,
  `elder_id` int NOT NULL,
  `item_id` int NOT NULL,
  `plain_id` int DEFAULT NULL,
  `nurse_id` int DEFAULT NULL,
  `scheduled_time` datetime DEFAULT NULL,
  `execute_time` datetime DEFAULT NULL,
  `execute_result` varchar(500) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'PENDING',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_nursing_task_no` (`task_no`),
  KEY `idx_task_elder` (`elder_id`),
  KEY `idx_task_nurse` (`nurse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL,
  `family_id` int DEFAULT NULL,
  `elder_id` int DEFAULT NULL,
  `service_item_id` int DEFAULT NULL,
  `service_name` varchar(100) DEFAULT NULL,
  `quantity` int DEFAULT 1,
  `total_amount` decimal(10,2) DEFAULT 0.00,
  `pay_amount` decimal(10,2) DEFAULT 0.00,
  `pay_status` varchar(30) DEFAULT 'UNPAID',
  `order_status` varchar(30) DEFAULT 'CREATED',
  `refund_amount` decimal(10,2) DEFAULT 0.00,
  `refund_reason` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_order_family` (`family_id`),
  KEY `idx_order_elder` (`elder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_bill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(50) NOT NULL,
  `bill_type` varchar(30) DEFAULT 'MONTHLY',
  `elder_id` int DEFAULT NULL,
  `family_id` int DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  `bill_month` varchar(20) DEFAULT NULL,
  `fee_name` varchar(100) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT 0.00,
  `paid_amount` decimal(10,2) DEFAULT 0.00,
  `balance_after` decimal(10,2) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'UNPAID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bill_no` (`bill_no`),
  KEY `idx_bill_elder` (`elder_id`),
  KEY `idx_bill_family` (`family_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_device` (
  `id` int NOT NULL AUTO_INCREMENT,
  `device_no` varchar(50) NOT NULL,
  `device_name` varchar(100) DEFAULT NULL,
  `device_type` varchar(50) DEFAULT NULL,
  `elder_id` int DEFAULT NULL,
  `bed_id` int DEFAULT NULL,
  `online_status` varchar(30) DEFAULT 'OFFLINE',
  `last_event_type` varchar(50) DEFAULT NULL,
  `last_event_value` varchar(255) DEFAULT NULL,
  `last_event_time` datetime DEFAULT NULL,
  `alert_rule` varchar(255) DEFAULT NULL,
  `alert_status` varchar(30) DEFAULT NULL,
  `alert_content` varchar(500) DEFAULT NULL,
  `status` int DEFAULT 1,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_no` (`device_no`),
  KEY `idx_device_elder` (`elder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` text,
  `message_type` varchar(50) DEFAULT NULL,
  `receiver_type` varchar(30) DEFAULT 'USER',
  `receiver_id` int DEFAULT NULL,
  `read_status` int DEFAULT 0,
  `read_time` datetime DEFAULT NULL,
  `enabled` int DEFAULT 1,
  `business_type` varchar(50) DEFAULT NULL,
  `business_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_message_receiver` (`receiver_type`, `receiver_id`),
  KEY `idx_message_business` (`business_type`, `business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `t_user` (`id`, `account`, `upwd`, `realname`, `email`, `department`, `job`, `role`, `phone`, `sex`, `islock`, `image`, `dept_id`, `post_id`, `role_id`) VALUES
(1, '20260023', '222222', '马云', 'my@173.com', '研发部', '程序员', '职员', '12012012022', '女', 0, 'https://zzyl-706.oss-cn-chengdu.aliyuncs.com/a1d4af5d-d28f-4920-9e02-24ecfc48304a.png', 1, 1, 1);

INSERT INTO `t_menu` (`id`, `pid`, `mname`, `path`, `sort`, `visible`) VALUES
(1, 0, '个人中心', NULL, 1, 1),
(2, 1, '个人信息', '/UserInfo', 1, 1),
(3, 1, '修改密码', '/ModifyPwd', 2, 1),
(4, 0, '服务管理', NULL, 2, 1),
(5, 4, '护理规划', '', 1, 1),
(6, 5, '护理项目', '/NursingItem', 1, 1),
(7, 5, '护理计划', '/NursingPlain', 2, 1),
(8, 5, '护理等级', '/NursingLevel', 3, 1),
(9, 0, '工作台', '/Dashboard', 3, 1),
(10, 0, '来访管理', '/Visit', 4, 1),
(11, 0, '入退管理', '/Apply', 5, 1),
(12, 0, '在住管理', '/Resident', 6, 1),
(13, 0, '订单管理', '/Order', 7, 1),
(14, 0, '财务管理', '/Bill', 8, 1),
(15, 0, '客户管理', '/Customer', 9, 1),
(16, 0, '协同工作', '/Todo', 10, 1),
(17, 0, '智能监测', '/Device', 11, 1),
(18, 0, '消息中心', '/Messages', 12, 1),
(19, 0, '权限配置', NULL, 13, 1),
(20, 19, '用户信息', '/UserManage', 1, 1),
(21, 19, '角色管理', '/RoleManage', 2, 1),
(22, 19, '菜单管理', '/MenuManage', 3, 1),
(23, 19, '部门管理', '/Department', 4, 1),
(24, 19, '职位管理', '/Post', 5, 1);

INSERT INTO `t_nursimg_item` (`id`, `itemname`, `price`, `unit`, `sort`, `islock`, `image`, `description`) VALUES
(2, '康复运行', 10.00, '天', '1', '启用', 'https://zzyl-706.oss-cn-chengdu.aliyuncs.com/df28271b-306b-4627-b5f1-672ad4c27689.png', '康复运行'),
(3, '助餐', 10.00, '次', '1', '启用', 'https://zzyl-706.oss-cn-chengdu.aliyuncs.com/df28271b-306b-4627-b5f1-672ad4c27689.png', '助餐'),
(4, '理发', 10.00, '次', '1', '启用', 'https://zzyl-706.oss-cn-chengdu.aliyuncs.com/df28271b-306b-4627-b5f1-672ad4c27689.png', '理发');

INSERT INTO `t_nursing_plain` (`id`, `plainname`, `createtime`, `createuser`, `islock`) VALUES
(1, 'A级护理计划', '2026-07-09 15:12:55', '马云', '启动'),
(2, 'B级护理计划', '2026-07-09 15:15:59', '马云', '启动'),
(3, 'B级护理计划', '2026-07-09 15:18:19', '马云', '启动'),
(4, 'D护理计划', '2026-07-09 15:19:57', '马云', '启动');

INSERT INTO `t_plain_item` (`id`, `item_id`, `plain_id`, `itemname`, `hlsj`, `hlzq`, `hlpc`) VALUES
(1, 2, 3, '康复运行', '2026-07-09T07:12:15.000Z', '天', 2),
(2, 2, 4, '康复运行', '2026-07-09T07:19:12.000Z', '天', 1),
(3, 3, 4, '助餐', '2026-07-09T07:19:12.000Z', '周', 2),
(4, 4, 4, '理发', '2026-07-09T07:19:12.000Z', '月', 3);

INSERT INTO `t_department` (`id`, `parent_id`, `dept_name`, `leader`, `phone`, `sort`, `status`) VALUES
(1, 0, '智慧养老中心', '马云', '12012012022', 1, 1),
(2, 1, '护理部', NULL, NULL, 1, 1),
(3, 1, '财务部', NULL, NULL, 2, 1),
(4, 1, '运营部', NULL, NULL, 3, 1);

INSERT INTO `t_post` (`id`, `post_name`, `post_code`, `sort`, `status`) VALUES
(1, '管理员', 'admin', 1, 1),
(2, '护理员', 'nurse', 2, 1),
(3, '财务人员', 'finance', 3, 1),
(4, '运营人员', 'operator', 4, 1);

INSERT INTO `t_role` (`id`, `role_name`, `role_code`, `menu_ids`, `data_scope`, `status`, `remark`) VALUES
(1, '系统管理员', 'ADMIN', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24', 'ALL', 1, '默认管理员角色'),
(2, '护理人员', 'NURSE', '1,2,4,5,6,7,8,12,16', 'DEPT', 1, '护理任务执行'),
(3, '财务人员', 'FINANCE', '1,2,13,14', 'ALL', 1, '订单和账单管理');

INSERT INTO `t_elder` (`id`, `elder_no`, `name`, `gender`, `status`, `family_contact`) VALUES
(1, 'ELD202607001', '张建国', '男', 'IN', '张小明');

INSERT INTO `t_family_user` (`id`, `account`, `password`, `name`, `phone`, `status`) VALUES
(1, 'family001', '123456', '张小明', '13800000001', 1);

INSERT INTO `t_family_elder` (`family_id`, `elder_id`, `relation`, `is_default`, `status`) VALUES
(1, 1, '子女', 1, 1);

INSERT INTO `t_bill` (`bill_no`,`bill_type`,`elder_id`,`family_id`,`bill_month`,`fee_name`,`total_amount`,`paid_amount`,`status`) VALUES
('ZD202607001','MONTHLY',1,1,'2026-07','床位护理月费',3000.00,3000.00,'PAID'),
('ZD202607002','SERVICE',1,1,'2026-07','肩颈按摩服务',75.00,75.00,'PAID'),
('ZD202607003','MONTHLY',1,1,'2026-08','床位护理月费',3000.00,0.00,'UNPAID');

INSERT INTO `t_bed` (`id`, `building`, `floor`, `room_no`, `room_type`, `bed_no`, `bed_code`, `bed_price`, `elder_id`, `status`) VALUES
(1, 'A栋', '3层', 'A301', '单人间', '1号床', 'BED-A301-1', 3000.00, 1, 'OCCUPIED'),
(2, 'A栋', '3层', 'A302', '双人间', '1号床', 'BED-A302-1', 2000.00, NULL, 'EMPTY'),
(3, 'A栋', '3层', 'A302', '双人间', '2号床', 'BED-A302-2', 2000.00, NULL, 'EMPTY');

SET FOREIGN_KEY_CHECKS = 1;
