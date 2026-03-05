/*
 Navicat Premium Data Transfer

 Source Server         : 120.27.205.171
 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : 120.27.205.171:3306
 Source Schema         : fruit

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001

 Date: 05/03/2026 09:58:23
*/

DROP DATABASE IF EXISTS fruit;
CREATE DATABASE fruit DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fruit;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announce_info
-- ----------------------------
DROP TABLE IF EXISTS `announce_info`;
CREATE TABLE `announce_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '通知内容',
  `sender_id` bigint NULL DEFAULT NULL,
  `emp_id` bigint NOT NULL COMMENT '接收用户ID',
  `group_id` bigint NULL DEFAULT NULL COMMENT '接收用户姓名',
  `need_number` bigint NULL DEFAULT NULL,
  `orchard_id` bigint NULL DEFAULT NULL,
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `is_finish` tinyint NULL DEFAULT 0 COMMENT '是否完成：0-未完成，1-已完成',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '采摘通知信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of announce_info
-- ----------------------------
INSERT INTO `announce_info` VALUES (3, 'string', 'content', 1, 1, 1, NULL, 1, 1, 0, '2026-01-25 11:02:22', '2026-01-27 09:50:45', 1, 1);
INSERT INTO `announce_info` VALUES (4, 'title', 'content', 1, 1, 1, NULL, 1, 1, 0, '2026-01-25 11:02:43', '2026-01-27 09:50:45', 1, 1);
INSERT INTO `announce_info` VALUES (5, 'string', '111', 1, 1, 1, NULL, 1, 1, 1, '2026-01-25 16:53:09', '2026-01-27 09:50:45', 1, 1);
INSERT INTO `announce_info` VALUES (6, '标题', '通知内容', 1, 1, 1, NULL, 1, 1, 1, '2026-01-25 16:53:47', '2026-01-27 09:50:46', 1, 1);
INSERT INTO `announce_info` VALUES (7, '标题', '通知内容', 1, 1, 1, NULL, 1, 0, 0, '2026-01-25 16:54:17', '2026-01-27 09:50:46', 1, 1);
INSERT INTO `announce_info` VALUES (8, '新接口测试标题', '新接口发布1', 1, 1, 1, NULL, 1, 1, 0, '2026-01-25 17:58:33', '2026-01-27 09:50:47', 1, 1);
INSERT INTO `announce_info` VALUES (9, 'string', '修改通知内容方法1', 1, 2, 1, NULL, 1, 1, 1, '2026-01-25 17:58:58', '2026-01-27 09:50:47', 11, 1);
INSERT INTO `announce_info` VALUES (10, '新接口测试标题', '新接口发布2', 1, 1, 1, NULL, 1, 0, 1, '2026-01-25 17:59:04', '2026-01-27 09:50:48', 1, 1);
INSERT INTO `announce_info` VALUES (11, '新接口测试标题', '新接口发布2', 1, 1, 1, NULL, 1, 1, 0, '2026-01-25 17:59:15', '2026-01-27 09:50:50', 1, 1);
INSERT INTO `announce_info` VALUES (12, '新接口测试标题', '新接口发布2', 1, 1, 1, NULL, 1, 1, 1, '2026-01-25 17:59:25', '2026-01-27 09:50:49', 1, 1);
INSERT INTO `announce_info` VALUES (14, '成功', '1.27需要测试发布通知（所有id对应不上的测试）', 1, 1, 1, NULL, 1, 0, 0, '2026-01-27 09:37:20', '2026-01-27 09:43:28', 1, 1);
INSERT INTO `announce_info` VALUES (15, '失败：empId错误', '1.27需要测试发布通知（所有id对应不上的测试）', 1, 1, 1, NULL, 1, 0, 0, '2026-01-27 09:37:41', '2026-01-27 09:43:29', 1, 1);
INSERT INTO `announce_info` VALUES (16, '失败2', '1.27需要测试发布通知（所有id对应不上的测试）', 1, 1, 1, NULL, 1, 0, 0, '2026-01-27 09:40:38', '2026-01-27 09:43:30', 1, 1);
INSERT INTO `announce_info` VALUES (17, 'string', 'string', 1, 1, 1, NULL, 1, 1, 1, '2026-01-27 09:43:09', '2026-01-27 09:43:32', 1, 1);
INSERT INTO `announce_info` VALUES (18, 'string', '雪花算法测试', 1, 1, 1, NULL, 1, 0, 1, '2026-01-27 09:44:47', '2026-01-27 09:53:34', 1, 1);
INSERT INTO `announce_info` VALUES (19, 'string', '雪花算法测试2', 1, 1, 1, NULL, 1, 1, 0, '2026-01-27 09:45:19', '2026-01-27 09:53:46', 1, 1);
INSERT INTO `announce_info` VALUES (20, 'string', '雪花算法测试3', 1, 1, 1, NULL, 1, 1, 1, '2026-01-27 09:45:48', '2026-01-27 09:53:50', 1, 1);
INSERT INTO `announce_info` VALUES (21, '修改成功', '修改测试', 1, 1, 1, NULL, 1, 1, 1, '2026-01-27 09:47:10', '2026-01-27 15:02:35', 1, 1);

-- ----------------------------
-- Table structure for area_info
-- ----------------------------
DROP TABLE IF EXISTS `area_info`;
CREATE TABLE `area_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地区id',
  `type_id` bigint NULL DEFAULT NULL COMMENT '类型关联ID',
  `emp_id` bigint NULL DEFAULT NULL COMMENT '关联员工ID',
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地区名称',
  `size` int NULL DEFAULT 0 COMMENT '地区面积/大小',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '地区信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of area_info
-- ----------------------------
INSERT INTO `area_info` VALUES (2, 1, 2014518886322917378, NULL, 'zzc', 100, '地区测试', '2026-01-22 14:12:23', '2026-01-23 10:02:38', 1, 1);
INSERT INTO `area_info` VALUES (4, 1, 2014518886322917378, NULL, 'test2', 666, 'des:测试', '2026-01-24 14:14:43', '2026-01-24 14:14:43', 2, 2);
INSERT INTO `area_info` VALUES (5, 1, 2014518886322917378, NULL, 'test2', 888, 'test2', '2026-01-24 14:16:11', '2026-01-24 14:16:11', 2, 2);
INSERT INTO `area_info` VALUES (6, 1, 1, 1, '东实南一楼', 10, '1', '2026-01-28 13:46:48', '2026-01-28 13:46:48', NULL, NULL);
INSERT INTO `area_info` VALUES (7, 1, 1, 1, '东实北一楼', 10, '1', '2026-01-28 17:38:58', '2026-01-28 17:38:58', NULL, NULL);

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
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '摄像头信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of camera
-- ----------------------------
INSERT INTO `camera` VALUES (1, 1, NULL, '2026-01-23 09:31:55', '2026-01-23 09:31:55', 1, 1, NULL);
INSERT INTO `camera` VALUES (2, 111, NULL, '2026-01-23 09:32:05', '2026-01-23 09:32:05', 1, 1, NULL);

-- ----------------------------
-- Table structure for emp_group
-- ----------------------------
DROP TABLE IF EXISTS `emp_group`;
CREATE TABLE `emp_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` bigint NOT NULL COMMENT '分组ID',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组名',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容/备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_finish` int NULL DEFAULT NULL,
  `info_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工分组表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emp_group
-- ----------------------------
INSERT INTO `emp_group` VALUES (1, 1, 'test', '1', '2026-01-27 09:10:37', '2026-01-27 09:15:38', '1', 0, NULL);

-- ----------------------------
-- Table structure for empgroup_emp
-- ----------------------------
DROP TABLE IF EXISTS `empgroup_emp`;
CREATE TABLE `empgroup_emp`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` bigint NULL DEFAULT NULL COMMENT '用户组id',
  `emp_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户-用户组关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of empgroup_emp
-- ----------------------------

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
  `sex` tinyint NULL DEFAULT NULL COMMENT '性别',
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `status` tinyint NULL DEFAULT 1 COMMENT '员工状态(可用：1，禁用：0)',
  `head_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `is_admin` tinyint NULL DEFAULT 0 COMMENT '1:super_admin ， 2 ： admin ， 3 ： 农户 ， 4 ： 消费者，5：游客',
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园ID',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信开放ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建者',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, 'zzc', 'zzc', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, 'C:\\Users\\27141\\Pictures\\Screenshots\\屏幕截图 2024-08-09 013017.png', 1, 1, '1', '2026-01-22 10:23:06', '2026-02-26 10:20:45', 1, NULL);
INSERT INTO `employee` VALUES (2, 'admin1213', 'admin1213', 'e10adc3949ba59abbe56e057f20f883e', '12345678900', 1, NULL, 1, '1', 1, 2, '1', '2026-01-22 10:23:06', '2026-02-26 16:42:01', 1, NULL);
INSERT INTO `employee` VALUES (3, 'admin', '张三', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 2, 1, '1', '2026-02-28 22:48:06', '2026-02-28 22:51:04', NULL, NULL);
INSERT INTO `employee` VALUES (4, 'super_admin', '李四', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 1, 1, '1', '2026-02-28 22:48:55', '2026-02-28 22:51:09', NULL, NULL);
INSERT INTO `employee` VALUES (2014221904027000834, '222', '222', 'e10adc3949ba59abbe56e057f20f883e', '1', 1, NULL, 1, NULL, 2, 3, NULL, '2026-01-22 14:21:39', '2026-01-28 13:40:19', 1, 1);
INSERT INTO `employee` VALUES (2014518886322917378, 'yqfzr', '园区负责人', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 4, NULL, '2026-01-23 10:01:45', '2026-01-28 13:40:20', 1, 1);
INSERT INTO `employee` VALUES (2014518995668422658, 'czry', '采摘人员', 'e10adc3949ba59abbe56e057f20f883e', '15012341234', 1, NULL, 1, NULL, 2, 5, NULL, '2026-01-23 10:02:11', '2026-01-28 13:40:21', 1, 1);
INSERT INTO `employee` VALUES (2016320759451226113, '大东北', '东北雨姐', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 0, NULL, 1, NULL, 0, 1, NULL, '2026-01-28 09:21:45', '2026-01-28 13:48:55', 1, 1);
INSERT INTO `employee` VALUES (2016320945154035713, 'jntm', '蔡徐坤', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 2, 1, NULL, '2026-01-28 09:22:29', '2026-01-28 13:41:30', 1, 1);
INSERT INTO `employee` VALUES (2016321188155232258, '日不落帝国', '维多利亚', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 0, NULL, 1, NULL, 0, 2, NULL, '2026-01-28 09:23:27', '2026-01-28 13:41:31', 1, 1);
INSERT INTO `employee` VALUES (2016321503592058881, '一飞冲天', '超级飞侠', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 3, NULL, '2026-01-28 09:24:43', '2026-01-28 13:41:32', 1, 1);
INSERT INTO `employee` VALUES (2016322733374574593, '111', '111', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 3, 4, NULL, '2026-01-28 09:29:36', '2026-03-02 11:30:37', 1, 1);
INSERT INTO `employee` VALUES (2016323985118466049, '小因因', '林徽因', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 5, NULL, '2026-01-28 09:34:34', '2026-01-28 13:41:36', 1, 1);
INSERT INTO `employee` VALUES (2016324641246023682, '熊出没牢大', '熊大', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 7, NULL, '2026-01-28 09:37:11', '2026-01-28 13:41:38', 1, 1);
INSERT INTO `employee` VALUES (2016325010390913025, '重生之我在学校开果园', '班主任', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 8, NULL, '2026-01-28 09:38:39', '2026-01-28 13:41:41', 1, 1);
INSERT INTO `employee` VALUES (2016325372128661506, '女儿红', '观音菩萨', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 8, NULL, '2026-01-28 09:40:05', '2026-01-28 13:43:34', 1, 1);
INSERT INTO `employee` VALUES (2016325539749826562, '大卫带', '良子', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 9, NULL, '2026-01-28 09:40:45', '2026-01-28 13:43:35', 1, 1);
INSERT INTO `employee` VALUES (2016383915632549889, '一艘海盗船浮在游泳池里被一群愤怒的美人鱼攻击', 'edlee', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 10, NULL, '2026-01-28 13:32:43', '2026-01-28 13:43:37', 1, 1);
INSERT INTO `employee` VALUES (2016384629461147650, '你在成都只能算个萝莉', '成都超人', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 11, NULL, '2026-01-28 13:35:33', '2026-01-28 13:43:38', 1, 1);
INSERT INTO `employee` VALUES (2016385307873042434, '你被捕了', '尼克狐尼克', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 12, NULL, '2026-01-28 13:38:15', '2026-01-28 13:43:39', 1, 1);
INSERT INTO `employee` VALUES (2016385689022029826, '万物归一者', '尤格索托斯', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 13, NULL, '2026-01-28 13:39:46', '2026-01-28 13:43:41', 1, 1);

-- ----------------------------
-- Table structure for fruit_emp
-- ----------------------------
DROP TABLE IF EXISTS `fruit_emp`;
CREATE TABLE `fruit_emp`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `emp_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `tree_id` bigint NULL DEFAULT NULL COMMENT '果树id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '果树与员工关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fruit_emp
-- ----------------------------

-- ----------------------------
-- Table structure for fruit_group
-- ----------------------------
DROP TABLE IF EXISTS `fruit_group`;
CREATE TABLE `fruit_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '果树组id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组名',
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '员工组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fruit_group
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '果树生长过程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fruit_grow
-- ----------------------------
INSERT INTO `fruit_grow` VALUES (2014219934159544322, NULL, NULL, NULL, '2026-01-22 14:13:49', '2026-01-22 14:13:49');
INSERT INTO `fruit_grow` VALUES (2014588807594266625, NULL, NULL, NULL, '2026-01-23 14:39:32', '2026-01-23 14:39:32');
INSERT INTO `fruit_grow` VALUES (2014900706571603970, NULL, NULL, NULL, '2026-01-24 11:18:55', '2026-01-24 11:18:55');

-- ----------------------------
-- Table structure for fruit_price
-- ----------------------------
DROP TABLE IF EXISTS `fruit_price`;
CREATE TABLE `fruit_price`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_id` bigint NULL DEFAULT NULL COMMENT '水果品种id，对应type表',
  `fruit_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '水果品种',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '水果价格（元，保留2位小数）',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计价单位（如：元/斤、元/公斤）',
  `origin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '产地（如：山东烟台、云南昆明）',
  `market` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易市场名称（如：北京新发地、上海江桥）',
  `price_date` date NULL DEFAULT NULL COMMENT '价格日期（记录该价格的日期）',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（自动更新）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（自动填充）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2028363748257034265 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水果价格记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fruit_price
-- ----------------------------
INSERT INTO `fruit_price` VALUES (2028363748257034243, NULL, '柑桔', 5.10, '斤', '浙江宁波市象山县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034244, NULL, '柑桔', 0.85, '斤', '浙江衢州市龙游县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034245, NULL, '柑桔', 1.70, '斤', '浙江台州市三门县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034246, NULL, '柑桔', 1.28, '斤', '福建宁德市霞浦县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034247, NULL, '柑桔', 1.02, '斤', '江西新余市渝水区', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034248, NULL, '柑桔', 1.29, '斤', '江西抚州市南丰县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034249, NULL, '柑桔', 0.85, '斤', '河南南阳市宛城区', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034250, NULL, '柑桔', 0.68, '斤', '湖北宜昌市当阳市', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034251, NULL, '柑桔', 0.47, '斤', '湖北宜昌市秭归县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034252, NULL, '柑桔', 0.60, '斤', '湖北黄冈市蕲春县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034253, NULL, '柑桔', 0.85, '斤', '广东韶关市仁化县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034254, NULL, '柑桔', 5.10, '斤', '广东惠州市惠城区', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034255, NULL, '柑桔', 0.77, '斤', '广东梅州市丰顺县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034256, NULL, '柑桔', 1.56, '斤', '广东潮州市饶平县', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034257, NULL, '柑桔', 3.67, '斤', '广西南宁市兴宁区', '惠农网', '2026-03-01', '2026-03-02 15:38:37', '2026-03-02 15:38:37');
INSERT INTO `fruit_price` VALUES (2028363748257034258, NULL, '柑桔', 5.10, '斤', '浙江宁波市象山县', '惠农网', '2026-03-01', '2026-03-02 15:43:18', '2026-03-02 15:43:17');
INSERT INTO `fruit_price` VALUES (2028363748257034259, NULL, '柑桔', 1.70, '斤', '浙江台州市三门县', '惠农网', '2026-03-01', '2026-03-02 15:43:19', '2026-03-02 15:43:17');
INSERT INTO `fruit_price` VALUES (2028363748257034260, NULL, '柑桔', 0.60, '斤', '湖北黄冈市蕲春县', '惠农网', '2026-03-01', '2026-03-02 15:43:19', '2026-03-02 15:43:17');
INSERT INTO `fruit_price` VALUES (2028363748257034261, NULL, 'test', 5.10, '斤', '广东惠州市惠城区', '惠农网', '2026-03-01', '2026-03-02 19:58:16', '2026-03-02 15:43:17');
INSERT INTO `fruit_price` VALUES (2028363748257034262, NULL, 'null', 7.67, '元/公斤', '山东', '北京新发地农产品批发市场', '2026-03-02', '2026-03-02 20:00:00', '2026-03-02 20:00:00');
INSERT INTO `fruit_price` VALUES (2028363748257034263, NULL, 'null', 7.85, '元/公斤', '海南', '上海农产品中心批发市场', '2026-03-02', '2026-03-02 20:00:00', '2026-03-02 20:00:00');
INSERT INTO `fruit_price` VALUES (2028363748257034264, NULL, 'null', 6.18, '元/公斤', '云南', '广州江南果菜批发市场', '2026-03-02', '2026-03-02 20:00:00', '2026-03-02 20:00:00');

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
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2014900706412220419 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '果树信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fruit_tree
-- ----------------------------
INSERT INTO `fruit_tree` VALUES (1, 1, 0, 0, NULL, 1, '2026-01-22 13:56:49', '2026-01-22 13:56:49', NULL);
INSERT INTO `fruit_tree` VALUES (2014219934159544322, 1, 0, 0, NULL, 2, '2026-01-22 14:13:49', '2026-01-22 14:13:49', NULL);
INSERT INTO `fruit_tree` VALUES (2014588807535546369, 1, 0, 0, NULL, 2, '2026-01-23 14:39:32', '2026-01-23 14:39:32', NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水果类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fruit_type
-- ----------------------------
INSERT INTO `fruit_type` VALUES (1, 'testType', '2026-01-22 14:12:15', '2026-01-23 13:56:01');

-- ----------------------------
-- Table structure for fruitgroup_fruit
-- ----------------------------
DROP TABLE IF EXISTS `fruitgroup_fruit`;
CREATE TABLE `fruitgroup_fruit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `tree_id` bigint NULL DEFAULT NULL COMMENT '果树id',
  `group_id` bigint NULL DEFAULT NULL COMMENT '果树组id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '果树组与果树关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fruitgroup_fruit
-- ----------------------------

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
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '识别信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of identify_info
-- ----------------------------
INSERT INTO `identify_info` VALUES (1, 100, 'fruitName', '1', '1', 0, 1, NULL, '2026-01-22 13:52:03', NULL, '2026-01-22 13:53:12', NULL, NULL);

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint NOT NULL COMMENT '新闻ID（雪花算法）',
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新闻标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '新闻内容',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原文链接',
  `img_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新闻图片URL',
  `agreement` int NULL DEFAULT 0 COMMENT '点赞/赞同数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '新闻资讯表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '新闻图片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of news_img
-- ----------------------------

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '操作日志',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作者ID',
  `operation_content` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作内容（登录\\修改）',
  `operation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operation_ip` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作者IP',
  `operation_result` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作返回结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for orchard
-- ----------------------------
DROP TABLE IF EXISTS `orchard`;
CREATE TABLE `orchard`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '果园id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '果园名',
  `size` bigint NULL DEFAULT NULL COMMENT '果园规模',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `emp_id` bigint NULL DEFAULT NULL COMMENT '果园管理员id',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态（1正常，2禁用，0未审核）',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `create_user` bigint NULL DEFAULT NULL,
  `update_user` bigint NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orchard
-- ----------------------------
INSERT INTO `orchard` VALUES (1, '信息工程果园', 114514, '这是一个超大的果园，里面有着现代科技，可以实现果园->果实->果汁->果脯一键式实现', 1, '1', '2026-01-26 10:03:53', '2026-01-26 14:59:22', 1, 1, NULL);
INSERT INTO `orchard` VALUES (2, '智能制造果园', 1000, '这是一个在圈地运动后出现的一种果园，它使用新的果园模式，就是用工业革命后的内燃机驱动的机械进行果园管理，集成和大小没有信息工程果园，但是已经很接近', 1, '1', '2026-01-26 10:04:53', '2026-01-26 10:04:53', 1, 1, NULL);
INSERT INTO `orchard` VALUES (3, '航空工程果园', 200000, '这是一个在天上的果园，专门生产太阳果、月亮果与云果，这些果实的树能够以云朵为泥土，水分通过根流到云朵天面形成雨。里面的人有双特异眼，白天橙色，晚上白色对应太阳、月亮颜色', 1, '1', '2026-01-26 10:39:57', '2026-01-26 10:39:57', 1, 1, NULL);
INSERT INTO `orchard` VALUES (4, '制药工程果园', 500, '这是一个农药自产自销的果园，里面的果树长得飞快，但是这个果园生活节奏非常快，人数和生育率非常多', 1, '1', '2026-01-26 10:47:49', '2026-01-26 10:47:49', 1, 1, NULL);
INSERT INTO `orchard` VALUES (5, '建筑工程果园', 300, '这是一个以果树为建筑的果园，管理员住的房子以树干为承重的柱子，以芭蕉作为窗户。生活非常惬意', 1, '1', '2026-01-26 10:50:24', '2026-01-26 10:50:24', 1, 1, NULL);
INSERT INTO `orchard` VALUES (7, '农果园', 400, '这是一个以精细化、男耕女织的时代。在管理果园上还在用曲袁犁耕地，用牛做苦力的果园', 2, '1', '2026-01-26 13:48:14', '2026-01-26 13:48:14', 1, 1, NULL);
INSERT INTO `orchard` VALUES (8, '师范果园', 500, '这是一个以教育著称的果园，里面拥有着一本传男不传女的种植果树秘籍：馗花保点，这本书只有这个果园的原住民才能看懂', 1, '1', '2026-01-26 13:49:06', '2026-01-26 13:49:06', 1, 1, NULL);
INSERT INTO `orchard` VALUES (9, '医果园', 500, '这是一个比较管理人比较懒散的果园。正是因为他们懒散，并加上他们的智慧，才能在果树有病虫害的时候能够通过他们的精妙手段进行治疗。果子的生长状态在治疗后还能提升', 1, '1', '2026-01-26 13:55:45', '2026-01-26 13:55:45', 1, 1, NULL);
INSERT INTO `orchard` VALUES (10, '商果园', 300, '这里拥有四面八方的果子，这里的人通过贸易把自己的小手工卖出去，买到别的果园的果子。每过一年就会休息5天进行庆祝。祈祷来年顺风顺水', 1, '1', '2026-01-26 14:49:09', '2026-01-26 14:49:09', 1, 1, NULL);
INSERT INTO `orchard` VALUES (11, '文旅果园', 2000, '这是一个给勇者的果园。这里天气非常恶劣，前一天30度，今天就可能-30度。明天可能就有火山喷发（这里有四座火山），后天可能刮10级台风（在海洋附近，和海洋有一个山脉阻挡），昼夜温差极大。也因此种出的果树能结果的话都会非常好吃', 1, '1', '2026-01-26 14:49:15', '2026-01-26 14:49:15', 1, 1, NULL);
INSERT INTO `orchard` VALUES (12, '设计果园', 1000, '这是一个专注于雕刻木制家具的果园。每一件这里出产的家具都很有设计感，其特产就是青花木', 1, '1', '2026-01-26 14:50:01', '2026-01-26 14:50:01', 1, 1, NULL);
INSERT INTO `orchard` VALUES (13, '武义果园', 999, '这是一个神秘的果园，每一个进入这个果园的人不过多久出来的时候就会发疯，啃掉自己的舌头，每天嘴里都在碎碎念，像是想说什么，但可惜没有舌头。出来后没过多久就会离奇死亡。相传是因为这个果园里的神秘信仰会让人无意识的被同质化，沦为虔诚的信徒。他们给这些进去的信徒都下了诅咒，如果逃出去就会自己咬断舌头，并让他们没过多久就离奇死亡。', 1, '1', '2026-01-26 14:50:08', '2026-01-26 15:07:46', 1, 1, NULL);
INSERT INTO `orchard` VALUES (14, '达芬果园', 100, '测试果园', 1, '0', '2026-01-27 11:05:55', '2026-01-27 11:05:55', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (15, '小别尚果园', 100, '测试果园', 1, '0', '2026-01-27 13:37:02', '2026-01-27 13:37:02', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (16, '五马分食果园', 100, '测试果园', 1, '0', '2026-01-27 13:43:19', '2026-01-27 13:43:19', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (17, '四叔果园', 100, '友谊第一，第二比赛，裁判不在就玩赖', 1, '0', '2026-01-27 13:51:36', '2026-01-27 13:51:36', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (18, '夜来香果园', 100, '测试果园成功', 1, '0', '2026-01-27 14:02:45', '2026-01-27 14:02:45', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (19, '91果园', 100, '测试果园成功', 1, '0', '2026-01-27 19:03:13', '2026-01-27 19:03:13', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (20, '离子园', 100, '测试果园成功', 1, '0', '2026-01-27 19:05:05', '2026-01-27 19:05:05', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (21, '26号守卫果园', 100, '测试果园成功', 1, '0', '2026-01-27 19:05:14', '2026-01-27 19:05:14', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (22, '好吃到爆果园', 100, '测试果园成功', 1, '0', '2026-01-27 19:05:51', '2026-01-27 19:05:51', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `creator_id` bigint NOT NULL COMMENT '创建人id',
  `orchard_id` bigint NOT NULL COMMENT '果园id',
  `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '任务内容',
  `task_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称/标题',
  `task_scope` bigint NULL DEFAULT NULL,
  `completion_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `deadline` datetime NULL DEFAULT NULL,
  `post_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建/发布时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '任务状态',
  `schedule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (1, 0, 1, '0', 'string', 'string', NULL, '2026-02-03 02:56:33', '2026-02-03 02:56:33', '2026-02-03 02:56:33', '2026-02-03 11:16:15', '2026-02-03 02:56:33', 0, 'string');
INSERT INTO `task` VALUES (2, 0, 0, '0', 'string', 'string', 0, '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', 0, NULL);
INSERT INTO `task` VALUES (3, 0, 0, '0', 'string', 'string', 0, '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', 0, NULL);
INSERT INTO `task` VALUES (4, 0, 0, '0', 'string', 'string', 0, '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', 0, NULL);
INSERT INTO `task` VALUES (6, 0, 0, '0', 'string', 'string', 0, '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', 0, NULL);

-- ----------------------------
-- Table structure for task_img
-- ----------------------------
DROP TABLE IF EXISTS `task_img`;
CREATE TABLE `task_img`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `task_id` bigint NULL DEFAULT NULL COMMENT '任务id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2029374345236123651 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '任务图片表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for tree_location
-- ----------------------------
DROP TABLE IF EXISTS `tree_location`;
CREATE TABLE `tree_location`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '纬度',
  `longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '经度',
  `tree_id` bigint NULL DEFAULT NULL COMMENT '果树id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '果树位置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tree_location
-- ----------------------------

-- ----------------------------
-- Table structure for treegroup_task
-- ----------------------------
DROP TABLE IF EXISTS `treegroup_task`;
CREATE TABLE `treegroup_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NULL DEFAULT NULL COMMENT '任务id',
  `group_id` bigint NULL DEFAULT NULL COMMENT '果树组id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '任务-果树组关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of treegroup_task
-- ----------------------------

-- ----------------------------
-- Table structure for treegroup_tree
-- ----------------------------
DROP TABLE IF EXISTS `treegroup_tree`;
CREATE TABLE `treegroup_tree`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tree_id` bigint NULL DEFAULT NULL COMMENT '树id',
  `group_id` bigint NULL DEFAULT NULL COMMENT '果树组id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '果树-果树组关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of treegroup_tree
-- ----------------------------

-- ----------------------------
-- Table structure for weather
-- ----------------------------
DROP TABLE IF EXISTS `weather`;
CREATE TABLE `weather`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '天气ID',
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '天气信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of weather
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
