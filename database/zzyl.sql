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

SET FOREIGN_KEY_CHECKS = 1;
