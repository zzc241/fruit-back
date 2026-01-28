/*
 Navicat Premium Data Transfer

 Source Server         : zizi
 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : localhost:3306
 Source Schema         : fruit

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001

 Date: 27/01/2026 18:39:05
*/
DROP DATABASE IF EXISTS fruit;
CREATE DATABASE fruit DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fruit;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for area_info
-- ----------------------------
DROP TABLE IF EXISTS `area_info`;
CREATE TABLE `area_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地区id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地区名称',
  `size` int NULL DEFAULT 0 COMMENT '地区面积/大小',
  `type_id` bigint NULL DEFAULT NULL COMMENT '类型关联ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述',
  `emp_id` bigint NULL DEFAULT NULL COMMENT '关联员工ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '地区信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of area_info
-- ----------------------------
INSERT INTO `area_info` VALUES (2, 'zzc', 100, 1, '地区测试', 2014518886322917378, '2026-01-22 14:12:23', '2026-01-23 10:02:38', 1, 1);
INSERT INTO `area_info` VALUES (4, 'test2', 666, 1, 'des:测试', 2014518886322917378, '2026-01-24 14:14:43', '2026-01-24 14:14:43', 2, 2);
INSERT INTO `area_info` VALUES (5, 'test2', 888, 1, 'test2', 2014518886322917378, '2026-01-24 14:16:11', '2026-01-24 14:16:11', 2, 2);

-- ----------------------------
-- Table structure for camera
-- ----------------------------
DROP TABLE IF EXISTS `camera`;
CREATE TABLE `camera`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '摄像头id，唯一id',
  `area_id` bigint NOT NULL COMMENT '区id，标识摄像头属于哪一块',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '摄像头URL地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建的时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改的时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人员的id',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人员的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '摄像头信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of camera
-- ----------------------------
INSERT INTO `camera` VALUES (1, 1, NULL, '2026-01-23 09:31:55', '2026-01-23 09:31:55', 1, 1);
INSERT INTO `camera` VALUES (2, 111, NULL, '2026-01-23 09:32:05', '2026-01-23 09:32:05', 1, 1);

-- ----------------------------
-- Table structure for emp_group
-- ----------------------------
DROP TABLE IF EXISTS `emp_group`;
CREATE TABLE `emp_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` bigint NOT NULL COMMENT '分组ID',
  `emp_id` bigint NOT NULL COMMENT '员工ID',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容/备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_finish` int NULL DEFAULT NULL,
  `info_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of emp_group
-- ----------------------------
INSERT INTO `emp_group` VALUES (1, 1, 1, 'test', '1', '2026-01-27 09:10:37', '2026-01-27 09:15:38', '1', 0, NULL);

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint NOT NULL COMMENT '员工id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `status` tinyint NULL DEFAULT 1 COMMENT '员工状态(可用：1，禁用：0)',
  `head_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `is_admin` tinyint NULL DEFAULT 0 COMMENT '1:超级管理员 ， 2 ： 果园管理员 ， 3 ： 农户 ， 4 ： 消费者',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信开放ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建者',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, 'zzc', 'zzc', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', '1', NULL, 1, 'C:\\Users\\27141\\Pictures\\Screenshots\\屏幕截图 2024-08-09 013017.png', 1, '1', '2026-01-22 10:23:06', '2026-01-27 14:58:37', 1, NULL);
INSERT INTO `employee` VALUES (2, 'zzz', 'zzz', 'e10adc3949ba59abbe56e057f20f883e', '12345678900', '1', NULL, 1, '1', 1, '1', '2026-01-22 10:23:06', '2026-01-25 18:52:21', 1, NULL);
INSERT INTO `employee` VALUES (2014221904027000834, '222', '222', 'e10adc3949ba59abbe56e057f20f883e', '1', '1', NULL, 1, NULL, 2, NULL, '2026-01-22 14:21:39', '2026-01-22 14:21:39', 1, 1);
INSERT INTO `employee` VALUES (2014518886322917378, 'yqfzr', '园区负责人', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', '1', NULL, 1, NULL, 0, NULL, '2026-01-23 10:01:45', '2026-01-23 10:01:45', 1, 1);
INSERT INTO `employee` VALUES (2014518995668422658, 'czry', '采摘人员', 'e10adc3949ba59abbe56e057f20f883e', '15012341234', '1', NULL, 1, NULL, 2, NULL, '2026-01-23 10:02:11', '2026-01-23 10:02:11', 1, 1);

-- ----------------------------
-- Table structure for fruit_grow
-- ----------------------------
DROP TABLE IF EXISTS `fruit_grow`;
CREATE TABLE `fruit_grow`  (
  `tree_id` bigint NOT NULL COMMENT '果树ID（关联果树表）',
  `first_term` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '第一期生长情况/描述',
  `second_term` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '第二期生长情况/描述',
  `third_term` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '第三期生长情况/描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tree_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '果树生长过程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fruit_grow
-- ----------------------------
INSERT INTO `fruit_grow` VALUES (2014219934159544322, NULL, NULL, NULL, '2026-01-22 14:13:49', '2026-01-22 14:13:49');
INSERT INTO `fruit_grow` VALUES (2014588807594266625, NULL, NULL, NULL, '2026-01-23 14:39:32', '2026-01-23 14:39:32');
INSERT INTO `fruit_grow` VALUES (2014900706571603970, NULL, NULL, NULL, '2026-01-24 11:18:55', '2026-01-24 11:18:55');

-- ----------------------------
-- Table structure for fruit_tree
-- ----------------------------
DROP TABLE IF EXISTS `fruit_tree`;
CREATE TABLE `fruit_tree`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '果树ID',
  `type` bigint NOT NULL COMMENT '果树类型ID',
  `count_num` int NULL DEFAULT 0 COMMENT '总数量',
  `ripe_num` int NULL DEFAULT 0 COMMENT '成熟数量',
  `img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '果树图片',
  `area_id` bigint NOT NULL COMMENT '区域ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2014900706412220419 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '果树信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fruit_tree
-- ----------------------------
INSERT INTO `fruit_tree` VALUES (1, 1, 0, 0, NULL, 1, '2026-01-22 13:56:49', '2026-01-22 13:56:49');
INSERT INTO `fruit_tree` VALUES (2014219934159544322, 1, 0, 0, NULL, 2, '2026-01-22 14:13:49', '2026-01-22 14:13:49');
INSERT INTO `fruit_tree` VALUES (2014588807535546369, 1, 0, 0, NULL, 2, '2026-01-23 14:39:32', '2026-01-23 14:39:32');

-- ----------------------------
-- Table structure for fruit_type
-- ----------------------------
DROP TABLE IF EXISTS `fruit_type`;
CREATE TABLE `fruit_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '水果类型ID',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '水果类型名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `type`(`type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水果类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fruit_type
-- ----------------------------
INSERT INTO `fruit_type` VALUES (1, 'testType', '2026-01-22 14:12:15', '2026-01-23 13:56:01');

-- ----------------------------
-- Table structure for identify_info
-- ----------------------------
DROP TABLE IF EXISTS `identify_info`;
CREATE TABLE `identify_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '识别信息ID',
  `fruit_num` int NOT NULL DEFAULT 0 COMMENT '果蔬总数量',
  `fruit_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '水果名称',
  `img_count` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理后水果定位图片URL',
  `img_ripe` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '成熟水果识别图片URL',
  `ripe_num` int NOT NULL DEFAULT 0 COMMENT '成熟水果数量',
  `tree_id` bigint NOT NULL COMMENT '果树ID',
  `tree_ripe_percent` int NULL DEFAULT NULL COMMENT '果树成熟百分比（0-100）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '识别信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of identify_info
-- ----------------------------
INSERT INTO `identify_info` VALUES (1, 100, 'fruitName', '1', '1', 0, 1, NULL, '2026-01-22 13:52:03', NULL, '2026-01-22 13:53:12', NULL);

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint NOT NULL COMMENT '新闻ID（雪花算法）',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新闻标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '新闻内容',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原文链接',
  `img_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新闻图片URL',
  `agreement` int NULL DEFAULT 0 COMMENT '点赞/赞同数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '新闻资讯表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
-- ----------------------------

-- ----------------------------
-- Table structure for news_img
-- ----------------------------
DROP TABLE IF EXISTS `news_img`;
CREATE TABLE `news_img`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `news_id` bigint NOT NULL COMMENT '新闻ID',
  `img_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `img_order` int NULL DEFAULT 0 COMMENT '图片排序（0最小最前）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '新闻图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news_img
-- ----------------------------

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '操作日志',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作者ID',
  `operation_content` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作内容（登录\\修改）',
  `operation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operation_ip` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作者IP',
  `operation_result` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作返回结果',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_log
-- ----------------------------
INSERT INTO `operation_log` VALUES (8, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@3fb3d600])', '2026-01-24 13:59:24', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-24T13:59:24.682732700\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (9, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@1b2ce845])', '2026-01-24 14:09:10', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-24T14:09:11.440194900\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (10, 2, 'com.jhc.fruit.controller.AreaController.delete([3])', '2026-01-24 14:13:51', '127.0.0.1', '{\"code\":1,\"data\":[],\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (11, 2, 'com.jhc.fruit.controller.AreaController.save([AreaVo(id=0, name=test2, size=666, type=testType, createTime=null, updateTime=null, description=des:测试, createUser=null, updateUser=null, empId=2014518886322917378)])', '2026-01-24 14:14:43', '127.0.0.1', '{\"code\":1,\"data\":\"新增地区成功\",\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (12, 2, 'com.jhc.fruit.controller.AreaController.save([AreaVo(id=0, name=test2, size=888, type=testTyoe, createTime=null, updateTime=null, description=test2, createUser=null, updateUser=null, empId=2014518886322917378)])', '2026-01-24 14:15:53', '127.0.0.1', '{\"code\":0,\"map\":{},\"msg\":\"找不到当前的种类\"}');
INSERT INTO `operation_log` VALUES (13, 2, 'com.jhc.fruit.controller.AreaController.save([AreaVo(id=0, name=test2, size=888, type=testType, createTime=null, updateTime=null, description=test2, createUser=null, updateUser=null, empId=2014518886322917378)])', '2026-01-24 14:16:11', '127.0.0.1', '{\"code\":1,\"data\":\"新增地区成功\",\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (14, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@b8b2846])', '2026-01-24 14:52:03', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-24T14:52:04.314874600\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (15, 2, 'com.jhc.fruit.controller.FruitTreeController.delete([2014900706412220418])', '2026-01-24 14:52:14', '127.0.0.1', '{\"code\":1,\"data\":\"删除成功\",\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (16, 2, 'com.jhc.fruit.controller.EmployeeController.logout([org.apache.catalina.connector.RequestFacade@b8b2846])', '2026-01-24 14:53:20', '127.0.0.1', '{\"code\":1,\"data\":\"退出成功\",\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (17, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@2e8a9e46])', '2026-01-24 14:57:21', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-24T14:57:21.636347700\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (18, 2, 'com.jhc.fruit.controller.FruitTreeController.delete([2014900706412220418])', '2026-01-24 14:57:37', '127.0.0.1', '{\"code\":1,\"data\":\"删除成功\",\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (19, 1, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzc, password=123456}, org.apache.catalina.connector.RequestFacade@76a4b710])', '2026-01-25 09:20:03', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T09:20:03.563\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (20, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@36d524f4])', '2026-01-25 09:33:08', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T09:33:08.431827600\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (21, 1, 'com.jhc.fruit.controller.EmployeeController.delete([2015233412731146242])', '2026-01-25 09:33:15', '127.0.0.1', '{\"code\":1,\"data\":\"删除成功\",\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (22, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@1c93d422])', '2026-01-25 09:36:56', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T09:36:55.593979700\",\"updateUser\":2,\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (23, 2, 'com.jhc.fruit.controller.EmployeeController.logout([org.apache.catalina.connector.RequestFacade@36d524f4])', '2026-01-25 09:48:24', '127.0.0.1', '{\"code\":1,\"data\":\"退出成功\",\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (24, 1, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzc, password=123456}, org.apache.catalina.connector.RequestFacade@36ab042d])', '2026-01-25 10:04:36', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T10:04:36.107\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (25, 1, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzc, password=123456}, org.apache.catalina.connector.RequestFacade@ff8c863])', '2026-01-25 10:06:00', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T10:06:00.319\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (26, 1, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzc, password=123456}, org.apache.catalina.connector.RequestFacade@488a2523])', '2026-01-25 10:33:50', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T10:33:50.711\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (27, 1, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzc, password=123456}, org.apache.catalina.connector.RequestFacade@7d8cd079])', '2026-01-25 10:51:42', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T10:51:42.170\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (28, NULL, 'com.jhc.fruit.controller.EmployeeController.login([{name=zzc, password=123456}, org.apache.catalina.connector.RequestFacade@585fa363])', '2026-01-25 14:27:36', '127.0.0.1', '{\"code\":0,\"map\":{},\"msg\":\"用户名不存在\"}');
INSERT INTO `operation_log` VALUES (29, NULL, 'com.jhc.fruit.controller.EmployeeController.login([{name=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@585fa363])', '2026-01-25 14:28:35', '127.0.0.1', '{\"code\":0,\"map\":{},\"msg\":\"用户名不存在\"}');
INSERT INTO `operation_log` VALUES (30, 1, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzc, password=123456}, org.apache.catalina.connector.RequestFacade@585fa363])', '2026-01-25 14:28:58', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T14:28:57.667\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (31, 1, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzc, password=123456}, org.apache.catalina.connector.RequestFacade@3405aeab])', '2026-01-25 14:29:58', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T14:29:58.186\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (32, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeDTO(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@3715f44c])', '2026-01-25 14:37:53', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T14:37:53.397\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (33, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@2422f7f1])', '2026-01-25 14:57:39', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T14:57:39.229\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (34, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@2cec7f7d])', '2026-01-25 15:07:52', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:07:52.497\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (35, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@6edbfd0b])', '2026-01-25 15:22:25', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:22:25.477608600\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (36, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@6f647058])', '2026-01-25 15:25:45', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:25:45.108\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (37, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@338be0cb])', '2026-01-25 15:26:08', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:26:08.592764700\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (38, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@2df06cfc])', '2026-01-25 15:29:23', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:29:24.428046800\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (39, 2, 'com.jhc.fruit.controller.EmployeeController.logout([org.apache.catalina.connector.RequestFacade@5c677b17])', '2026-01-25 15:32:05', '127.0.0.1', '{\"code\":1,\"data\":\"退出成功\",\"map\":{},\"token\":\"null\"}');
INSERT INTO `operation_log` VALUES (40, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@55d13c98])', '2026-01-25 15:32:47', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:32:47.534798300\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (41, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@55d13c98])', '2026-01-25 15:34:38', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:34:38.559766\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (42, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@4fc19ddf])', '2026-01-25 15:38:25', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:38:25.954526300\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (43, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@cdc13b])', '2026-01-25 15:40:35', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:40:35.215658300\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (44, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@12a37511])', '2026-01-25 15:43:20', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:43:21.497937800\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (45, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@c456598])', '2026-01-25 15:59:15', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:59:16.347242300\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (46, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@1f541888])', '2026-01-25 15:59:35', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:59:34.805\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (47, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@56ba501d])', '2026-01-25 15:59:47', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T15:59:46.576763800\",\"updateUser\":2,\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (48, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@7d80f16b])', '2026-01-25 16:17:54', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T16:17:54.296\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (49, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@3456ef3a])', '2026-01-25 16:19:10', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T16:19:10.303\",\"updateUser\":1,\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (50, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@6b40866b])', '2026-01-25 16:45:10', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T16:45:11.623312600\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (51, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@1eaf0331])', '2026-01-25 16:46:18', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T16:46:18.431\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (52, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@38df8732])', '2026-01-25 16:50:25', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T16:50:25.971582800\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (53, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@74f1a719])', '2026-01-25 16:57:08', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T16:57:08.371900700\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (54, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@63c40b17])', '2026-01-25 17:01:52', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T17:01:53.500476300\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (55, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@3d7f5aaf])', '2026-01-25 17:08:07', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T17:08:08.168432200\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (56, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@3d7f5aaf])', '2026-01-25 17:10:46', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T17:10:46.397699300\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (57, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@bfbc363])', '2026-01-25 17:18:00', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T17:18:01.685702700\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (58, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@178565b6])', '2026-01-25 17:56:35', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T17:56:34.760\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (59, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@72f23681])', '2026-01-25 18:07:01', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T18:07:01.313\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (60, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@737a379a])', '2026-01-25 18:14:55', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T18:14:55.753736200\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (61, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@6a48d319])', '2026-01-25 18:47:32', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T18:47:32.902299600\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (62, 2, 'com.jhc.fruit.controller.EmployeeController.login([{username=zzz, password=123456}, org.apache.catalina.connector.RequestFacade@464b46f6])', '2026-01-25 18:52:20', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"1\",\"id\":2,\"isAdmin\":1,\"name\":\"zzz\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"12345678900\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-25T18:52:21.163188500\",\"username\":\"zzz\"},\"map\":{},\"token\":\"2\"}');
INSERT INTO `operation_log` VALUES (63, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@2150fda7])', '2026-01-26 10:03:49', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T10:03:49.644\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (64, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@5f000c71])', '2026-01-26 10:17:50', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T10:17:50.625\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (65, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@78fa97ae])', '2026-01-26 10:38:42', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T10:38:42.411\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (66, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@6994c58d])', '2026-01-26 10:47:21', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T10:47:21.695\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (67, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@5581ab48])', '2026-01-26 10:50:12', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T10:50:12.327\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (68, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@5581ab48])', '2026-01-26 10:57:05', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T10:57:05.365\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (69, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@3c43515b])', '2026-01-26 11:22:25', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T11:22:25.605\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (70, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@175ab7b1])', '2026-01-26 13:28:05', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T13:28:05.336\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (71, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@163ab2d7])', '2026-01-26 13:40:33', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T13:40:33.685\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (72, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@74dc018e])', '2026-01-26 13:48:07', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T13:48:07.343\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (73, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@c55c7a4])', '2026-01-26 13:55:34', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T13:55:34.061\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (74, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@1c45ffb])', '2026-01-26 14:00:29', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T14:00:28.861\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (75, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@5d8d1aaf])', '2026-01-26 14:39:06', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T14:39:06.003\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (76, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@7da175e1])', '2026-01-26 14:39:19', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T14:39:19.180\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (77, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@3c5a47af])', '2026-01-26 14:41:35', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T14:41:35.442\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (78, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@112feeef])', '2026-01-26 14:43:31', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T14:43:30.922\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (79, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@463a0389])', '2026-01-26 14:53:22', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T14:53:22.481\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');
INSERT INTO `operation_log` VALUES (80, 1, 'com.jhc.fruit.controller.EmployeeController.login([loginEmployeeVo(username=zzc, password=123456), org.apache.catalina.connector.RequestFacade@3d185203])', '2026-01-26 15:05:35', '127.0.0.1', '{\"code\":1,\"data\":{\"createTime\":\"2026-01-22T10:23:06\",\"createUser\":1,\"headImg\":\"C:\\\\Users\\\\27141\\\\Pictures\\\\Screenshots\\\\屏幕截图 2024-08-09 013017.png\",\"id\":1,\"isAdmin\":1,\"name\":\"zzc\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"phone\":\"15312341234\",\"sex\":\"1\",\"status\":1,\"updateTime\":\"2026-01-26T15:05:35.183\",\"username\":\"zzc\"},\"map\":{},\"token\":\"1\"}');

-- ----------------------------
-- Table structure for orchard
-- ----------------------------
DROP TABLE IF EXISTS `orchard`;
CREATE TABLE `orchard`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `size` bigint NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `emp_id` bigint NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `create_user` bigint NULL DEFAULT NULL,
  `update_user` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orchard
-- ----------------------------
INSERT INTO `orchard` VALUES (1, '信息工程果园', 114514, '这是一个超大的果园，里面有着现代科技，可以实现果园->果实->果汁->果脯一键式实现', 1, '1', '2026-01-26 10:03:53', '2026-01-26 14:59:22', 1, 1);
INSERT INTO `orchard` VALUES (2, '智能制造果园', 1000, '这是一个在圈地运动后出现的一种果园，它使用新的果园模式，就是用工业革命后的内燃机驱动的机械进行果园管理，集成和大小没有信息工程果园，但是已经很接近', 1, '1', '2026-01-26 10:04:53', '2026-01-26 10:04:53', 1, 1);
INSERT INTO `orchard` VALUES (3, '航空工程果园', 200000, '这是一个在天上的果园，专门生产太阳果、月亮果与云果，这些果实的树能够以云朵为泥土，水分通过根流到云朵天面形成雨。里面的人有双特异眼，白天橙色，晚上白色对应太阳、月亮颜色', 1, '1', '2026-01-26 10:39:57', '2026-01-26 10:39:57', 1, 1);
INSERT INTO `orchard` VALUES (4, '制药工程果园', 500, '这是一个农药自产自销的果园，里面的果树长得飞快，但是这个果园生活节奏非常快，人数和生育率非常多', 1, '1', '2026-01-26 10:47:49', '2026-01-26 10:47:49', 1, 1);
INSERT INTO `orchard` VALUES (5, '建筑工程果园', 300, '这是一个以果树为建筑的果园，管理员住的房子以树干为承重的柱子，以芭蕉作为窗户。生活非常惬意', 1, '1', '2026-01-26 10:50:24', '2026-01-26 10:50:24', 1, 1);
INSERT INTO `orchard` VALUES (7, '农果园', 400, '这是一个以精细化、男耕女织的时代。在管理果园上还在用曲袁犁耕地，用牛做苦力的果园', 2, '1', '2026-01-26 13:48:14', '2026-01-26 13:48:14', 1, 1);
INSERT INTO `orchard` VALUES (8, '师范果园', 500, '这是一个以教育著称的果园，里面拥有着一本传男不传女的种植果树秘籍：馗花保点，这本书只有这个果园的原住民才能看懂', 1, '1', '2026-01-26 13:49:06', '2026-01-26 13:49:06', 1, 1);
INSERT INTO `orchard` VALUES (9, '医果园', 500, '这是一个比较管理人比较懒散的果园。正是因为他们懒散，并加上他们的智慧，才能在果树有病虫害的时候能够通过他们的精妙手段进行治疗。果子的生长状态在治疗后还能提升', 1, '1', '2026-01-26 13:55:45', '2026-01-26 13:55:45', 1, 1);
INSERT INTO `orchard` VALUES (10, '商果园', 300, '这里拥有四面八方的果子，这里的人通过贸易把自己的小手工卖出去，买到别的果园的果子。每过一年就会休息5天进行庆祝。祈祷来年顺风顺水', 1, '1', '2026-01-26 14:49:09', '2026-01-26 14:49:09', 1, 1);
INSERT INTO `orchard` VALUES (11, '文旅果园', 2000, '这是一个给勇者的果园。这里天气非常恶劣，前一天30度，今天就可能-30度。明天可能就有火山喷发（这里有四座火山），后天可能刮10级台风（在海洋附近，和海洋有一个山脉阻挡），昼夜温差极大。也因此种出的果树能结果的话都会非常好吃', 1, '1', '2026-01-26 14:49:15', '2026-01-26 14:49:15', 1, 1);
INSERT INTO `orchard` VALUES (12, '设计果园', 1000, '这是一个专注于雕刻木制家具的果园。每一件这里出产的家具都很有设计感，其特产就是青花木', 1, '1', '2026-01-26 14:50:01', '2026-01-26 14:50:01', 1, 1);
INSERT INTO `orchard` VALUES (13, '武义果园', 999, '这是一个神秘的果园，每一个进入这个果园的人不过多久出来的时候就会发疯，啃掉自己的舌头，每天嘴里都在碎碎念，像是想说什么，但可惜没有舌头。出来后没过多久就会离奇死亡。相传是因为这个果园里的神秘信仰会让人无意识的被同质化，沦为虔诚的信徒。他们给这些进去的信徒都下了诅咒，如果逃出去就会自己咬断舌头，并让他们没过多久就离奇死亡。', 1, '1', '2026-01-26 14:50:08', '2026-01-26 15:07:46', 1, 1);
INSERT INTO `orchard` VALUES (14, '达芬果园', 100, '测试果园', 1, '0', '2026-01-27 11:05:55', '2026-01-27 11:05:55', NULL, NULL);
INSERT INTO `orchard` VALUES (15, '小别尚果园', 100, '测试果园', 1, '0', '2026-01-27 13:37:02', '2026-01-27 13:37:02', NULL, NULL);
INSERT INTO `orchard` VALUES (16, '五马分食果园', 100, '测试果园', 1, '0', '2026-01-27 13:43:19', '2026-01-27 13:43:19', NULL, NULL);
INSERT INTO `orchard` VALUES (17, '四叔果园', 100, '友谊第一，第二比赛，裁判不在就玩赖', 1, '0', '2026-01-27 13:51:36', '2026-01-27 13:51:36', NULL, NULL);
INSERT INTO `orchard` VALUES (18, '夜来香果园', 100, '测试果园成功', 1, '0', '2026-01-27 14:02:45', '2026-01-27 14:02:45', NULL, NULL);
INSERT INTO `orchard` VALUES (19, '91果园', 100, '测试果园成功', 1, '0', '2026-01-27 19:03:13', '2026-01-27 19:03:13', NULL, NULL);
INSERT INTO `orchard` VALUES (20, '离子园', 100, '测试果园成功', 1, '0', '2026-01-27 19:05:05', '2026-01-27 19:05:05', NULL, NULL);
INSERT INTO `orchard` VALUES (21, '26号守卫果园', 100, '测试果园成功', 1, '0', '2026-01-27 19:05:14', '2026-01-27 19:05:14', NULL, NULL);
INSERT INTO `orchard` VALUES (22, '好吃到爆果园', 100, '测试果园成功', 1, '0', '2026-01-27 19:05:51', '2026-01-27 19:05:51', NULL, NULL);

-- ----------------------------
-- Table structure for pick_info
-- ----------------------------
DROP TABLE IF EXISTS `pick_info`;
CREATE TABLE `pick_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '通知内容',
  `sender_id` bigint NULL DEFAULT NULL,
  `emp_id` bigint NOT NULL COMMENT '接收用户ID',
  `group_id` bigint NULL DEFAULT NULL COMMENT '接收用户姓名',
  `orchard_id` bigint NULL DEFAULT NULL,
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `is_finish` tinyint NULL DEFAULT 0 COMMENT '是否完成：0-未完成，1-已完成',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '采摘通知信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pick_info
-- ----------------------------
INSERT INTO `pick_info` VALUES (3, 'string', 'content', 1, 1, 1, 1, 1, 0, '2026-01-25 11:02:22', '2026-01-27 09:50:45', 1, 1);
INSERT INTO `pick_info` VALUES (4, 'title', 'content', 1, 1, 1, 1, 1, 0, '2026-01-25 11:02:43', '2026-01-27 09:50:45', 1, 1);
INSERT INTO `pick_info` VALUES (5, 'string', '111', 1, 1, 1, 1, 1, 1, '2026-01-25 16:53:09', '2026-01-27 09:50:45', 1, 1);
INSERT INTO `pick_info` VALUES (6, '标题', '通知内容', 1, 1, 1, 1, 1, 1, '2026-01-25 16:53:47', '2026-01-27 09:50:46', 1, 1);
INSERT INTO `pick_info` VALUES (7, '标题', '通知内容', 1, 1, 1, 1, 0, 0, '2026-01-25 16:54:17', '2026-01-27 09:50:46', 1, 1);
INSERT INTO `pick_info` VALUES (8, '新接口测试标题', '新接口发布1', 1, 1, 1, 1, 1, 0, '2026-01-25 17:58:33', '2026-01-27 09:50:47', 1, 1);
INSERT INTO `pick_info` VALUES (9, 'string', '修改通知内容方法1', 1, 2, 1, 1, 1, 1, '2026-01-25 17:58:58', '2026-01-27 09:50:47', 11, 1);
INSERT INTO `pick_info` VALUES (10, '新接口测试标题', '新接口发布2', 1, 1, 1, 1, 0, 1, '2026-01-25 17:59:04', '2026-01-27 09:50:48', 1, 1);
INSERT INTO `pick_info` VALUES (11, '新接口测试标题', '新接口发布2', 1, 1, 1, 1, 1, 0, '2026-01-25 17:59:15', '2026-01-27 09:50:50', 1, 1);
INSERT INTO `pick_info` VALUES (12, '新接口测试标题', '新接口发布2', 1, 1, 1, 1, 1, 1, '2026-01-25 17:59:25', '2026-01-27 09:50:49', 1, 1);
INSERT INTO `pick_info` VALUES (14, '成功', '1.27需要测试发布通知（所有id对应不上的测试）', 1, 1, 1, 1, 0, 0, '2026-01-27 09:37:20', '2026-01-27 09:43:28', 1, 1);
INSERT INTO `pick_info` VALUES (15, '失败：empId错误', '1.27需要测试发布通知（所有id对应不上的测试）', 1, 1, 1, 1, 0, 0, '2026-01-27 09:37:41', '2026-01-27 09:43:29', 1, 1);
INSERT INTO `pick_info` VALUES (16, '失败2', '1.27需要测试发布通知（所有id对应不上的测试）', 1, 1, 1, 1, 0, 0, '2026-01-27 09:40:38', '2026-01-27 09:43:30', 1, 1);
INSERT INTO `pick_info` VALUES (17, 'string', 'string', 1, 1, 1, 1, 1, 1, '2026-01-27 09:43:09', '2026-01-27 09:43:32', 1, 1);
INSERT INTO `pick_info` VALUES (18, 'string', '雪花算法测试', 1, 1, 1, 1, 0, 1, '2026-01-27 09:44:47', '2026-01-27 09:53:34', 1, 1);
INSERT INTO `pick_info` VALUES (19, 'string', '雪花算法测试2', 1, 1, 1, 1, 1, 0, '2026-01-27 09:45:19', '2026-01-27 09:53:46', 1, 1);
INSERT INTO `pick_info` VALUES (20, 'string', '雪花算法测试3', 1, 1, 1, 1, 1, 1, '2026-01-27 09:45:48', '2026-01-27 09:53:50', 1, 1);
INSERT INTO `pick_info` VALUES (21, '修改成功', '修改测试', 1, 1, 1, 1, 1, 1, '2026-01-27 09:47:10', '2026-01-27 15:02:35', 1, 1);

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` bigint NOT NULL COMMENT '任务id',
  `creator_id` bigint NOT NULL COMMENT '创建人id',
  `task_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '任务内容',
  `task_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称/标题',
  `task_type` tinyint NULL DEFAULT NULL COMMENT '任务类型(0一次性/1定时)',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务图片',
  `task_scope` bigint NULL DEFAULT NULL COMMENT '发布范围,绑定emp_group.group_id',
  `completion_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `deadline` datetime NULL DEFAULT NULL COMMENT '截止时间',
  `post_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '任务状态（0未发布/1未完成/2完成/3截止）',
  `schedule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '定时发布',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (0, 0, 'string', 'string', 0, 'string', 1, '2026-01-26 12:25:22', NULL, '2026-01-26 12:25:22', '2026-01-26 12:25:22', '2026-01-26 12:25:22', 0, NULL);

-- ----------------------------
-- Table structure for weather
-- ----------------------------
DROP TABLE IF EXISTS `weather`;
CREATE TABLE `weather`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '天气ID',
  `date` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日期（如：2022-05-17）',
  `week` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '星期（如：星期一）',
  `location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市/地区名称',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '天气类型（晴、阴、雨等）',
  `high_temp` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最高温度（带单位）',
  `low_temp` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最低温度（带单位）',
  `fx` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风向',
  `wind` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风力',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '天气信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weather
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
