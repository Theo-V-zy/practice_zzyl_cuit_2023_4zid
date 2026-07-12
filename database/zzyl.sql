/*
 Navicat Premium Dump SQL

 Source Server         : hello
 Source Server Type    : MySQL
 Source Server Version : 80021 (8.0.21)
 Source Host           : localhost:3306
 Source Schema         : zzyl

 Target Server Type    : MySQL
 Target Server Version : 80021 (8.0.21)
 File Encoding         : 65001

 Date: 09/07/2026 16:41:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `zzyl` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `zzyl`;

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` int NULL DEFAULT NULL,
  `mname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NULL DEFAULT NULL,
  `visible` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES (1, 0, '个人中心', NULL, 1, 1);
INSERT INTO `t_menu` VALUES (2, 1, '个人信息', '/UserInfo', 1, 1);
INSERT INTO `t_menu` VALUES (3, 1, '修改密码', '/ModifyPwd', 1, 1);
INSERT INTO `t_menu` VALUES (4, 0, '服务管理', NULL, 1, 1);
INSERT INTO `t_menu` VALUES (5, 4, '护理规划', '', 1, 1);
INSERT INTO `t_menu` VALUES (6, 5, '护理项目', '/NursingItem', 1, 1);
INSERT INTO `t_menu` VALUES (7, 5, '护理计划', '/NursingPlain', 1, 1);
INSERT INTO `t_menu` VALUES (8, 5, '护理等级', NULL, 1, 1);
INSERT INTO `t_menu` VALUES (9, 0, '客户管理', NULL, 2, 1);
INSERT INTO `t_menu` VALUES (10, 9, '客户信息', '/Customer', 1, 1);
INSERT INTO `t_menu` VALUES (11, 0, '协同工作', NULL, 3, 1);
INSERT INTO `t_menu` VALUES (12, 11, '我的待办', '/Todo', 1, 1);
INSERT INTO `t_menu` VALUES (13, 11, '我的申请', '/Application', 2, 1);
INSERT INTO `t_menu` VALUES (14, 0, '智能监测', NULL, 4, 1);
INSERT INTO `t_menu` VALUES (15, 14, '设备管理', '/Device', 1, 1);
INSERT INTO `t_menu` VALUES (16, 14, '报警数据', '/Alert', 2, 1);
INSERT INTO `t_menu` VALUES (17, 14, '报警规则', '/AlertRule', 3, 1);

-- ----------------------------
-- Table structure for t_nursimg_item
-- ----------------------------
DROP TABLE IF EXISTS `t_nursimg_item`;
CREATE TABLE `t_nursimg_item`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `itemname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `islock` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_nursimg_item
-- ----------------------------
INSERT INTO `t_nursimg_item` VALUES (2, '康复运行', 10.00, '天', '1', '启用', 'https://zzyl-706.oss-cn-chengdu.aliyuncs.com/df28271b-306b-4627-b5f1-672ad4c27689.png', '康复运行');
INSERT INTO `t_nursimg_item` VALUES (3, '助餐', 10.00, '次', '1', '启用', 'https://zzyl-706.oss-cn-chengdu.aliyuncs.com/df28271b-306b-4627-b5f1-672ad4c27689.png', '助餐');
INSERT INTO `t_nursimg_item` VALUES (4, '理发', 10.00, '次', '1', '启用', 'https://zzyl-706.oss-cn-chengdu.aliyuncs.com/df28271b-306b-4627-b5f1-672ad4c27689.png', '理发');

-- ----------------------------
-- Table structure for t_nursing_plain
-- ----------------------------
DROP TABLE IF EXISTS `t_nursing_plain`;
CREATE TABLE `t_nursing_plain`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `plainname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `createtime` datetime NULL DEFAULT NULL,
  `createuser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `islock` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_nursing_plain
-- ----------------------------
INSERT INTO `t_nursing_plain` VALUES (1, 'A级护理计划', '2026-07-09 15:12:55', '马云', '启动');
INSERT INTO `t_nursing_plain` VALUES (2, 'B级护理计划', '2026-07-09 15:15:59', '马云', '启动');
INSERT INTO `t_nursing_plain` VALUES (3, 'B级护理计划', '2026-07-09 15:18:19', '马云', '启动');
INSERT INTO `t_nursing_plain` VALUES (4, 'D护理计划', '2026-07-09 15:19:57', '马云', '启动');

-- ----------------------------
-- Table structure for t_plain_item
-- ----------------------------
DROP TABLE IF EXISTS `t_plain_item`;
CREATE TABLE `t_plain_item`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `item_id` int NOT NULL,
  `plain_id` int NOT NULL,
  `itemname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `hlsj` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `hlzq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `hlpc` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_item_id`(`item_id` ASC) USING BTREE,
  INDEX `fk_plain_id`(`plain_id` ASC) USING BTREE,
  CONSTRAINT `fk_item_id` FOREIGN KEY (`item_id`) REFERENCES `t_nursimg_item` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_plain_id` FOREIGN KEY (`plain_id`) REFERENCES `t_nursing_plain` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_plain_item
-- ----------------------------
INSERT INTO `t_plain_item` VALUES (1, 2, 3, '康复运行', '2026-07-09T07:12:15.000Z', '天', 2);
INSERT INTO `t_plain_item` VALUES (2, 2, 4, '康复运行', '2026-07-09T07:19:12.000Z', '天', 1);
INSERT INTO `t_plain_item` VALUES (3, 3, 4, '助餐', '2026-07-09T07:19:12.000Z', '周', 2);
INSERT INTO `t_plain_item` VALUES (4, 4, 4, '理发', '2026-07-09T07:19:12.000Z', '月', 3);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `job` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `islock` int NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '20260023', '222222', '马云', 'my@173.com', '研发部', '程序员', '职员', '12012012022', '女', 0, 'https://zzyl-706.oss-cn-chengdu.aliyuncs.com/a1d4af5d-d28f-4920-9e02-24ecfc48304a.png');

-- ----------------------------
-- Table structure for t_customer (组员C: 客户管理)
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `intention_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '意向等级(A/B/C/D)',
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户来源(线上/线下/转介绍/其他)',
  `follow_up_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '跟进内容',
  `next_follow_up_time` datetime NULL DEFAULT NULL COMMENT '下次跟进时间',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `islock` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '启用' COMMENT '状态(启用/禁用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_customer
-- ----------------------------
INSERT INTO `t_customer` VALUES (1, '张大爷', '13800138001', 'A', '线上咨询', '老人有入住意向，家属已来院参观，对环境和护理服务满意。', '2026-07-20 10:00:00', '马云', '2026-07-10 09:00:00', '2026-07-10 09:00:00', '启用');
INSERT INTO `t_customer` VALUES (2, '李奶奶', '13800138002', 'B', '转介绍', '通过张大爷介绍，电话初步沟通。', '2026-07-22 14:00:00', '马云', '2026-07-10 10:00:00', '2026-07-10 10:00:00', '启用');
INSERT INTO `t_customer` VALUES (3, '王阿姨', '13800138003', 'C', '线下活动', '参加社区养老讲座后咨询，对价格较敏感。', '2026-07-25 09:30:00', '马云', '2026-07-11 08:00:00', '2026-07-11 08:00:00', '启用');

-- ----------------------------
-- Table structure for t_elder (组员C: 老人信息)
-- ----------------------------
DROP TABLE IF EXISTS `t_elder`;
CREATE TABLE `t_elder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '老人姓名',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `bed_id` int NULL DEFAULT NULL COMMENT '床位ID',
  `health_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '健康信息',
  `check_in_time` datetime NULL DEFAULT NULL COMMENT '入住时间',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '在住' COMMENT '状态(在住/已退住/请假)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_elder
-- ----------------------------
INSERT INTO `t_elder` VALUES (1, '张建国', 78, '男', '310101194801010001', '13800138001', 1, '高血压、糖尿病，需定时服药。行动自如，饮食正常。', '2026-06-01 10:00:00', '在住', '2026-06-01 10:00:00');
INSERT INTO `t_elder` VALUES (2, '李秀兰', 82, '女', '310101194401010002', '13800138002', 2, '轻度认知障碍，需日常看护。无重大疾病史。', '2026-05-15 09:00:00', '在住', '2026-05-15 09:00:00');

-- ----------------------------
-- Table structure for t_apply (组员C: 协同工作/审批)
-- ----------------------------
DROP TABLE IF EXISTS `t_apply`;
CREATE TABLE `t_apply` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apply_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请类型(入住/退住/请假)',
  `apply_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人',
  `apply_user_id` int NULL DEFAULT NULL COMMENT '申请人ID',
  `elder_id` int NULL DEFAULT NULL COMMENT '老人ID',
  `elder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '老人姓名',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '申请描述',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '待审批' COMMENT '状态(待审批/已通过/已拒绝)',
  `approve_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批人',
  `approve_user_id` int NULL DEFAULT NULL COMMENT '审批人ID',
  `approve_time` datetime NULL DEFAULT NULL COMMENT '审批时间',
  `approve_opinion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '审批意见',
  `apply_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_apply
-- ----------------------------
INSERT INTO `t_apply` VALUES (1, '入住', '马云', 1, 1, '张建国', '老人张建国申请入住我院，已完成体检和评估。', '待审批', NULL, NULL, NULL, NULL, '2026-07-09 10:00:00');
INSERT INTO `t_apply` VALUES (2, '退住', '马云', 1, 2, '李秀兰', '老人李秀兰因家庭原因申请退住。', '已通过', '马云', 1, '2026-07-10 09:00:00', '同意退住，已办理相关手续。', '2026-07-09 14:00:00');
INSERT INTO `t_apply` VALUES (3, '请假', '马云', 1, 1, '张建国', '老人张建国申请请假回家3天。', '待审批', NULL, NULL, NULL, NULL, '2026-07-11 08:00:00');

-- ----------------------------
-- Table structure for t_device (组员C: 智能监测设备)
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` int NOT NULL AUTO_INCREMENT,
  `device_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备类型(手环/床垫/摄像头/紧急按钮/其他)',
  `device_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备编号',
  `elder_id` int NULL DEFAULT NULL COMMENT '绑定老人ID',
  `elder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定老人姓名',
  `bed_id` int NULL DEFAULT NULL COMMENT '绑定床位ID',
  `install_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '安装位置',
  `running_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '正常' COMMENT '运行状态(正常/异常/离线)',
  `alarm_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '无报警' COMMENT '报警状态(无报警/一般报警/紧急报警)',
  `last_heartbeat` datetime NULL DEFAULT NULL COMMENT '最后心跳时间',
  `islock` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '启用' COMMENT '启用状态(启用/禁用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_device
-- ----------------------------
INSERT INTO `t_device` VALUES (1, '智能手环A01', '手环', 'SH-A001', 1, '张建国', 1, 'A栋101室', '正常', '无报警', '2026-07-12 10:00:00', '启用', '2026-07-01 09:00:00');
INSERT INTO `t_device` VALUES (2, '智能床垫B02', '床垫', 'CD-B002', 2, '李秀兰', 2, 'A栋102室', '异常', '一般报警', '2026-07-12 08:30:00', '启用', '2026-07-01 09:00:00');
INSERT INTO `t_device` VALUES (3, '紧急按钮C03', '紧急按钮', 'JZ-C003', NULL, NULL, NULL, 'B栋走廊', '离线', '紧急报警', NULL, '禁用', '2026-07-05 10:00:00');

-- ----------------------------
-- Table structure for t_family_user (组员C: 家属端用户)
-- ----------------------------
DROP TABLE IF EXISTS `t_family_user`;
CREATE TABLE `t_family_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信openid',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '家属姓名',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_family_user
-- ----------------------------
INSERT INTO `t_family_user` VALUES (1, 'wx_openid_001', '张小明', '13900139001', NULL, '2026-06-01 10:00:00');
INSERT INTO `t_family_user` VALUES (2, 'wx_openid_002', '李小红', '13900139002', NULL, '2026-06-15 14:00:00');

-- ----------------------------
-- Table structure for t_family_elder (组员C: 家属-老人绑定关系)
-- ----------------------------
DROP TABLE IF EXISTS `t_family_elder`;
CREATE TABLE `t_family_elder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_user_id` int NOT NULL COMMENT '家属用户ID',
  `elder_id` int NOT NULL COMMENT '老人ID',
  `elder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '老人姓名',
  `relation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关系(子女/配偶/其他)',
  `bind_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定验证码/编号',
  `bind_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '已绑定' COMMENT '状态(已绑定/已解绑)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_family_user_id`(`family_user_id` ASC) USING BTREE,
  INDEX `idx_elder_id`(`elder_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_family_elder
-- ----------------------------
INSERT INTO `t_family_elder` VALUES (1, 1, 1, '张建国', '子女', 'BIND20260001', '2026-06-01 10:00:00', '已绑定');
INSERT INTO `t_family_elder` VALUES (2, 2, 2, '李秀兰', '子女', 'BIND20260002', '2026-06-15 14:00:00', '已绑定');

-- ----------------------------
-- Table structure for t_message (组员C: 消息通知)
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '接收用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容',
  `msg_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '系统通知' COMMENT '消息类型(系统通知/审批提醒/报警通知)',
  `is_read` int NULL DEFAULT 0 COMMENT '是否已读(0未读/1已读)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message
-- ----------------------------
INSERT INTO `t_message` VALUES (1, 1, '审批提醒', '您有一条入住申请待审批，申请人：张建国。', '审批提醒', 0, '2026-07-09 10:00:00');
INSERT INTO `t_message` VALUES (2, 1, '设备报警', '智能床垫B02检测到异常，绑定老人：李秀兰，请及时处理。', '报警通知', 0, '2026-07-12 08:30:00');

SET FOREIGN_KEY_CHECKS = 1;
