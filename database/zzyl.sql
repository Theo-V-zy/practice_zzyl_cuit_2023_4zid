-- MySQL dump 10.13  Distrib 5.7.24, for osx11.1 (x86_64)
--
-- Host: localhost    Database: zzyl
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS `zzyl` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `zzyl`;

--
-- Table structure for table `t_apply`
--

DROP TABLE IF EXISTS `t_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  KEY `idx_apply_type_status` (`apply_type`,`status`),
  KEY `idx_apply_elder` (`elder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_apply` VALUES (1,'SQ20260710001','CHECKIN',5,1,NULL,NULL,NULL,'APPROVED',NULL,'2026-07-12 09:17:41','',NULL,NULL,'2026-07-10 17:04:18','2026-07-12 16:38:38'),(2,'SQ20260710002','LEAVE',1,1,NULL,NULL,NULL,'PENDING',NULL,NULL,NULL,NULL,NULL,'2026-07-10 17:04:18','2026-07-10 17:04:18');

--
-- Table structure for table `t_bed`
--

DROP TABLE IF EXISTS `t_bed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_bed` (
  `id` int NOT NULL AUTO_INCREMENT,
  `building` varchar(50) DEFAULT NULL,
  `floor` varchar(50) DEFAULT NULL,
  `room_no` varchar(50) NOT NULL,
  `room_type` varchar(50) DEFAULT NULL,
  `bed_no` varchar(50) NOT NULL,
  `bed_code` varchar(50) DEFAULT NULL,
  `bed_price` decimal(10,2) DEFAULT '0.00',
  `elder_id` int DEFAULT NULL,
  `device_no` varchar(50) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'EMPTY',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bed_code` (`bed_code`),
  KEY `idx_bed_room` (`room_no`),
  KEY `idx_bed_elder` (`elder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_bed` VALUES (1,'A栋','3层','A301','单人间','1号床','BED-A301-1',3000.00,1,NULL,'OCCUPIED',NULL),(2,'A栋','3层','A302','双人间','1号床','BED-A302-1',2000.00,NULL,NULL,'EMPTY',NULL),(3,'A栋','3层','A302','双人间','2号床','BED-A302-2',2000.00,NULL,NULL,'EMPTY',NULL),(4,'A栋','3层','A303','单人间','1号床','BED-A303-1',3500.00,2,NULL,'OCCUPIED',NULL),(5,'A栋','4层','A401','双人间','1号床','BED-A401-1',2500.00,3,NULL,'OCCUPIED',NULL),(6,'A栋','4层','A401','双人间','2号床','BED-A401-2',2500.00,4,NULL,'OCCUPIED',NULL),(7,'B栋','1层','B101','单人间','1号床','BED-B101-1',4000.00,NULL,NULL,'EMPTY',NULL),(8,'B栋','2层','B201','三人间','1号床','BED-B201-1',1800.00,NULL,NULL,'EMPTY',NULL),(9,'B栋','2层','B201','三人间','2号床','BED-B201-2',1800.00,NULL,NULL,'EMPTY',NULL),(10,'B栋','2层','B201','三人间','3号床','BED-B201-3',1800.00,NULL,NULL,'EMPTY',NULL);

--
-- Table structure for table `t_bill`
--

DROP TABLE IF EXISTS `t_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_bill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(50) NOT NULL,
  `bill_type` varchar(30) DEFAULT 'MONTHLY',
  `elder_id` int DEFAULT NULL,
  `family_id` int DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  `bill_month` varchar(20) DEFAULT NULL,
  `fee_name` varchar(100) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT '0.00',
  `paid_amount` decimal(10,2) DEFAULT '0.00',
  `balance_after` decimal(10,2) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'UNPAID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bill_no` (`bill_no`),
  KEY `idx_bill_elder` (`elder_id`),
  KEY `idx_bill_family` (`family_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_bill` VALUES (1,'ZD202607001','MONTHLY',1,1,NULL,'2026-07','床位护理月费',3000.00,3000.00,NULL,'PAID','2026-07-12 08:58:48'),(2,'ZD202607002','SERVICE',1,1,1,'2026-07','肩颈按摩服务',75.00,75.00,NULL,'PAID','2026-07-12 08:58:48'),(3,'ZD202607003','MONTHLY',1,1,NULL,'2026-08','床位护理月费',3000.00,0.00,NULL,'UNPAID','2026-07-12 08:58:48'),(10,'ZD202607004','MONTHLY',2,1,NULL,'2026-06','床位护理月费',3000.00,3000.00,NULL,'PAID','2026-06-01 00:00:00'),(11,'ZD202607005','MONTHLY',2,1,NULL,'2026-07','床位护理月费',3000.00,0.00,NULL,'UNPAID','2026-07-01 00:00:00'),(12,'ZD202607006','SERVICE',1,1,5,'2026-07','康复运行服务费',10.00,10.00,NULL,'PAID','2026-07-12 14:57:33'),(13,'ZD202607007','SERVICE',1,1,6,'2026-07','助餐服务费',20.00,20.00,NULL,'PAID','2026-07-12 14:57:33'),(14,'ZD202607008','SERVICE',2,1,8,'2026-07','康复运行服务费',30.00,30.00,NULL,'PAID','2026-07-12 14:57:33'),(15,'ZD202607009','PREPAY',1,1,NULL,'2026-07','预缴款充值',5000.00,5000.00,NULL,'PAID','2026-07-01 00:00:00'),(16,'ZD202607010','PREPAY',2,1,NULL,'2026-07','预缴款充值',3000.00,3000.00,NULL,'PAID','2026-07-01 00:00:00'),(17,'ZD202605001','MONTHLY',1,1,NULL,'2026-05','床位护理月费',3000.00,3000.00,NULL,'PAID','2026-05-01 00:00:00'),(18,'ZD202605002','MONTHLY',2,1,NULL,'2026-05','床位护理月费',3000.00,3000.00,NULL,'PAID','2026-05-01 00:00:00'),(19,'ZD202606001','MONTHLY',1,1,NULL,'2026-06','床位护理月费',3000.00,3000.00,NULL,'PAID','2026-06-01 00:00:00'),(20,'ZD202606002','MONTHLY',2,1,NULL,'2026-06','床位护理月费',3000.00,0.00,NULL,'UNPAID','2026-06-01 00:00:00'),(21,'ZD202606003','SERVICE',1,1,NULL,'2026-06','理发/助餐服务费',40.00,40.00,NULL,'PAID','2026-06-30 00:00:00');

--
-- Table structure for table `t_contract`
--

DROP TABLE IF EXISTS `t_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_contract` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contract_no` varchar(50) NOT NULL,
  `elder_id` int NOT NULL,
  `family_id` int DEFAULT NULL,
  `bed_id` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `deposit` decimal(10,2) DEFAULT '0.00',
  `monthly_fee` decimal(10,2) DEFAULT '0.00',
  `status` varchar(30) DEFAULT 'ACTIVE',
  `file_url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_contract_no` (`contract_no`),
  KEY `idx_contract_elder` (`elder_id`),
  KEY `idx_contract_family` (`family_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_contract` VALUES (1,'HT20260710001',1,1,1,'2026-07-01','2028-06-08',5000.00,3000.00,'ACTIVE',NULL,'2026-07-10 17:04:18'),(2,'HT20260710002',2,1,3,'2026-07-01','2028-06-08',3000.00,2500.00,'ACTIVE',NULL,'2026-07-10 17:04:18');

--
-- Table structure for table `t_customer`
--

DROP TABLE IF EXISTS `t_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_customer` VALUES (1,'CUS001','赵大爷','13900001001','男','线上咨询','A','已来院参观，对双人间满意','2026-07-20 10:00:00','FOLLOWING',NULL,'2026-07-12 14:55:34','2026-07-12 14:55:34'),(2,'CUS002','钱奶奶','13900001002','女','转介绍','B','通过赵大爷介绍，关注护理服务','2026-07-22 14:00:00','FOLLOWING',NULL,'2026-07-12 14:55:34','2026-07-12 14:55:34'),(3,'CUS003','孙叔叔','13900001003','男','线下活动','A','社区讲座后咨询，意向单人间','2026-07-25 09:30:00','FOLLOWING',NULL,'2026-07-12 14:55:34','2026-07-12 14:55:34'),(4,'CUS004','周阿姨','13900001004','女','电话咨询','C','简单了解，暂无明确需求','2026-07-30 15:00:00','FOLLOWING',NULL,'2026-07-12 14:55:34','2026-07-12 14:55:34');

--
-- Table structure for table `t_department`
--

DROP TABLE IF EXISTS `t_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_id` int DEFAULT '0',
  `dept_name` varchar(100) NOT NULL,
  `leader` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `sort` int DEFAULT '1',
  `status` int DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_department_parent` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_department` VALUES (1,0,'中州养老院','马云','12012012022',1,1,'2026-07-10 15:07:44','2026-07-10 17:11:47'),(2,1,'财务部',NULL,NULL,1,1,'2026-07-10 15:07:44','2026-07-10 17:11:47'),(3,2,'会计组',NULL,NULL,2,1,'2026-07-10 15:07:44','2026-07-10 17:11:47'),(4,2,'结算组',NULL,NULL,3,1,'2026-07-10 15:07:44','2026-07-10 17:11:47'),(5,1,'行政部','行政部部长',NULL,2,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(6,5,'行政人事组','行政人事组主管',NULL,1,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(7,1,'护理部','护理部部长',NULL,3,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(8,7,'护理组','护理组主管',NULL,1,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(9,1,'销售部','销售部部长',NULL,4,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(10,9,'销售组','销售组主管',NULL,1,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(11,1,'法务部','法务部部长',NULL,5,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(12,11,'法务组','法务组主管',NULL,1,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(13,1,'后勤部','后勤部部长',NULL,6,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(14,13,'后勤组','后勤组主管',NULL,1,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(15,13,'保安组',NULL,NULL,2,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(16,13,'保洁组',NULL,NULL,3,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(17,1,'售后保障部','售后保障部部长',NULL,7,1,'2026-07-10 17:11:47','2026-07-10 17:11:47'),(18,17,'客服组','客服组主管',NULL,1,1,'2026-07-10 17:11:47','2026-07-10 17:11:47');

--
-- Table structure for table `t_device`
--

DROP TABLE IF EXISTS `t_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `status` int DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_no` (`device_no`),
  KEY `idx_device_elder` (`elder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_device` VALUES (1,'DEV001','智能床垫-101','床垫监测',1,NULL,'ONLINE','心率异常','心率105次/分','2026-07-12 08:30:00',NULL,'NORMAL',NULL,1,'2026-07-12 14:57:33'),(2,'DEV002','紧急呼叫器-101','呼叫器',1,NULL,'ONLINE','测试呼叫','功能检测','2026-07-11 20:00:00',NULL,'NORMAL',NULL,1,'2026-07-12 14:57:33'),(3,'DEV003','智能床垫-102','床垫监测',2,NULL,'ONLINE','离床超时','离床超过30分钟','2026-07-12 10:00:00',NULL,'WARNING',NULL,1,'2026-07-12 14:57:33'),(4,'DEV004','活动监测手环-101','手环',1,NULL,'OFFLINE',NULL,NULL,NULL,NULL,'ALERT',NULL,1,'2026-07-12 14:57:33'),(5,'DEV005','智能床垫-103','床垫监测',3,NULL,'ONLINE','翻身提醒','2小时未翻身','2026-07-12 06:00:00',NULL,'NORMAL',NULL,1,'2026-07-12 14:57:33');

--
-- Table structure for table `t_elder`
--

DROP TABLE IF EXISTS `t_elder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_elder` VALUES (1,'ELD202607001','张建国','男',NULL,'1940-03-15',NULL,NULL,'IN',NULL,'轻度失能',NULL,'张小明',NULL,'2026-07-10 15:07:44','2026-07-12 16:35:13'),(2,'ELD202607002','王美丽','女',NULL,'1945-07-20',NULL,NULL,'IN',NULL,'中度失能',NULL,'王小爱',NULL,'2026-07-10 17:04:18','2026-07-12 16:35:13'),(3,'ELD202607003','李玉林','男',NULL,'1950-11-08',NULL,NULL,'IN',NULL,'能力完好',NULL,'李小明',NULL,'2026-07-10 17:04:18','2026-07-12 16:35:13'),(4,'ELD202607004','张大民','男',NULL,'1935-01-25',NULL,NULL,'IN',NULL,'重度失能',NULL,'张小民',NULL,'2026-07-10 17:04:18','2026-07-12 16:35:13'),(5,'ELD202607005','赵芳','女',NULL,'1955-06-10',NULL,NULL,'IN',NULL,'能力完好',NULL,'赵小明',NULL,'2026-07-10 17:04:18','2026-07-12 16:35:13');

--
-- Table structure for table `t_family_elder`
--

DROP TABLE IF EXISTS `t_family_elder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_family_elder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_id` int NOT NULL,
  `elder_id` int NOT NULL,
  `relation` varchar(30) DEFAULT NULL,
  `is_default` int DEFAULT '0',
  `status` int DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_family_elder` (`family_id`,`elder_id`),
  KEY `idx_family_elder_elder` (`elder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_family_elder` VALUES (1,1,1,'子女',1,1,'2026-07-10 15:07:44');

--
-- Table structure for table `t_family_user`
--

DROP TABLE IF EXISTS `t_family_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_family_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `status` int DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_family_account` (`account`),
  KEY `idx_family_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_family_user` VALUES (1,'family001','123456','张小明','13800000001',NULL,1,'2026-07-10 15:07:44','2026-07-10 15:07:44');

--
-- Table structure for table `t_menu`
--

DROP TABLE IF EXISTS `t_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` int DEFAULT NULL,
  `mname` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `visible` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_menu_pid` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_menu` VALUES (1,0,'个人中心',NULL,1,1),(2,1,'个人信息','/UserInfo',1,1),(3,1,'修改密码','/ModifyPwd',2,1),(4,0,'服务管理',NULL,2,1),(5,4,'护理规划','',1,1),(6,5,'护理项目','/NursingItem',1,1),(7,5,'护理计划','/NursingPlain',2,1),(8,5,'护理等级','/NursingLevel',3,1),(9,0,'工作台','/Dashboard',3,1),(10,0,'来访管理','/Visit',4,1),(11,0,'入退管理','/Apply',5,1),(12,0,'在住管理','/Resident',6,1),(13,0,'订单管理','/Order',7,1),(14,0,'财务管理','/Bill',8,1),(15,0,'客户管理','/Customer',9,1),(16,0,'协同工作','/Todo',10,1),(17,0,'智能监测','/Device',11,1),(18,0,'消息中心','/Messages',12,1),(19,0,'权限配置',NULL,13,1),(20,19,'用户信息','/UserManage',1,1),(21,19,'角色管理','/RoleManage',2,1),(22,19,'菜单管理','/MenuManage',3,1),(23,19,'部门管理','/Department',4,1),(24,19,'职位管理','/Post',5,1),(25,0,'AI助手',NULL,12,1),(26,25,'智能问答','/AIChat',1,1);

--
-- Table structure for table `t_message`
--

DROP TABLE IF EXISTS `t_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` text,
  `message_type` varchar(50) DEFAULT NULL,
  `receiver_type` varchar(30) DEFAULT 'USER',
  `receiver_id` int DEFAULT NULL,
  `read_status` int DEFAULT '0',
  `read_time` datetime DEFAULT NULL,
  `enabled` int DEFAULT '1',
  `business_type` varchar(50) DEFAULT NULL,
  `business_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_message_receiver` (`receiver_type`,`receiver_id`),
  KEY `idx_message_business` (`business_type`,`business_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_message` VALUES (1,'【待办事项】您有一个单据待处理','顾廷烨提交了入住申请，请尽快审批。','BUSINESS','USER',1,1,'2026-07-11 09:18:13',1,NULL,NULL,'2026-07-10 17:04:18'),(2,'【已批事项】您发起的单据已通过','您的退住申请已审批通过。','BUSINESS','USER',1,1,'2026-07-11 11:06:09',1,NULL,NULL,'2026-07-10 17:04:18'),(3,'【数据异常】老人数据异常，请及时处理','王美丽老人的健康数据出现异常波动。','ALERT','USER',1,1,'2026-07-11 11:03:33',1,NULL,NULL,'2026-07-10 17:04:18'),(4,'【数据异常】设备数据异常，请及时处理','BED-A303-1床位设备离线超过2小时。','ALERT','USER',1,1,'2026-07-11 11:06:09',1,NULL,NULL,'2026-07-10 17:04:18'),(5,'系统升级通知','系统将于本周六凌晨2:00-4:00进行升级维护。','SYSTEM','USER',1,1,NULL,1,NULL,NULL,'2026-07-10 17:04:18'),(6,'【待办事项】预约来访提醒','今日下午14:00有家属预约参观。','BUSINESS','USER',1,1,NULL,1,NULL,NULL,'2026-07-10 17:04:18'),(7,'新员工入职通知','财务部新员工林噙霜已入职。','SYSTEM','USER',1,1,'2026-07-11 09:18:26',1,NULL,NULL,'2026-07-10 17:04:18'),(8,'【协同通知】入住审批已通过','顾廷烨提交的老人赵芳入住申请已由盛明兰审批通过，请尽快安排入住配置。','BUSINESS','USER',1,0,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(9,'【缴费提醒】8月月度账单已生成','张建国的8月月度账单已生成，金额3000.00元，请通知家属及时缴费。','BUSINESS','USER',1,0,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(10,'【预约通知】今日下午有家属参观','家属张小明预约今日14:00参观养老院，请前台接待做好准备。','BUSINESS','USER',1,0,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(11,'【设备告警】A302房间烟雾报警','A302房间烟雾报警器于今日08:35触发，请立即派人检查。','ALERT','USER',1,0,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(12,'【设备告警】老人王美丽离床超时','王美丽老人夜间离床超过30分钟未返回，请护理员前往查看。','ALERT','USER',1,1,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(13,'【系统公告】国庆节值班安排通知','根据院办通知，国庆节期间各部门需安排值班人员，值班表请于9月28日前提交行政部。','SYSTEM','USER',1,0,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(14,'【培训通知】新员工入职培训','下周三下午2点在3楼会议室举行新员工入职培训，请各部门新入职员工准时参加。','SYSTEM','USER',1,1,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(15,'【协同通知】退住申请已提交','护理员为老人张大民提交了退住申请，等待副院长审批。','BUSINESS','USER',1,0,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(16,'【费用通知】预存款余额不足','老人李玉林的预存款余额已不足下月扣费，请通知家属及时充值。','BUSINESS','USER',1,0,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(17,'【设备告警】B201房间门磁异常','B201房间门磁报警器于今日10:12触发，可能为老人未经授权外出。','ALERT','USER',1,0,NULL,1,NULL,NULL,'2026-07-11 11:08:55'),(18,'【预约通知】今日有家属参观','家属李女士预约今日14:00参观养老院，请做好准备。','BUSINESS','USER',12,0,NULL,1,NULL,NULL,'2026-07-11 11:43:19'),(19,'【系统通知】欢迎使用中州养老系统','前台小王，欢迎加入中州养老院！请及时完善个人资料。','SYSTEM','USER',12,0,NULL,1,NULL,NULL,'2026-07-11 11:43:19'),(20,'【待办】请确认明日预约安排','明日共有3组家属预约参观，请提前安排好接待工作。','BUSINESS','USER',12,0,NULL,1,NULL,NULL,'2026-07-11 11:43:19');

--
-- Table structure for table `t_nursimg_item`
--

DROP TABLE IF EXISTS `t_nursimg_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_nursimg_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `itemname` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `sort` varchar(255) DEFAULT NULL,
  `islock` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_nursimg_item` VALUES (2,'助浴',15.00,'日','1','0','由护理人员协助长者沐浴，做好防滑与保暖照护。'),(3,'肩颈按摩60分钟',98.90,'日','2','0','舒缓肩颈疲劳，服务前确认长者身体情况。'),(4,'健康检查',80.00,'月','3','0','定期为长者进行身体健康检查与评估。');

--
-- Table structure for table `t_nursing_level`
--

DROP TABLE IF EXISTS `t_nursing_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_nursing_level` (
  `id` int NOT NULL AUTO_INCREMENT,
  `level_name` varchar(100) DEFAULT NULL COMMENT '等级名称',
  `islock` varchar(30) DEFAULT '启用' COMMENT '启用/禁用',
  `description` varchar(500) DEFAULT NULL COMMENT '等级描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_nursing_level` VALUES (1,'特级护理','启用','24小时专人照护，适用于完全失能老人','2026-07-12 15:51:00'),(2,'一级护理','启用','每日定时照护，适用于重度失能老人','2026-07-12 15:51:00'),(3,'二级护理','启用','定期巡视照护，适用于中度失能老人','2026-07-12 15:51:00'),(4,'三级护理','启用','基础生活照料，适用于轻度失能老人','2026-07-12 15:51:00');

--
-- Table structure for table `t_nursing_plain`
--

DROP TABLE IF EXISTS `t_nursing_plain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_nursing_plain` (
  `id` int NOT NULL AUTO_INCREMENT,
  `plainname` varchar(255) DEFAULT NULL,
  `level_id` int DEFAULT NULL,
  `level_name` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `createuser` varchar(255) DEFAULT NULL,
  `islock` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_nursing_plain` VALUES (1,'A级护理计划',1,'特级护理','2026-07-09 15:12:55','马云','启动'),(2,'B级护理计划',2,'一级护理','2026-07-09 15:15:59','马云','启动'),(3,'B级护理计划',2,'一级护理','2026-07-09 15:18:19','马云','启动'),(4,'D护理计划',4,'三级护理','2026-07-09 15:19:57','马云','启动');

--
-- Table structure for table `t_nursing_task`
--

DROP TABLE IF EXISTS `t_nursing_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_nursing_task` VALUES (1,'TSK202607001',1,2,NULL,1,'2026-07-12 14:00:00',NULL,NULL,'PENDING','2026-07-12 14:57:33'),(2,'TSK202607002',1,3,NULL,1,'2026-07-12 08:00:00',NULL,NULL,'DONE','2026-07-12 08:57:33'),(3,'TSK202607003',2,2,NULL,2,'2026-07-12 16:00:00',NULL,NULL,'PENDING','2026-07-12 14:57:33'),(4,'TSK202607004',2,4,NULL,2,'2026-07-12 09:00:00',NULL,NULL,'DONE','2026-07-12 09:57:33'),(5,'TSK202607005',3,2,NULL,1,'2026-07-13 10:00:00',NULL,NULL,'PENDING','2026-07-12 14:57:33'),(6,'TSK202607006',4,3,NULL,2,'2026-07-13 14:00:00',NULL,NULL,'PENDING','2026-07-12 14:57:33');

--
-- Table structure for table `t_order`
--

DROP TABLE IF EXISTS `t_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL,
  `family_id` int DEFAULT NULL,
  `elder_id` int DEFAULT NULL,
  `service_item_id` int DEFAULT NULL,
  `service_name` varchar(100) DEFAULT NULL,
  `quantity` int DEFAULT '1',
  `total_amount` decimal(10,2) DEFAULT '0.00',
  `pay_amount` decimal(10,2) DEFAULT '0.00',
  `pay_status` varchar(30) DEFAULT 'UNPAID',
  `order_status` varchar(30) DEFAULT 'CREATED',
  `refund_amount` decimal(10,2) DEFAULT '0.00',
  `refund_reason` varchar(500) DEFAULT NULL,
  `service_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_order_family` (`family_id`),
  KEY `idx_order_elder` (`elder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_order` VALUES (1,'DD20260710001',1,1,NULL,'肩颈按摩60分钟',1,75.00,75.00,'PAID','FINISHED',0.00,NULL,NULL,NULL,'2026-07-10 17:04:18','2026-07-10 17:04:18'),(2,'DD20260710002',1,1,NULL,'康复理疗',1,120.00,120.00,'PAID','REFUNDED',120.00,'不需要此项服务了',NULL,NULL,'2026-07-10 17:04:18','2026-07-12 09:22:21'),(3,'DD20260710003',1,1,NULL,'足部护理',1,60.00,60.00,'PAID','SERVING',0.00,NULL,NULL,NULL,'2026-07-10 17:04:18','2026-07-10 17:04:18'),(4,'DD20260712090614556',1,1,2,'助浴',1,15.00,15.00,'PAID','PAID',0.00,NULL,'2026-07-13 09:00:00','联调测试','2026-07-12 09:06:14','2026-07-12 09:06:14'),(5,'DD20260712001',1,1,2,'康复运行',1,10.00,10.00,'PAID','FINISHED',0.00,NULL,NULL,NULL,'2026-07-12 14:55:34','2026-07-12 14:55:34'),(6,'DD20260712002',1,1,3,'助餐',2,20.00,20.00,'PAID','PAID',0.00,NULL,NULL,NULL,'2026-07-12 14:55:34','2026-07-12 14:55:34'),(7,'DD20260712003',1,1,4,'理发',1,10.00,0.00,'UNPAID','CREATED',0.00,NULL,NULL,NULL,'2026-07-12 14:55:34','2026-07-12 14:55:34'),(8,'DD20260712004',1,2,2,'康复运行',3,30.00,30.00,'PAID','SERVING',0.00,NULL,NULL,NULL,'2026-07-12 14:55:34','2026-07-12 14:55:34'),(9,'DD20260712005',1,2,3,'助餐',1,10.00,10.00,'PAID','REFUNDED',0.00,NULL,NULL,NULL,'2026-07-11 14:55:34','2026-07-12 14:55:34'),(15,'DD20260712153530169',1,1,2,'助浴',1,15.00,15.00,'PAID','PAID',0.00,NULL,'2026-07-13 09:00:00',NULL,'2026-07-12 15:35:30','2026-07-12 15:35:33'),(16,'ORD202605001',1,1,3,'助餐',1,10.00,10.00,'PAID','FINISHED',0.00,NULL,NULL,NULL,'2026-05-15 10:00:00','2026-05-15 10:00:00'),(17,'ORD202605002',1,1,2,'康复运行',2,20.00,20.00,'PAID','FINISHED',0.00,NULL,NULL,NULL,'2026-05-20 14:00:00','2026-05-20 14:00:00'),(18,'ORD202605003',1,2,4,'理发',1,10.00,10.00,'PAID','FINISHED',0.00,NULL,NULL,NULL,'2026-05-25 09:00:00','2026-05-25 09:00:00'),(19,'ORD202606001',1,1,4,'理发',1,10.00,10.00,'PAID','FINISHED',0.00,NULL,NULL,NULL,'2026-06-10 11:00:00','2026-06-10 11:00:00'),(20,'ORD202606002',1,1,3,'助餐',3,30.00,30.00,'PAID','FINISHED',0.00,NULL,NULL,NULL,'2026-06-18 16:00:00','2026-06-18 16:00:00'),(21,'ORD202606003',1,2,2,'康复运行',2,20.00,20.00,'PAID','FINISHED',0.00,NULL,NULL,NULL,'2026-06-22 08:00:00','2026-06-22 08:00:00'),(22,'ORD202606004',1,3,4,'理发',1,10.00,0.00,'UNPAID','CANCELED',0.00,NULL,NULL,NULL,'2026-06-28 13:00:00','2026-06-28 13:00:00');

--
-- Table structure for table `t_plain_item`
--

DROP TABLE IF EXISTS `t_plain_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  CONSTRAINT `fk_plain_item_item` FOREIGN KEY (`item_id`) REFERENCES `t_nursimg_item` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_plain_item_plain` FOREIGN KEY (`plain_id`) REFERENCES `t_nursing_plain` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_plain_item` VALUES (1,2,3,'助浴','2026-07-09T07:12:15.000Z','天',1),(2,2,4,'助浴','2026-07-09T07:19:12.000Z','天',1),(3,3,4,'肩颈按摩60分钟','2026-07-09T07:19:12.000Z','天',1),(4,4,4,'健康检查','2026-07-09T07:19:12.000Z','月',1);

--
-- Table structure for table `t_post`
--

DROP TABLE IF EXISTS `t_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_post` (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_name` varchar(100) NOT NULL,
  `post_code` varchar(50) DEFAULT NULL,
  `sort` int DEFAULT '1',
  `status` int DEFAULT '1',
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_code` (`post_code`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_post` VALUES (1,'院长','dean',1,1,NULL,'2026-07-10 15:07:44','2026-07-10 17:11:47'),(2,'副院长','vice_dean',2,1,NULL,'2026-07-10 15:07:44','2026-07-10 17:11:47'),(3,'财务部部长','finance_minister',3,1,NULL,'2026-07-10 15:07:44','2026-07-10 17:11:47'),(4,'行政部部长','admin_minister',4,1,NULL,'2026-07-10 15:07:44','2026-07-10 17:11:47'),(5,'护理部部长','nurse_minister',5,1,'财务部会计','2026-07-10 17:04:18','2026-07-10 17:11:47'),(6,'销售部部长','sales_minister',6,1,'财务部出纳','2026-07-10 17:04:18','2026-07-10 17:11:47'),(7,'法务部部长','legal_minister',7,1,'财务部结算','2026-07-10 17:04:18','2026-07-10 17:11:47'),(8,'会计组主管','accounting_lead',8,1,'财务部收费','2026-07-10 17:04:18','2026-07-10 17:11:47'),(9,'会计','accountant',9,1,'护理部主管','2026-07-10 17:04:18','2026-07-10 17:11:47'),(10,'结算组主管','settlement_lead',10,1,'结算组负责人','2026-07-10 17:11:47','2026-07-10 17:11:47'),(11,'出纳','cashier',11,1,'负责现金收付','2026-07-10 17:11:47','2026-07-10 17:11:47'),(12,'结算员','settler',12,1,'负责费用结算','2026-07-10 17:11:47','2026-07-10 17:11:47'),(13,'收费员','collector',13,1,'负责费用收纳','2026-07-10 17:11:47','2026-07-10 17:11:47'),(14,'行政人事组主管','hr_lead',14,1,'行政人事负责人','2026-07-10 17:11:47','2026-07-10 17:11:47'),(15,'行政专员','admin_specialist',15,1,'系统基础信息维护','2026-07-10 17:11:47','2026-07-10 17:11:47'),(16,'人事专员','hr_specialist',16,1,'员工账号管理','2026-07-10 17:11:47','2026-07-10 17:11:47'),(17,'前台接待','receptionist',17,1,'来访接待和预约登记','2026-07-10 17:11:47','2026-07-10 17:11:47'),(18,'后勤部部长','logistics_minister',18,1,'后勤管理负责人','2026-07-10 17:11:47','2026-07-10 17:11:47'),(19,'后勤组主管','logistics_lead',19,1,'后勤组负责人','2026-07-10 17:11:47','2026-07-10 17:11:47'),(20,'护理组主管','nurse_lead',20,1,'护理组负责人','2026-07-10 17:11:47','2026-07-10 17:11:47'),(21,'护理员','nurse',21,1,'负责老人日常护理','2026-07-10 17:11:47','2026-07-10 17:11:47'),(22,'销售组主管','sales_lead',22,1,'销售组负责人','2026-07-10 17:11:47','2026-07-10 17:11:47'),(23,'养老顾问','elder_advisor',23,1,'客户开发和入住办理','2026-07-10 17:11:47','2026-07-10 17:11:47'),(24,'法务组主管','legal_lead',24,1,'法务组负责人','2026-07-10 17:11:47','2026-07-10 17:11:47'),(25,'法务专员','legal_specialist',25,1,'合同管理和审核','2026-07-10 17:11:47','2026-07-10 17:11:47');

--
-- Table structure for table `t_role`
--

DROP TABLE IF EXISTS `t_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) NOT NULL,
  `role_code` varchar(50) NOT NULL,
  `menu_ids` text,
  `data_scope` varchar(30) DEFAULT 'ALL',
  `status` int DEFAULT '1',
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_role` VALUES (1,'院长','DEAN','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24','ALL',1,'默认管理员角色','2026-07-10 15:07:44','2026-07-10 17:23:17'),(2,'副院长','VICE_DEAN','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24','ALL',1,'护理任务执行','2026-07-10 15:07:44','2026-07-11 11:26:09'),(3,'人事专员','HR','2,13,14,18,1','ALL',1,'订单和账单管理','2026-07-10 15:07:44','2026-07-12 08:50:04'),(4,'结算员','SETTLER','1,2,3,9,10,11,12,13,14,16,17,18,19,20,21,22,23,24','ALL',1,'床位+护理+设备+权限','2026-07-10 17:11:47','2026-07-10 17:23:08'),(5,'前台接待','RECEPTION','1,2,3,9,10,11,12,13,15,16,17,18','DEPT',1,'预约+来访','2026-07-10 17:11:47','2026-07-11 23:41:40'),(6,'护理员','NURSE_ROLE','1,2,3,9,12,16,17,18','DEPT',1,'护理任务+老人','2026-07-10 17:11:47','2026-07-10 17:23:08'),(7,'养老顾问','ADVISOR','1,2,3,9,11,12,15,16,18','DEPT',1,'客户+合同+待办','2026-07-10 17:11:47','2026-07-10 17:23:08'),(8,'法务专员','LEGAL','1,2,3,9,12,18','DEPT',1,'合同管理','2026-07-10 17:11:47','2026-07-10 17:11:47'),(9,'客服专员','CS','1,2,3,9,13,14,18','ALL',1,'订单+退款','2026-07-10 17:11:47','2026-07-10 17:11:47');

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_user` VALUES (1,'123','We123456','ziyao','my@173.com',NULL,NULL,'职员','12012012022','男',0,'https://zzyl-ziyaowei.oss-cn-chengdu.aliyuncs.com/42e614bf-aca0-4e4e-87a4-0d84fbefb693.jpg',1,1,1,'2026-07-10 15:07:44','2026-07-12 08:41:35'),(2,'gutingye@qq.com','We123456','顾廷烨','gutingye@qq.com',NULL,NULL,NULL,'13898998888','男',0,NULL,1,5,4,'2026-07-10 17:04:18','2026-07-11 22:18:41'),(3,'gutingwei','123456','顾廷伟','gutingwei@qq.com',NULL,NULL,NULL,'13898998881','男',0,NULL,2,6,5,'2026-07-10 17:04:18','2026-07-10 17:04:18'),(4,'shengminglan','We123456','盛明兰','shengminglan@qq.com',NULL,NULL,NULL,'13898998882','女',0,NULL,5,1,5,'2026-07-10 17:04:18','2026-07-11 23:08:13'),(5,'shengmolan','123456','盛墨兰','shengmolan@qq.com',NULL,NULL,NULL,'13898998883','女',0,NULL,3,3,6,'2026-07-10 17:04:18','2026-07-10 17:04:18'),(6,'shengrulan','123456','盛如兰','shengrulan@qq.com',NULL,NULL,NULL,'13898998884','女',0,NULL,3,3,6,'2026-07-10 17:04:18','2026-07-10 17:04:18'),(7,'guyankai','123456','顾偃开','guyankai@qq.com',NULL,NULL,NULL,'13898998885','男',0,NULL,6,1,4,'2026-07-10 17:04:18','2026-07-10 17:04:18'),(8,'shenghong','123456','盛纮','shenghong@qq.com',NULL,NULL,NULL,'13898998886','男',0,NULL,7,7,6,'2026-07-10 17:04:18','2026-07-10 17:04:18'),(9,'wangruofu','123456','王若弗','wangruofu@qq.com',NULL,NULL,NULL,'13898998887','女',0,NULL,10,1,5,'2026-07-10 17:04:18','2026-07-10 17:04:18'),(10,'linqinshuang','123456','林噙霜','linqinshuang@qq.com',NULL,NULL,NULL,'13898998889','女',1,NULL,11,1,6,'2026-07-10 17:04:18','2026-07-11 11:08:13'),(11,'ziyaowei','We123456','前台小王','315@qq.com',NULL,NULL,NULL,'12345678910','男',0,'',1,17,5,'2026-07-11 11:27:31','2026-07-11 23:42:13'),(12,'reception@zzyly.com','We123456','前台小王','reception@zzyly.com',NULL,NULL,NULL,'13800001111','女',0,NULL,5,17,5,'2026-07-11 11:36:03','2026-07-11 11:51:24');

--
-- Table structure for table `t_visit`
--

DROP TABLE IF EXISTS `t_visit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `t_visit` VALUES (1,'YY20260710001','RESERVATION','张小明','13800000001','VISIT',1,'张建国','2026-07-12 14:00:00',NULL,NULL,'PENDING',NULL,'2026-07-10 17:04:18','2026-07-10 17:04:18'),(2,'YY20260710002','RESERVATION','王小爱','16600008888','INTERVIEW',2,'王美丽','2026-07-11 10:00:00',NULL,NULL,'PENDING',NULL,'2026-07-10 17:04:18','2026-07-10 17:04:18');
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;