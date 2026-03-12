/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.123.92
 Source Server Type    : MySQL
 Source Server Version : 80044
 Source Host           : 192.168.123.92:3306
 Source Schema         : fruit

 Target Server Type    : MySQL
 Target Server Version : 80044
 File Encoding         : 65001

 Date: 12/03/2026 20:58:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_weed
-- ----------------------------
DROP TABLE IF EXISTS `ai_weed`;
CREATE TABLE `ai_weed`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tree_id` bigint NULL DEFAULT NULL,
  `class_id` bigint NULL DEFAULT NULL COMMENT '种类代码',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '种类名',
  `confidence` decimal(5, 4) NULL DEFAULT NULL COMMENT '置信度',
  `x1` decimal(16, 12) NULL DEFAULT NULL COMMENT '图片像素坐标（左上角）',
  `y1` decimal(16, 12) NULL DEFAULT NULL COMMENT '图片像素坐标（左上角）',
  `x2` decimal(16, 12) NULL DEFAULT NULL COMMENT '图片像素坐标（右下角）',
  `y2` decimal(16, 12) NULL DEFAULT NULL COMMENT '图片像素坐标（右下角）',
  `originImg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原始图片',
  `outputImg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '处理图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_weed
-- ----------------------------
INSERT INTO `ai_weed` VALUES (5, NULL, 0, 'gouweibacao', 0.3176, 248.704116821289, 480.148895263672, 1672.008544921875, 1809.217041015625, 'AIWeed/origin/b8c00ef712ac4bd4ac8eb32fc3eead09.jpg', 'AIWeed/output/d84281f275e443f29c4da393e30eed1b.jpg');
INSERT INTO `ai_weed` VALUES (6, NULL, 0, 'gouweibacao', 0.2563, 326.922973632813, 1001.845031738281, 726.563476562500, 1733.009643554688, 'AIWeed/origin/b8c00ef712ac4bd4ac8eb32fc3eead09.jpg', 'AIWeed/output/d84281f275e443f29c4da393e30eed1b.jpg');

-- ----------------------------
-- Table structure for announce_info
-- ----------------------------
DROP TABLE IF EXISTS `announce_info`;
CREATE TABLE `announce_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '通知内容',
  `area_id` bigint NULL DEFAULT NULL,
  `sender_id` bigint NULL DEFAULT NULL COMMENT '发送用户id',
  `emp_id` bigint NULL DEFAULT NULL COMMENT '接收用户ID',
  `group_id` bigint NULL DEFAULT NULL COMMENT '0是发给所有人，1是发给管理员，2是发给农户、游客',
  `have_number` bigint NULL DEFAULT NULL,
  `need_number` bigint NULL DEFAULT NULL,
  `orchard_id` bigint NULL DEFAULT NULL,
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `is_finish` tinyint NULL DEFAULT 0 COMMENT '是否完成：0-未完成，1-已完成',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新人',
  `status` tinyint NULL DEFAULT NULL COMMENT '审核状态（0未通过/1审核/2已撤销/3未通过）',
  `salary` bigint NULL DEFAULT NULL COMMENT '工资',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '采摘通知信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of announce_info
-- ----------------------------
INSERT INTO `announce_info` VALUES (3, 'string', 'content', 42, 1, 1, 1, NULL, NULL, 1, 1, 0, '2026-01-25 11:02:22', '2026-03-11 09:37:51', 1, 1, NULL, NULL);
INSERT INTO `announce_info` VALUES (4, 'title', 'content', 42, 1, 1, 1, NULL, NULL, 1, 1, 0, '2026-01-25 11:02:43', '2026-03-11 09:37:50', 1, 1, NULL, NULL);
INSERT INTO `announce_info` VALUES (5, 'string', '111', 42, 1, 1, 1, NULL, NULL, 1, 1, 1, '2026-01-25 16:53:09', '2026-03-11 09:37:50', 1, 1, NULL, NULL);
INSERT INTO `announce_info` VALUES (6, '标题', '通知内容', 42, 1, 1, 1, NULL, NULL, 1, 1, 1, '2026-01-25 16:53:47', '2026-03-11 09:37:49', 1, 1, NULL, NULL);
INSERT INTO `announce_info` VALUES (7, '标题', '通知内容', 42, 1, 1, 1, NULL, NULL, 1, 0, 0, '2026-01-25 16:54:17', '2026-03-11 09:37:49', 1, 1, NULL, NULL);
INSERT INTO `announce_info` VALUES (8, '新接口测试标题', '新接口发布1', 42, 1, 1, 1, NULL, NULL, 1, 1, 0, '2026-01-25 17:58:33', '2026-03-11 09:37:48', 1, 1, NULL, NULL);
INSERT INTO `announce_info` VALUES (9, 'string', '修改通知内容方法1', 42, 1, 2, 1, NULL, NULL, 1, 1, 1, '2026-01-25 17:58:58', '2026-03-11 09:37:48', 11, 1, NULL, NULL);
INSERT INTO `announce_info` VALUES (10, '新接口测试标题', '新接口发布2', 42, 1, 1, 1, NULL, NULL, 1, 0, 1, '2026-01-25 17:59:04', '2026-03-11 09:37:47', 1, 1, NULL, NULL);
INSERT INTO `announce_info` VALUES (26, 'dfadfdfda', 'hjhfhd a', 42, 3, NULL, 1, NULL, NULL, 1, 0, 0, '2026-03-11 08:51:18', '2026-03-11 09:48:32', 3, 3, 1, NULL);
INSERT INTO `announce_info` VALUES (27, 'rf ', 'gffgr', 42, 3, NULL, 1, 1, 100, 1, 0, 0, '2026-03-11 09:14:17', '2026-03-11 09:53:50', 3, 3, 1, 91);
INSERT INTO `announce_info` VALUES (28, '1231', '123', 42, 3, 0, 0, NULL, NULL, 1, 0, 0, '2026-03-11 09:43:44', '2026-03-11 09:54:49', 3, 3, 1, 91);
INSERT INTO `announce_info` VALUES (29, '标题', '测试新接口', 42, 5, 1, NULL, 1, 91, 1, 0, 0, '2026-03-11 10:01:58', '2026-03-11 10:01:58', 1, 0, 0, 100);
INSERT INTO `announce_info` VALUES (30, '234', '23424', NULL, 4, NULL, 0, 0, 0, 0, 0, 0, '2026-03-11 21:02:04', '2026-03-11 21:02:04', 0, 0, 0, NULL);

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
  `latitude_start` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开始纬度',
  `latitude_end` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结束纬度',
  `longitude_start` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开始经度',
  `longitude_end` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结束经度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '地区信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of area_info
-- ----------------------------
INSERT INTO `area_info` VALUES (38, 3, 3, 1, '东北', 0, '东北部', '2026-03-08 09:10:38', '2026-03-08 09:10:38', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `area_info` VALUES (39, 2, 3, 1, '砂糖', 0, '算法的', '2026-03-08 10:26:02', '2026-03-08 10:26:02', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `area_info` VALUES (40, 4, 3, 1, '3443', 0, '34234', '2026-03-08 10:36:38', '2026-03-08 10:36:38', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `area_info` VALUES (41, 3, 1, 1, '都', 3, '打分', '2026-03-09 10:45:57', '2026-03-09 10:45:57', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `area_info` VALUES (42, 2, 1, 1, 'pyl-1区', 100, '这个是pyl的区域', '2026-03-09 19:54:56', '2026-03-09 19:54:56', 3, 3, NULL, NULL, NULL, NULL);
INSERT INTO `area_info` VALUES (43, 2, 32, 3, '123', 1, '321', '2026-03-12 16:45:59', '2026-03-12 16:45:59', 6, 6, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `tree_id` bigint NULL DEFAULT NULL COMMENT '果树id',
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `img` tinyint NULL DEFAULT NULL COMMENT '图片路径',
  `choice` tinyint NULL DEFAULT NULL COMMENT '选择（好吃/不好吃）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL,
  `create_user` bigint NULL DEFAULT NULL,
  `update_user` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 114 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (101, 201, 301, 401, '这棵树长势很好', NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO `comment` VALUES (102, 202, 302, 402, '果子很甜', NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO `comment` VALUES (103, 203, 303, 403, '需要施肥', NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `comment` VALUES (109, 1, 141, 1, '这果树真好吃', NULL, 25, '2026-03-08 19:23:04', '2026-03-08 19:24:48', NULL, NULL);
INSERT INTO `comment` VALUES (111, 0, 0, 0, 'string', NULL, 0, '2026-03-09 10:37:14', '2026-03-09 10:37:14', NULL, NULL);
INSERT INTO `comment` VALUES (112, 1, 141, 1, '一般', NULL, 28, '2026-03-09 20:13:28', '2026-03-09 20:13:28', NULL, NULL);
INSERT INTO `comment` VALUES (113, 1, 141, 1, '一般', NULL, 29, '2026-03-09 20:25:53', '2026-03-09 20:25:53', NULL, NULL);

-- ----------------------------
-- Table structure for disaster
-- ----------------------------
DROP TABLE IF EXISTS `disaster`;
CREATE TABLE `disaster`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `disaster_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '灾害类型',
  `user_id` bigint NULL DEFAULT NULL COMMENT '上报人id',
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  `find_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '发现时间',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `typeId` tinyint NULL DEFAULT NULL COMMENT '灾害大类（1气象，2病害，3虫害)',
  `area_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '灾害表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of disaster
-- ----------------------------
INSERT INTO `disaster` VALUES (2, '灾害预警第二次', '第二个类型', 0, 1, '2026-03-07 11:28:48', '2026-03-07 11:28:48', NULL, NULL);
INSERT INTO `disaster` VALUES (3, '🌤️【金华市实时天气】\n天气：晴\n温度：18.0℃\n湿度：28.0%\n风向：西南风 1级\n数据更新时间：2026-03-11 13:10:15\n\n⚠️ 【干旱蓝色预警】\n\n🌱 柑橘种植管理建议：\n• 早晚滴灌浇水，避免大水漫灌；\n• 喷施抗旱剂（黄腐酸钾），提高保水能力；\n• 重点防治红蜘蛛、蚜虫（干旱易爆发）。', NULL, NULL, 1, NULL, '2026-03-11 13:41:51', NULL, NULL);
INSERT INTO `disaster` VALUES (4, '柑橘种植管理建议：\n• 早晚滴灌浇水，避免大水漫灌；\n• 喷施抗旱剂（黄腐酸钾），提高保水能力；\n• 重点防治红蜘蛛、蚜虫（干旱易爆发）。', '干旱蓝色预警', NULL, 1, NULL, '2026-03-11 13:52:01', NULL, NULL);
INSERT INTO `disaster` VALUES (5, '柑橘种植管理建议：\n• 早晚滴灌浇水，避免大水漫灌；\n• 喷施抗旱剂（黄腐酸钾），提高保水能力；\n• 重点防治红蜘蛛、蚜虫（干旱易爆发）。', '干旱蓝色预警', NULL, 1, NULL, '2026-03-11 15:45:52', NULL, NULL);

-- ----------------------------
-- Table structure for disaster_type
-- ----------------------------
DROP TABLE IF EXISTS `disaster_type`;
CREATE TABLE `disaster_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `disaster_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '灾害类型映射表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of disaster_type
-- ----------------------------

-- ----------------------------
-- Table structure for emp_group
-- ----------------------------
DROP TABLE IF EXISTS `emp_group`;
CREATE TABLE `emp_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组名',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容/备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工分组表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emp_group
-- ----------------------------
INSERT INTO `emp_group` VALUES (1, 'test', '1', '2026-01-27 09:10:37', '2026-01-27 09:15:38', NULL);
INSERT INTO `emp_group` VALUES (2, 'string', 'string', '2026-03-06 20:49:36', '2026-03-06 20:49:36', NULL);
INSERT INTO `emp_group` VALUES (3, 'string', 'string', '2026-03-07 14:59:19', '2026-03-07 14:59:19', NULL);
INSERT INTO `emp_group` VALUES (4, 'string', 'string', '2026-03-07 19:42:21', '2026-03-07 19:42:21', NULL);
INSERT INTO `emp_group` VALUES (5, 'string', 'string', '2026-03-07 19:49:20', '2026-03-07 19:49:20', NULL);
INSERT INTO `emp_group` VALUES (6, '上报', '扫码上报', '2026-03-07 20:50:49', '2026-03-07 20:50:49', NULL);
INSERT INTO `emp_group` VALUES (7, 'ad', 'df', '2026-03-07 20:51:15', '2026-03-07 20:51:15', NULL);
INSERT INTO `emp_group` VALUES (8, 'adfadf', 'adf', '2026-03-07 20:51:31', '2026-03-07 20:51:31', NULL);
INSERT INTO `emp_group` VALUES (9, '浇水', '全部浇水', '2026-03-08 08:41:23', '2026-03-08 08:41:23', NULL);
INSERT INTO `emp_group` VALUES (10, 'string', 'string', '2026-03-08 09:02:13', '2026-03-08 09:02:13', NULL);
INSERT INTO `emp_group` VALUES (11, 'j浇水', 'j浇水', '2026-03-08 13:38:10', '2026-03-08 13:38:10', NULL);
INSERT INTO `emp_group` VALUES (12, 'test_time', '测试时间解析yyyy-MM-dd HH:mm:ss.SSS', '2026-03-09 21:14:51', '2026-03-09 21:14:51', NULL);
INSERT INTO `emp_group` VALUES (13, '浇水', '浇水', '2026-03-10 09:31:31', '2026-03-10 09:31:31', NULL);
INSERT INTO `emp_group` VALUES (14, '1', '123', '2026-03-12 16:40:57', '2026-03-12 16:40:57', NULL);

-- ----------------------------
-- Table structure for empgroup_emp
-- ----------------------------
DROP TABLE IF EXISTS `empgroup_emp`;
CREATE TABLE `empgroup_emp`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` bigint NULL DEFAULT NULL COMMENT '用户组id',
  `emp_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户-用户组关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of empgroup_emp
-- ----------------------------
INSERT INTO `empgroup_emp` VALUES (1, 1, 1);
INSERT INTO `empgroup_emp` VALUES (2, 0, 2);
INSERT INTO `empgroup_emp` VALUES (3, 1, 2);
INSERT INTO `empgroup_emp` VALUES (4, 2, 2);
INSERT INTO `empgroup_emp` VALUES (5, 3, 2);
INSERT INTO `empgroup_emp` VALUES (6, 4, 2);
INSERT INTO `empgroup_emp` VALUES (7, 0, 3);
INSERT INTO `empgroup_emp` VALUES (8, 1, 3);
INSERT INTO `empgroup_emp` VALUES (9, 2, 3);
INSERT INTO `empgroup_emp` VALUES (10, 3, 3);
INSERT INTO `empgroup_emp` VALUES (11, 0, 4);
INSERT INTO `empgroup_emp` VALUES (12, 0, 5);
INSERT INTO `empgroup_emp` VALUES (13, 1, 5);
INSERT INTO `empgroup_emp` VALUES (14, 2, 5);
INSERT INTO `empgroup_emp` VALUES (15, 3, 5);
INSERT INTO `empgroup_emp` VALUES (16, 3, 6);
INSERT INTO `empgroup_emp` VALUES (17, 4, 6);
INSERT INTO `empgroup_emp` VALUES (18, 2016320759451226000, 7);
INSERT INTO `empgroup_emp` VALUES (19, 2030129999780094000, 8);
INSERT INTO `empgroup_emp` VALUES (20, 1, 9);
INSERT INTO `empgroup_emp` VALUES (21, 0, 10);
INSERT INTO `empgroup_emp` VALUES (22, 11, 1);
INSERT INTO `empgroup_emp` VALUES (23, 1, 2016322733374574593);
INSERT INTO `empgroup_emp` VALUES (24, 12, 23);
INSERT INTO `empgroup_emp` VALUES (25, 13, 1);
INSERT INTO `empgroup_emp` VALUES (26, 14, 12);

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '员工id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, '1', '庞依莲', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 0, '11010119900307491X', 1, NULL, 3, 1, NULL, '2026-03-05 10:58:36', '2026-03-12 19:40:38', NULL, NULL);
INSERT INTO `employee` VALUES (2, 'admin1213', 'admin1213', 'e10adc3949ba59abbe56e057f20f883e', '12345678900', 1, NULL, 1, '1', 1, 1, '1', '2026-01-22 10:23:06', '2026-03-06 20:43:32', 1, NULL);
INSERT INTO `employee` VALUES (3, 'admin', '张三', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 2, 1, '1', '2026-02-28 22:48:06', '2026-03-09 18:43:06', NULL, NULL);
INSERT INTO `employee` VALUES (4, 'super_admin', '李四', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 1, 1, '1', '2026-02-28 22:48:55', '2026-03-09 17:52:21', NULL, NULL);
INSERT INTO `employee` VALUES (5, 'zzc', 'zzc', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, 'C:\\Users\\27141\\Pictures\\Screenshots\\屏幕截图 2024-08-09 013017.png', 1, 1, '1', '2026-01-22 10:23:06', '2026-03-12 19:46:49', 1, NULL);
INSERT INTO `employee` VALUES (6, '222', '222', 'e10adc3949ba59abbe56e057f20f883e', '1', 1, NULL, 1, NULL, 2, 3, NULL, '2026-01-22 14:21:39', '2026-03-12 19:43:25', 1, 1);
INSERT INTO `employee` VALUES (7, 'yqfzr', '园区负责人', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 4, NULL, '2026-01-23 10:01:45', '2026-03-09 10:54:42', 1, 1);
INSERT INTO `employee` VALUES (8, 'czry', '采摘人员', 'e10adc3949ba59abbe56e057f20f883e', '15012341234', 1, NULL, 1, NULL, 2, 5, NULL, '2026-01-23 10:02:11', '2026-03-09 10:54:42', 1, 1);
INSERT INTO `employee` VALUES (9, '大东北', '东北雨姐', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 0, NULL, 1, NULL, 0, 1, NULL, '2026-01-28 09:21:45', '2026-03-09 10:54:44', 1, 1);
INSERT INTO `employee` VALUES (10, 'jntm', '蔡徐坤', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 2, 1, NULL, '2026-01-28 09:22:29', '2026-03-09 10:54:46', 1, 1);
INSERT INTO `employee` VALUES (11, '日不落帝国', '维多利亚', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 0, NULL, 1, NULL, 0, 2, NULL, '2026-01-28 09:23:27', '2026-03-09 10:54:47', 1, 1);
INSERT INTO `employee` VALUES (12, '一飞冲天', '超级飞侠', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 3, NULL, '2026-01-28 09:24:43', '2026-03-09 10:54:48', 1, 1);
INSERT INTO `employee` VALUES (13, '111', '111', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 3, 4, NULL, '2026-01-28 09:29:36', '2026-03-12 19:57:49', 1, NULL);
INSERT INTO `employee` VALUES (14, '小因因', '林徽因', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 5, NULL, '2026-01-28 09:34:34', '2026-03-09 10:54:54', 1, 1);
INSERT INTO `employee` VALUES (15, '熊出没牢大', '熊大', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 7, NULL, '2026-01-28 09:37:11', '2026-03-09 10:54:56', 1, 1);
INSERT INTO `employee` VALUES (16, '重生之我在学校开果园', '班主任', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 8, NULL, '2026-01-28 09:38:39', '2026-03-09 10:54:58', 1, 1);
INSERT INTO `employee` VALUES (17, '女儿红', '观音菩萨', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 8, NULL, '2026-01-28 09:40:05', '2026-03-09 10:54:59', 1, 1);
INSERT INTO `employee` VALUES (18, '大卫带', '良子', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 9, NULL, '2026-01-28 09:40:45', '2026-03-09 10:55:00', 1, 1);
INSERT INTO `employee` VALUES (19, '一艘海盗船浮在游泳池里被一群愤怒的美人鱼攻击', 'edlee', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 10, NULL, '2026-01-28 13:32:43', '2026-03-09 10:55:01', 1, 1);
INSERT INTO `employee` VALUES (20, '你在成都只能算个萝莉', '成都超人', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 11, NULL, '2026-01-28 13:35:33', '2026-03-09 10:55:03', 1, 1);
INSERT INTO `employee` VALUES (21, '你被捕了', '尼克狐尼克', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 12, NULL, '2026-01-28 13:38:15', '2026-03-09 10:55:04', 1, 1);
INSERT INTO `employee` VALUES (22, '万物归一者', '尤格索托斯', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 1, NULL, 1, NULL, 0, 13, NULL, '2026-01-28 13:39:46', '2026-03-09 10:55:08', 1, 1);
INSERT INTO `employee` VALUES (23, '测试', '测试', 'e10adc3949ba59abbe56e057f20f883e', 'string', 0, 'string', 0, 'string', 0, 1, 'string', '2026-03-07 11:54:45', '2026-03-09 10:55:08', 3, 3);
INSERT INTO `employee` VALUES (24, '111111', 'string', 'e10adc3949ba59abbe56e057f20f883e', 'string', 0, 'string', 0, 'string', 2, 1, 'string', '2026-03-08 10:01:57', '2026-03-09 10:55:10', NULL, NULL);
INSERT INTO `employee` VALUES (25, 'zddzc', 'string', 'e10adc3949ba59abbe56e057f20f883e', 'string', 0, 'string', 0, 'string', 0, 0, 'string', '2026-03-08 10:59:04', '2026-03-09 10:55:11', NULL, NULL);
INSERT INTO `employee` VALUES (26, 'zzc_1772950630527_1', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 1, '', 6, 1, '', '2026-03-08 14:17:09', '2026-03-09 19:06:59', NULL, NULL);
INSERT INTO `employee` VALUES (27, 'zzc_1772950630526_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 1, '', 6, 1, '', '2026-03-08 14:17:09', '2026-03-09 19:07:00', NULL, NULL);
INSERT INTO `employee` VALUES (28, 'zzc_1772950641606_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 1, '', 3, 1, '', '2026-03-08 14:17:20', '2026-03-09 19:07:04', NULL, NULL);
INSERT INTO `employee` VALUES (29, 'zzc_1772972943612_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 1, '', 6, 1, '', '2026-03-08 20:29:02', '2026-03-09 10:55:17', NULL, NULL);
INSERT INTO `employee` VALUES (30, 'zzc_1773024446707_1', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 1, '', 3, 1, '', '2026-03-09 10:47:27', '2026-03-09 10:55:19', NULL, NULL);
INSERT INTO `employee` VALUES (31, 'zzc_1773024446706_0', 'test', 'e10adc3949ba59abbe56e057f20f883e', '15312341234', 0, '110101199003076632', 1, '', 3, 1, '', '2026-03-09 10:47:27', '2026-03-12 16:35:13', NULL, NULL);
INSERT INTO `employee` VALUES (32, 'zzc_1773304516882_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:35:17', '2026-03-12 16:35:17', 6, 0);
INSERT INTO `employee` VALUES (33, 'zzc_1773304528497_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:35:29', '2026-03-12 16:35:29', 6, 0);
INSERT INTO `employee` VALUES (34, 'zzc_1773304539385_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:35:39', '2026-03-12 16:35:39', 6, 0);
INSERT INTO `employee` VALUES (35, 'zzc_1773304633129_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:37:13', '2026-03-12 16:37:13', 6, 0);
INSERT INTO `employee` VALUES (36, 'zzc_1773304658409_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:37:38', '2026-03-12 16:37:38', 6, 0);
INSERT INTO `employee` VALUES (37, 'zzc_1773304728353_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:38:48', '2026-03-12 16:38:48', 6, 0);
INSERT INTO `employee` VALUES (38, 'zzc_1773304742889_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 6, 3, '', '2026-03-12 16:39:03', '2026-03-12 16:39:03', 6, 0);
INSERT INTO `employee` VALUES (39, 'zzc_1773304793425_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:39:53', '2026-03-12 16:39:53', 6, 0);
INSERT INTO `employee` VALUES (40, 'zzc_1773304987488_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:43:08', '2026-03-12 16:43:08', 6, 0);
INSERT INTO `employee` VALUES (41, 'newUserbe331971d4d045c89acc33331ae6829e', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:44:35', '2026-03-12 16:45:19', 6, 0);
INSERT INTO `employee` VALUES (42, 'zzc_1773305091817_0', '新账号', 'e10adc3949ba59abbe56e057f20f883e', '', 0, '', 0, '', 3, 3, '', '2026-03-12 16:44:52', '2026-03-12 16:44:52', 6, 0);

-- ----------------------------
-- Table structure for fert_water
-- ----------------------------
DROP TABLE IF EXISTS `fert_water`;
CREATE TABLE `fert_water`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型（1浇水，2施肥）',
  `num` bigint NULL DEFAULT NULL COMMENT '施用量',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单位',
  `tree_id` bigint NULL DEFAULT NULL COMMENT '果树d',
  `operation_time` bigint NULL DEFAULT NULL COMMENT '操作时间',
  `operation_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作方式',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL,
  `create_user` bigint NULL DEFAULT NULL,
  `update_user` bigint NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '水肥操作记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fert_water
-- ----------------------------
INSERT INTO `fert_water` VALUES (1, '1', 1, '1', 141, NULL, '1', '2026-03-10 10:04:32', NULL, NULL, NULL, NULL);
INSERT INTO `fert_water` VALUES (2, '1', 1, '1', 127, NULL, '2', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for fruit_emp
-- ----------------------------
DROP TABLE IF EXISTS `fruit_emp`;
CREATE TABLE `fruit_emp`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `emp_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `tree_id` bigint NULL DEFAULT NULL COMMENT '果树id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '果树与员工关联表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '员工组' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fruit_group
-- ----------------------------

-- ----------------------------
-- Table structure for fruit_grow
-- ----------------------------
DROP TABLE IF EXISTS `fruit_grow`;
CREATE TABLE `fruit_grow`  (
  `id` bigint NOT NULL COMMENT '果树ID（关联果树表）',
  `first_term` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '第一期生长情况/描述',
  `second_term` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '第二期生长情况/描述',
  `third_term` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '第三期生长情况/描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '果树生长过程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fruit_grow
-- ----------------------------
INSERT INTO `fruit_grow` VALUES (0, NULL, NULL, NULL, '2026-03-06 08:55:47', '2026-03-06 08:55:47');
INSERT INTO `fruit_grow` VALUES (111, NULL, NULL, NULL, '2026-03-07 20:58:41', '2026-03-07 20:58:41');
INSERT INTO `fruit_grow` VALUES (112, NULL, NULL, NULL, '2026-03-07 21:05:06', '2026-03-07 21:05:06');
INSERT INTO `fruit_grow` VALUES (113, NULL, NULL, NULL, '2026-03-07 21:06:01', '2026-03-07 21:06:01');
INSERT INTO `fruit_grow` VALUES (2014219934159544322, NULL, NULL, NULL, '2026-01-22 14:13:49', '2026-01-22 14:13:49');
INSERT INTO `fruit_grow` VALUES (2014588807594266625, NULL, NULL, NULL, '2026-01-23 14:39:32', '2026-01-23 14:39:32');
INSERT INTO `fruit_grow` VALUES (2014900706571603970, NULL, NULL, NULL, '2026-01-24 11:18:55', '2026-01-24 11:18:55');
INSERT INTO `fruit_grow` VALUES (2030204594517700609, NULL, NULL, NULL, '2026-03-07 16:51:08', '2026-03-07 16:51:08');
INSERT INTO `fruit_grow` VALUES (2030268856694968322, NULL, NULL, NULL, '2026-03-07 21:06:29', '2026-03-07 21:06:29');
INSERT INTO `fruit_grow` VALUES (2030269043479908354, NULL, NULL, NULL, '2026-03-07 21:07:14', '2026-03-07 21:07:14');
INSERT INTO `fruit_grow` VALUES (2030270284222091266, NULL, NULL, NULL, '2026-03-07 21:12:09', '2026-03-07 21:12:09');
INSERT INTO `fruit_grow` VALUES (2030270373720150017, NULL, NULL, NULL, '2026-03-07 21:12:31', '2026-03-07 21:12:31');

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
) ENGINE = InnoDB AUTO_INCREMENT = 2028363748257034280 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水果价格记录表' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `fruit_price` VALUES (2028363748257034265, NULL, '柑桔', 1.36, '斤', '河北邯郸市丛台区', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034266, NULL, '柑桔', 5.10, '斤', '浙江宁波市象山县', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034267, NULL, '柑桔', 1.70, '斤', '浙江台州市三门县', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034268, NULL, '柑桔', 1.53, '斤', '浙江台州市临海市', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034269, NULL, '柑桔', 1.28, '斤', '福建宁德市霞浦县', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034270, NULL, '柑桔', 1.02, '斤', '江西新余市渝水区', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034271, NULL, '柑桔', 0.85, '斤', '江西吉安市万安县', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034272, NULL, '柑桔', 0.85, '斤', '江西吉安市泰和县', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034273, NULL, '柑桔', 1.93, '斤', '江西抚州市南丰县', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034274, NULL, '柑桔', 0.85, '斤', '河南南阳市宛城区', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034275, NULL, '柑桔', 0.68, '斤', '湖北宜昌市当阳市', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034276, NULL, '柑桔', 0.64, '斤', '湖北宜昌市枝江市', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034277, NULL, '柑桔', 0.47, '斤', '湖北宜昌市秭归县', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034278, NULL, '柑桔', 0.60, '斤', '湖北黄冈市蕲春县', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');
INSERT INTO `fruit_price` VALUES (2028363748257034279, NULL, '柑桔', 1.53, '斤', '湖南湘西土家族苗族自治州泸溪县', '惠农网', '2026-03-08', '2026-03-09 19:10:47', '2026-03-09 19:10:47');

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
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `orchard_id` bigint NULL DEFAULT NULL COMMENT '果园id',
  `health_condition` bigint NULL DEFAULT NULL COMMENT '健康状态',
  `create_user` bigint NULL DEFAULT NULL,
  `update_user` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 246 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '果树信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fruit_tree
-- ----------------------------
INSERT INTO `fruit_tree` VALUES (1, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 16:16:04', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (2, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 16:16:06', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (3, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 16:16:07', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (4, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 16:16:10', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (5, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 16:16:12', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (6, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 16:16:15', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (7, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 17:17:17', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (8, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 17:17:19', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (9, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 17:17:20', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (10, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 17:17:22', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (11, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 17:17:23', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (12, 2, 0, 0, NULL, 27, '2026-03-06 19:33:33', '2026-03-07 17:17:25', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (13, 3, 0, 0, NULL, 36, '2026-03-07 21:12:32', '2026-03-07 21:19:53', 1, 0, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (111, 4, 0, 0, 'string', 27, '2026-03-07 16:44:45', '2026-03-07 21:08:09', 1, 0, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (112, 4, 0, 0, 'string', 27, '2026-03-07 16:45:46', '2026-03-07 21:08:15', 1, 0, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (113, 4, 0, 0, 'string', 27, '2026-03-07 16:51:09', '2026-03-07 21:08:18', 1, 0, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (114, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:23', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (115, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:27', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (116, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:28', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (117, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:30', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (118, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:32', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (119, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:33', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (120, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:36', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (121, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:38', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (122, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:41', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (123, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:43', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (124, 3, 0, 0, NULL, 36, '2026-03-07 20:55:04', '2026-03-07 21:08:45', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (125, 3, 0, 0, NULL, 36, '2026-03-07 21:22:04', '2026-03-07 21:22:04', 1, 0, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (126, 3, 0, 0, NULL, 36, '2026-03-07 21:24:17', '2026-03-07 21:24:17', 1, 0, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (128, 3, 0, 0, NULL, 38, '2026-03-08 09:10:38', '2026-03-08 09:10:38', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (129, 3, 0, 0, NULL, 38, '2026-03-08 09:10:38', '2026-03-08 09:10:38', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (130, 3, 0, 0, NULL, 38, '2026-03-08 09:10:38', '2026-03-08 17:08:14', 1, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (131, 2, 0, 0, NULL, 39, '2026-03-08 10:26:02', '2026-03-08 10:26:02', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (132, 2, 0, 0, NULL, 39, '2026-03-08 10:26:02', '2026-03-08 10:26:02', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (133, 2, 0, 0, NULL, 39, '2026-03-08 10:26:02', '2026-03-08 10:26:02', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (134, 2, 0, 0, NULL, 39, '2026-03-08 10:26:02', '2026-03-08 10:26:02', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (135, 2, 0, 0, NULL, 39, '2026-03-08 10:26:02', '2026-03-08 10:26:02', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (136, 4, 0, 0, NULL, 40, '2026-03-08 10:36:38', '2026-03-08 10:36:38', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (137, 4, 0, 0, NULL, 40, '2026-03-08 10:36:38', '2026-03-08 10:36:38', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (138, 4, 0, 0, NULL, 40, '2026-03-08 10:36:38', '2026-03-08 10:36:38', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (139, 4, 0, 0, NULL, 40, '2026-03-08 16:47:05', '2026-03-08 16:47:05', 1, 0, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (140, 4, 0, 0, 'test.png', 40, '2026-03-08 16:48:02', '2026-03-10 16:24:39', 1, 0, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (141, 4, 0, 0, 'test.png', 40, '2026-03-08 16:49:13', '2026-03-10 16:24:41', 1, 0, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (142, 3, 0, 0, NULL, 41, '2026-03-09 10:45:57', '2026-03-09 10:45:57', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (143, 3, 0, 0, NULL, 41, '2026-03-09 10:45:57', '2026-03-09 10:45:57', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (144, 3, 0, 0, NULL, 41, '2026-03-09 10:45:57', '2026-03-09 10:45:57', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (145, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (146, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (147, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (148, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (149, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (150, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (151, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (152, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (153, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (154, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (155, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (156, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (157, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (158, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (159, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (160, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (161, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (162, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (163, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (164, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (165, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (166, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (167, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (168, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (169, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (170, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (171, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (172, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (173, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (174, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (175, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (176, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (177, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (178, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (179, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (180, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (181, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (182, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (183, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (184, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (185, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (186, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (187, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (188, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (189, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (190, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (191, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (192, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (193, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (194, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (195, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (196, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (197, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (198, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (199, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (200, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (201, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (202, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (203, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (204, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (205, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (206, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (207, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (208, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (209, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (210, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (211, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (212, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (213, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (214, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (215, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (216, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (217, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (218, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (219, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (220, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (221, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (222, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (223, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (224, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (225, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (226, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (227, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (228, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (229, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (230, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (231, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (232, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (233, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (234, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (235, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (236, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (237, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (238, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (239, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (240, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (241, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (242, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (243, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (244, 2, 0, 0, NULL, 42, '2026-03-09 19:54:56', '2026-03-09 19:54:56', NULL, NULL, NULL, NULL);
INSERT INTO `fruit_tree` VALUES (245, 2, 0, 0, NULL, 43, '2026-03-12 16:45:59', '2026-03-12 16:45:59', NULL, NULL, NULL, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水果类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fruit_type
-- ----------------------------
INSERT INTO `fruit_type` VALUES (2, '沙糖桔', '2026-03-05 19:24:37', '2026-03-05 19:28:47');
INSERT INTO `fruit_type` VALUES (3, '沃柑', '2026-03-05 19:24:48', '2026-03-05 19:24:48');
INSERT INTO `fruit_type` VALUES (4, '温州蜜柑', '2026-03-05 19:24:54', '2026-03-05 19:25:39');
INSERT INTO `fruit_type` VALUES (5, '红美人', '2026-03-05 19:26:19', '2026-03-05 19:26:19');

-- ----------------------------
-- Table structure for fruitgroup_fruit
-- ----------------------------
DROP TABLE IF EXISTS `fruitgroup_fruit`;
CREATE TABLE `fruitgroup_fruit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `tree_id` bigint NULL DEFAULT NULL COMMENT '果树id',
  `group_id` bigint NULL DEFAULT NULL COMMENT '果树组id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '果树组与果树关联表' ROW_FORMAT = DYNAMIC;

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
  `create_user` bigint NULL DEFAULT NULL,
  `update_user` bigint NULL DEFAULT NULL,
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
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orchard
-- ----------------------------
INSERT INTO `orchard` VALUES (1, '信息工程果园', 114514, '这是一个超大的果园，里面有着现代科技，可以实现果园->果实->果汁->果脯一键式实现', 1, '1', '2026-01-26 10:03:53', '2026-01-26 14:59:22', 1, 1, '东实训楼');
INSERT INTO `orchard` VALUES (2, '智能制造果园', 1000, '这是一个在圈地运动后出现的一种果园，它使用新的果园模式，就是用工业革命后的内燃机驱动的机械进行果园管理，集成和大小没有信息工程果园，但是已经很接近', 1, '1', '2026-01-26 10:04:53', '2026-01-26 10:04:53', 1, 1, '科实南');
INSERT INTO `orchard` VALUES (3, '航空工程果园', 200000, '这是一个在天上的果园，专门生产太阳果、月亮果与云果，这些果实的树能够以云朵为泥土，水分通过根流到云朵天面形成雨。里面的人有双特异眼，白天橙色，晚上白色对应太阳、月亮颜色', 1, '1', '2026-01-26 10:39:57', '2026-01-26 10:39:57', 1, 1, '科实北');
INSERT INTO `orchard` VALUES (4, '制药工程果园', 500, '这是一个农药自产自销的果园，里面的果树长得飞快，但是这个果园生活节奏非常快，人数和生育率非常多', 1, '1', '2026-01-26 10:47:49', '2026-01-26 10:47:49', 1, 1, '校史馆');
INSERT INTO `orchard` VALUES (5, '建筑工程果园', 300, '这是一个以果树为建筑的果园，管理员住的房子以树干为承重的柱子，以芭蕉作为窗户。生活非常惬意', 1, '1', '2026-01-26 10:50:24', '2026-01-26 10:50:24', 1, 1, '建工学院');
INSERT INTO `orchard` VALUES (7, '农果园', 400, '这是一个以精细化、男耕女织的时代。在管理果园上还在用曲袁犁耕地，用牛做苦力的果园', 2, '1', '2026-01-26 13:48:14', '2026-01-26 13:48:14', 1, 1, '东教5');
INSERT INTO `orchard` VALUES (8, '师范果园', 500, '这是一个以教育著称的果园，里面拥有着一本传男不传女的种植果树秘籍：馗花保点，这本书只有这个果园的原住民才能看懂', 1, '1', '2026-01-26 13:49:06', '2026-01-26 13:49:06', 1, 1, '西教4');
INSERT INTO `orchard` VALUES (9, '医果园', 500, '这是一个比较管理人比较懒散的果园。正是因为他们懒散，并加上他们的智慧，才能在果树有病虫害的时候能够通过他们的精妙手段进行治疗。果子的生长状态在治疗后还能提升', 1, '1', '2026-01-26 13:55:45', '2026-01-26 13:55:45', 1, 1, '西实训楼');
INSERT INTO `orchard` VALUES (10, '商果园', 300, '这里拥有四面八方的果子，这里的人通过贸易把自己的小手工卖出去，买到别的果园的果子。每过一年就会休息5天进行庆祝。祈祷来年顺风顺水', 1, '1', '2026-01-26 14:49:09', '2026-01-26 14:49:09', 1, 1, '商学院');
INSERT INTO `orchard` VALUES (11, '文旅果园', 2000, '这是一个给勇者的果园。这里天气非常恶劣，前一天30度，今天就可能-30度。明天可能就有火山喷发（这里有四座火山），后天可能刮10级台风（在海洋附近，和海洋有一个山脉阻挡），昼夜温差极大。也因此种出的果树能结果的话都会非常好吃', 1, '1', '2026-01-26 14:49:15', '2026-01-26 14:49:15', 1, 1, '西教3');
INSERT INTO `orchard` VALUES (12, '设计果园', 1000, '这是一个专注于雕刻木制家具的果园。每一件这里出产的家具都很有设计感，其特产就是青花木', 1, '1', '2026-01-26 14:50:01', '2026-01-26 14:50:01', 1, 1, '设计学院');
INSERT INTO `orchard` VALUES (13, '武义果园', 999, '这是一个神秘的果园，每一个进入这个果园的人不过多久出来的时候就会发疯，啃掉自己的舌头，每天嘴里都在碎碎念，像是想说什么，但可惜没有舌头。出来后没过多久就会离奇死亡。相传是因为这个果园里的神秘信仰会让人无意识的被同质化，沦为虔诚的信徒。他们给这些进去的信徒都下了诅咒，如果逃出去就会自己咬断舌头，并让他们没过多久就离奇死亡。', 1, '1', '2026-01-26 14:50:08', '2026-01-26 15:07:46', 1, 1, '武义');
INSERT INTO `orchard` VALUES (14, '达芬果园', 100, '测试果园', 1, '0', '2026-01-27 11:05:55', '2026-01-27 11:05:55', NULL, NULL, '达芬');
INSERT INTO `orchard` VALUES (15, '小别尚果园', 100, '测试果园', 1, '0', '2026-01-27 13:37:02', '2026-01-27 13:37:02', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (16, '五马分食果园', 100, '测试果园', 1, '0', '2026-01-27 13:43:19', '2026-01-27 13:43:19', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (17, '四叔果园', 100, '友谊第一，第二比赛，裁判不在就玩赖', 1, '0', '2026-01-27 13:51:36', '2026-01-27 13:51:36', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (18, '夜来香果园', 100, '测试果园成功', 1, '0', '2026-01-27 14:02:45', '2026-01-27 14:02:45', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (19, '91果园', 100, '测试果园成功', 1, '0', '2026-01-27 19:03:13', '2026-01-27 19:03:13', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (20, '离子园', 100, '测试果园成功', 1, '0', '2026-01-27 19:05:05', '2026-01-27 19:05:05', NULL, NULL, NULL);
INSERT INTO `orchard` VALUES (21, '26号守卫果园', 100, '测试果园成功', 1, '0', '2026-01-27 19:05:14', '2026-01-27 19:05:14', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for qr_tree
-- ----------------------------
DROP TABLE IF EXISTS `qr_tree`;
CREATE TABLE `qr_tree`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tree_id` bigint NULL DEFAULT NULL COMMENT '果树id',
  `url` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '二维码url',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 325 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '果树二维码表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qr_tree
-- ----------------------------
INSERT INTO `qr_tree` VALUES (196, 131, 'QRtree/1f8658a08f40445683ea60a255307d7b.png', '2026-03-08 10:26:19');
INSERT INTO `qr_tree` VALUES (197, 132, 'QRtree/96f35f0fe6044afdba614b5d53684f6e.png', '2026-03-08 10:26:19');
INSERT INTO `qr_tree` VALUES (198, 133, 'QRtree/0f2f64b4641c413dade6030064e0bd44.png', '2026-03-08 10:26:19');
INSERT INTO `qr_tree` VALUES (199, 134, 'QRtree/727d00b412044093a610e62323a24d71.png', '2026-03-08 10:26:19');
INSERT INTO `qr_tree` VALUES (200, 135, 'QRtree/85b7f04ef65949ee8d95b01790fba08d.png', '2026-03-08 10:26:19');
INSERT INTO `qr_tree` VALUES (201, 13, 'QRtree/440304becce548758e6e79fb89731ca9.png', '2026-03-08 10:36:02');
INSERT INTO `qr_tree` VALUES (202, 114, 'QRtree/0a49e6ebe7154257aab059e28eed522f.png', '2026-03-08 10:36:02');
INSERT INTO `qr_tree` VALUES (203, 115, 'QRtree/3603531fcb4f418b988299441f004f95.png', '2026-03-08 10:36:02');
INSERT INTO `qr_tree` VALUES (204, 116, 'QRtree/e148a9d93dab4c3bb0c0572455191e22.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (205, 117, 'QRtree/4032ad2a0e7f45ce8e548b733ae0e4ff.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (206, 118, 'QRtree/cefdcddcb7854d4196dbf22f260cd83d.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (207, 119, 'QRtree/75c23b26e31641158be83b9eea0ac95f.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (208, 120, 'QRtree/bc681bf126e34c43b259588964b66dcb.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (209, 121, 'QRtree/9bbed5043baa4ee38981af772db1abdf.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (210, 122, 'QRtree/201c276385204b20960b6532cf2dcca8.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (211, 123, 'QRtree/0503db19e8574df0b528e8b914021e2d.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (212, 124, 'QRtree/bbf0355f08e240c7a8f489ecd2a70a38.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (213, 125, 'QRtree/f14d7ff956f24a1993fb8d771f01b72b.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (214, 126, 'QRtree/a5534ee0c38b4c0cbca8853ea00ac213.png', '2026-03-08 10:36:03');
INSERT INTO `qr_tree` VALUES (215, 136, 'QRtree/c0557ad65c434544888d829f3ccf1fdf.png', '2026-03-08 10:36:39');
INSERT INTO `qr_tree` VALUES (216, 137, 'QRtree/ab81126bc7e94d20a7fab8b190245830.png', '2026-03-08 10:36:39');
INSERT INTO `qr_tree` VALUES (217, 138, 'QRtree/32202bb8354e4612a67bf4eeee4b8cea.png', '2026-03-08 10:36:39');
INSERT INTO `qr_tree` VALUES (218, 127, 'QRtree/99ca5f7148204feb884dfce5b9ee0a39.png', '2026-03-08 14:58:17');
INSERT INTO `qr_tree` VALUES (219, 128, 'QRtree/2c9b348b2d064b8b8174e58fb10f57ff.png', '2026-03-08 14:58:17');
INSERT INTO `qr_tree` VALUES (220, 129, 'QRtree/f329b80ba0d244b097dbd25d67a9bd90.png', '2026-03-08 14:58:17');
INSERT INTO `qr_tree` VALUES (221, 130, 'QRtree/f80df7f900434b7a908f6899f39c2ba4.png', '2026-03-08 14:58:17');
INSERT INTO `qr_tree` VALUES (222, 142, 'QRtree/7d2cb42d5a0b48b8a7eed3a16e3d88cc.png', '2026-03-09 10:46:01');
INSERT INTO `qr_tree` VALUES (223, 143, 'QRtree/3493f9af3dd74ce0b2442458ab12c7bb.png', '2026-03-09 10:46:01');
INSERT INTO `qr_tree` VALUES (224, 144, 'QRtree/a29e1a1a2f014a48b55ebca93c1e0f80.png', '2026-03-09 10:46:01');
INSERT INTO `qr_tree` VALUES (225, 145, 'QRtree/3656e467720b492f8bfabcff30d53b05.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (226, 146, 'QRtree/8cf380ff75844634a6a59b65a64d3d0b.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (227, 147, 'QRtree/fde70e0649214d8789fb1aef358e64c8.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (228, 148, 'QRtree/a2d15732262c4096a61238725102c550.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (229, 149, 'QRtree/783360b9eab940449946376c488ae053.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (230, 150, 'QRtree/77d49866130b41f5ba84d8960b04aed4.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (231, 151, 'QRtree/4ecf9d83e978401b9619a10a76c2818f.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (232, 152, 'QRtree/b640849a3edd413dafba04a39c25de22.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (233, 153, 'QRtree/ed929b778bb8422dbaab844ce4cb3735.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (234, 154, 'QRtree/4b81072e4f7849f18289e0876b8a5b08.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (235, 155, 'QRtree/19ec4589a66d445d8a2d970699929332.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (236, 156, 'QRtree/b7f961b1decc422fa90b03ac0ffd31a5.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (237, 157, 'QRtree/636b1f6c421f4f5684fba072718571b3.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (238, 158, 'QRtree/709f6810e70247f58fd787163a7be5ac.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (239, 159, 'QRtree/b7ae624140f441c3be689b801426f1cd.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (240, 160, 'QRtree/c8841270bad14efab90c5e4ca50a77e9.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (241, 161, 'QRtree/6cf26d8882194c729553a54c6d27d458.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (242, 162, 'QRtree/353d2862d4b84639973b46c6f878709b.png', '2026-03-09 19:55:10');
INSERT INTO `qr_tree` VALUES (243, 163, 'QRtree/b072bccabf1c4bc5bab65cc3bc98de6c.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (244, 164, 'QRtree/f041aa385594412d8de0d258a206c204.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (245, 165, 'QRtree/532fdb74b82e449a83503af7fd5b65e3.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (246, 166, 'QRtree/74f86a00643e43b597b54ba31d92878f.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (247, 167, 'QRtree/a54ad7c1d6484cc0b48eee6d0a2cf759.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (248, 168, 'QRtree/e95c4d2e4ce44ced8fabd7f9fa0219f7.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (249, 169, 'QRtree/ea241d6c39e74099ba6f1849090da229.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (250, 170, 'QRtree/416a1d8a08f848a78b02e8c18181a561.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (251, 171, 'QRtree/7b210fd503a64320bf0c9834b46fd796.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (252, 172, 'QRtree/2e9bc83865a94430870f3407bc69256c.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (253, 173, 'QRtree/37a702313f2c4233b1267e11122a73ab.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (254, 174, 'QRtree/30799cfb70d64421bea2147db1cb7080.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (255, 175, 'QRtree/a3baf03279cb4942af59affd79ee55eb.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (256, 176, 'QRtree/32a598405dd64135817f388b597f8e50.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (257, 177, 'QRtree/c4c084e8909c448688d7e796e9346aec.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (258, 178, 'QRtree/0926bc188ab24634be5f802ea47575d7.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (259, 179, 'QRtree/a72cb17006304d389e87e90c33778310.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (260, 180, 'QRtree/b1d73bf2feae4adcb37be20b550dec5b.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (261, 181, 'QRtree/923fb43491ca49e684b3992ce4bbdfa0.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (262, 182, 'QRtree/6f363eb7d3144afa972ecc075b7e25cf.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (263, 183, 'QRtree/9a520990612b4a6aa21a45fd1bff9f20.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (264, 184, 'QRtree/891f1e23b3da400381292a79834a6a21.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (265, 185, 'QRtree/4e780c453620418fa0bc8a7b26b959d7.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (266, 186, 'QRtree/ebbd1bf93a654aaba54880b748a070ff.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (267, 187, 'QRtree/e6ad7be15e954a5d9ec57e165d55d41e.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (268, 188, 'QRtree/8d9006916efe4cffbdd3ca7f1bc78de2.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (269, 189, 'QRtree/69695dbcc2a04cb7907a1193894a74fc.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (270, 190, 'QRtree/b2b6f9536bae4038b6b16af021a817de.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (271, 191, 'QRtree/c37fe22ac19b46739387a3fe01218b81.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (272, 192, 'QRtree/39ff24ca094748cf9f9dc21d5de630a6.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (273, 193, 'QRtree/bfdbcac10ab3412f8ef247fce1e3576b.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (274, 194, 'QRtree/a5e0396af0dc446bbf338d2130b1d3d7.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (275, 195, 'QRtree/80cbb813d95d4710a603c7f4a014a67b.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (276, 196, 'QRtree/2a89fb7c9e7c431e8224aae23538aa5c.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (277, 197, 'QRtree/cd5cc794e7c94bceb8235476bb9d6b8e.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (278, 198, 'QRtree/f22716243bb9406abd0ee5e5e59e5f6d.png', '2026-03-09 19:55:11');
INSERT INTO `qr_tree` VALUES (279, 199, 'QRtree/5c3031598b3743138073c8e7004ff408.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (280, 200, 'QRtree/9903ebe335a24626b3af7a170c6a6454.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (281, 201, 'QRtree/8cb735eca8634782ae75a6a93db579a1.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (282, 202, 'QRtree/1a4705a30dad49f6a0acd600011e4a9e.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (283, 203, 'QRtree/148fdd69849740739e9e02a03453121e.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (284, 204, 'QRtree/32bf8e2e1ed84b6094d841814866cdf3.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (285, 205, 'QRtree/fb48f92080664da7989df805029866eb.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (286, 206, 'QRtree/acd4102263b64b73aba29afb826aab06.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (287, 207, 'QRtree/ada409ec8db8430882868f292ea0790c.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (288, 208, 'QRtree/49f565aa50e54d12a27e5f3c7ba7daed.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (289, 209, 'QRtree/ea48ee811eca417a8fb3d5859d3482ef.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (290, 210, 'QRtree/8fb1189d1a924c22b2ee8f691912b640.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (291, 211, 'QRtree/aff3c680117b43ed92d27ead7e05583a.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (292, 212, 'QRtree/f184687de80c4008a06bd29e0e3963c0.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (293, 213, 'QRtree/f5f195d5708e4ceb9e60e4993c976424.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (294, 214, 'QRtree/be46414ecab44f4cae59ee01f8a3f398.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (295, 215, 'QRtree/83d885f4ee544344857fcdc3fcc70f34.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (296, 216, 'QRtree/0c7fe6e97ab04eeab5464052633d0b6e.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (297, 217, 'QRtree/3190df52fc2f4f36a745ae27edca7ceb.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (298, 218, 'QRtree/bb160ba613cd46e58b1cdb5e4e64a453.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (299, 219, 'QRtree/737c0ef019994d7a9e97d6f2835bcfff.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (300, 220, 'QRtree/757c005f90f84deaa92ca7e7ef4d7bb3.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (301, 221, 'QRtree/b4133742d3204c26872534da129eb7bb.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (302, 222, 'QRtree/26e77cc83d134b568d73fee8b785060a.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (303, 223, 'QRtree/e85d8861a9b54a5c8fc548cfb346fffe.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (304, 224, 'QRtree/31719b291df446dc89e1ac10215475ce.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (305, 225, 'QRtree/fc44a03e031f40769c8aa2838eb717e6.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (306, 226, 'QRtree/3917fa70dae84af780aa169837aa7426.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (307, 227, 'QRtree/a50f921bd7384e9fb87b180bfcbc864e.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (308, 228, 'QRtree/1734eb49e60647e9bb9a59b11418f723.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (309, 229, 'QRtree/045355fc9a1541b48793e34cf793e3ad.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (310, 230, 'QRtree/e3bd6c4ee84f4e10b2419627eb9839cb.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (311, 231, 'QRtree/9a56e5df3b2b4445b0d7b17ab0fda935.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (312, 232, 'QRtree/4c11b42a814741568cdce30e2db7ec4e.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (313, 233, 'QRtree/b5db0b6541584cc2be0de13eb890e5f3.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (314, 234, 'QRtree/dc56dff620d6433dba36d4f709efceb9.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (315, 235, 'QRtree/5235443cd8a24947941bd35f90563a2e.png', '2026-03-09 19:55:12');
INSERT INTO `qr_tree` VALUES (316, 236, 'QRtree/011c76681d0b4d7cabd3c3617180a202.png', '2026-03-09 19:55:13');
INSERT INTO `qr_tree` VALUES (317, 237, 'QRtree/eb66da408df54bbab8e77caab3930760.png', '2026-03-09 19:55:13');
INSERT INTO `qr_tree` VALUES (318, 238, 'QRtree/5817a75155494cdc990d6f9c2e2cc4c7.png', '2026-03-09 19:55:13');
INSERT INTO `qr_tree` VALUES (319, 239, 'QRtree/471f143009cd4901af5eb567ca87fa17.png', '2026-03-09 19:55:13');
INSERT INTO `qr_tree` VALUES (320, 240, 'QRtree/a7ff4f00286c479b8eec6143c38afd6c.png', '2026-03-09 19:55:13');
INSERT INTO `qr_tree` VALUES (321, 241, 'QRtree/b4462a5bb54743dead00f45d3c1ef43b.png', '2026-03-09 19:55:13');
INSERT INTO `qr_tree` VALUES (322, 242, 'QRtree/6cb02c4045174fddb8b513b638f57d77.png', '2026-03-09 19:55:13');
INSERT INTO `qr_tree` VALUES (323, 243, 'QRtree/e019440ebecf4c3db065246a8da7b71d.png', '2026-03-09 19:55:13');
INSERT INTO `qr_tree` VALUES (324, 244, 'QRtree/23cbb490c3374f1a989551bb1cd823d8.png', '2026-03-09 19:55:13');

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `tree_id` bigint NULL DEFAULT NULL COMMENT '果树id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '上报内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  `create_user` bigint NULL DEFAULT NULL,
  `update_user` bigint NULL DEFAULT NULL,
  `type_id` tinyint NULL DEFAULT NULL COMMENT '上报类型(1水肥，2病虫害，3杂草，4成熟度)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '上报记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of report
-- ----------------------------
INSERT INTO `report` VALUES (8, 3, 1, '果树生长状态良好', '2026-03-09 19:22:02', '2026-03-09 19:22:02', NULL, NULL, NULL);
INSERT INTO `report` VALUES (9, 3, 1, '发现该果树果实出现未知病症，树叶疑是出现炭疽病', '2026-03-09 19:30:11', '2026-03-09 19:30:11', NULL, NULL, NULL);
INSERT INTO `report` VALUES (10, 1, 1, 'test3.11.16:28', '2026-03-11 16:30:48', '2026-03-11 16:30:48', 1, 1, 1);
INSERT INTO `report` VALUES (11, 1, NULL, '我想要吃饭', '2026-03-12 20:08:05', '2026-03-12 20:08:05', NULL, NULL, 4);
INSERT INTO `report` VALUES (12, 1, NULL, '我想要吃饭', '2026-03-12 20:10:08', '2026-03-12 20:10:08', NULL, NULL, 4);
INSERT INTO `report` VALUES (13, 1, 141, '我想要吃饭', '2026-03-12 20:12:06', '2026-03-12 20:12:06', NULL, NULL, 4);
INSERT INTO `report` VALUES (14, 1, 141, '我想要吃饭', '2026-03-12 20:14:01', '2026-03-12 20:14:01', NULL, NULL, 4);
INSERT INTO `report` VALUES (15, 1, 141, '我想要吃饭', '2026-03-12 20:15:51', '2026-03-12 20:15:51', NULL, NULL, 4);
INSERT INTO `report` VALUES (16, 1, 141, '我想要吃饭', '2026-03-12 20:34:26', '2026-03-12 20:34:26', NULL, NULL, 4);
INSERT INTO `report` VALUES (17, 1, 141, '我想要吃饭', '2026-03-12 20:35:28', '2026-03-12 20:35:28', NULL, NULL, 4);
INSERT INTO `report` VALUES (18, 1, 141, '我想要吃饭', '2026-03-12 20:36:31', '2026-03-12 20:36:31', NULL, NULL, 4);
INSERT INTO `report` VALUES (19, 1, 141, '我想要吃饭', '2026-03-12 20:37:53', '2026-03-12 20:37:53', NULL, NULL, 4);
INSERT INTO `report` VALUES (20, 1, 141, '我想要吃饭', '2026-03-12 20:41:31', '2026-03-12 20:41:31', NULL, NULL, 4);
INSERT INTO `report` VALUES (21, 1, 141, '11', '2026-03-12 20:49:33', '2026-03-12 20:49:33', NULL, NULL, 4);
INSERT INTO `report` VALUES (22, 1, 141, '11', '2026-03-12 20:51:58', '2026-03-12 20:51:58', NULL, NULL, 4);
INSERT INTO `report` VALUES (23, 1, 141, '11', '2026-03-12 20:52:57', '2026-03-12 20:52:57', NULL, NULL, 4);
INSERT INTO `report` VALUES (24, 1, 141, '你好', '2026-03-12 20:55:48', '2026-03-12 20:55:48', NULL, NULL, 4);

-- ----------------------------
-- Table structure for report_img
-- ----------------------------
DROP TABLE IF EXISTS `report_img`;
CREATE TABLE `report_img`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片相对路径',
  `report_id` bigint NULL DEFAULT NULL COMMENT '报告id',
  `typeId` bigint NULL DEFAULT NULL COMMENT '类型(1水肥，2病虫害，3杂草，4成熟度)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report_img
-- ----------------------------
INSERT INTO `report_img` VALUES (14, 'ripe/0041668070354cef9da57d247ebb065f.png', 8, 4);
INSERT INTO `report_img` VALUES (15, 'ripe/111526938c3846a292178cc6e2a0c554.png', 8, 4);
INSERT INTO `report_img` VALUES (16, 'weeds/db19e7109f274caebc68470f6c7c396f.jpg', 8, 3);
INSERT INTO `report_img` VALUES (17, 'bc/892db1e29b1148c89b327ef233424781.JPG', 9, 2);
INSERT INTO `report_img` VALUES (18, 'bc/df0d7220ca4848218429cd8547dfee69.jpg', 9, 2);
INSERT INTO `report_img` VALUES (19, 'bc/b43e1be691e04c38a647f29d2768d8a9.jpg', 9, 2);
INSERT INTO `report_img` VALUES (20, 'ripe/d0083918f7f847b3a2511f828dfa7673.png', 23, 4);
INSERT INTO `report_img` VALUES (21, 'ripe/0f5bb6dc940643ceb3d2ebc563b672a6.png', 24, 4);

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `creator_id` bigint NOT NULL COMMENT '创建人id',
  `orchard_id` bigint NOT NULL COMMENT '果园id',
  `area_id` bigint NULL DEFAULT NULL,
  `tree_id` bigint NULL DEFAULT NULL,
  `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务类型',
  `task_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '任务内容',
  `task_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称/标题',
  `group_id` bigint NULL DEFAULT NULL COMMENT '员工组',
  `completion_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `deadline` datetime NULL DEFAULT NULL COMMENT '截止时间',
  `post_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建/发布时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '任务状态',
  `schedule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布计划',
  `create_user` bigint NULL DEFAULT NULL,
  `update_user` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2032013967055994883 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (2029399655948156929, 1, 0, 40, NULL, '0', 'string', 'string', 0, '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', '2026-03-05 01:47:03', NULL, 0, NULL, NULL, NULL);
INSERT INTO `task` VALUES (2029452252792967170, 1, 0, 40, NULL, '0', '测试更新', '现在是第一个title', 0, '2026-03-05 06:48:18', '2026-03-05 06:48:18', '2026-03-05 06:48:18', '2026-03-05 15:01:37', '2026-03-05 15:01:37', 0, NULL, 1, 1);
INSERT INTO `task` VALUES (2030264910188933122, 3, 1, 0, NULL, '0', '扫码上报', '上报', NULL, NULL, '2026-03-08 16:00:00', '2026-03-07 12:50:50', '2026-03-07 20:50:50', '2026-03-07 20:50:50', 0, NULL, NULL, NULL);
INSERT INTO `task` VALUES (2030265017521172482, 3, 1, 0, NULL, '1', 'df', 'ad', NULL, NULL, '2026-03-09 16:00:00', '2026-03-07 12:51:16', '2026-03-07 20:51:15', '2026-03-07 20:51:15', 0, NULL, NULL, NULL);
INSERT INTO `task` VALUES (2030265086903349249, 3, 1, 0, NULL, '0', 'adf', 'adfadf', NULL, NULL, '2026-03-15 16:00:00', '2026-03-07 12:51:32', '2026-03-07 20:51:32', '2026-03-07 20:51:32', 0, NULL, NULL, NULL);
INSERT INTO `task` VALUES (2030443729936019457, 3, 1, 0, NULL, '0', '全部浇水', '浇水', 1, NULL, '2026-03-09 16:00:00', '2026-03-08 00:41:25', '2026-03-08 08:41:24', '2026-03-08 08:41:24', 0, NULL, NULL, NULL);
INSERT INTO `task` VALUES (2030518415164239874, 3, 1, 0, NULL, '0', 'j浇水', 'j浇水', 1, NULL, '2026-03-09 16:00:00', '2026-03-08 05:38:11', '2026-03-08 13:38:10', '2026-03-08 13:38:10', 0, NULL, NULL, NULL);
INSERT INTO `task` VALUES (2030995732865007617, 3, 1, 0, NULL, '1', '测试时间解析yyyy-MM-dd HH:mm:ss.SSS', 'test_time', NULL, NULL, '2026-03-18 00:00:00', '2026-03-09 21:14:52', '2026-03-09 21:14:51', '2026-03-09 21:14:51', 0, NULL, NULL, NULL);
INSERT INTO `task` VALUES (2031181121135325185, 3, 1, 40, NULL, '0', '浇水', '浇水', NULL, NULL, '2026-03-12 00:00:00', '2026-03-10 09:31:32', '2026-03-10 09:31:31', '2026-03-10 09:31:31', 0, NULL, NULL, NULL);
INSERT INTO `task` VALUES (2032013967055994882, 6, 3, 0, NULL, '0', '123', '1', NULL, NULL, '2026-03-16 00:00:00', '2026-03-12 16:40:57', '2026-03-12 16:40:57', '2026-03-12 16:40:57', 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for task_img
-- ----------------------------
DROP TABLE IF EXISTS `task_img`;
CREATE TABLE `task_img`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `task_id` bigint NULL DEFAULT NULL COMMENT '任务id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2030249438294863875 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '任务图片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_img
-- ----------------------------
INSERT INTO `task_img` VALUES (2029399623966588929, 'string', 2029399623857537025);
INSERT INTO `task_img` VALUES (2029399624029503489, 'nmsl', 2029399623857537025);
INSERT INTO `task_img` VALUES (2029399624029503490, 'woshinidie', 2029399623857537025);
INSERT INTO `task_img` VALUES (2029399656078180353, 'string', 2029399655948156929);
INSERT INTO `task_img` VALUES (2029399656078180354, 'nmsl', 2029399655948156929);
INSERT INTO `task_img` VALUES (2029399656145289217, 'woshinidie', 2029399655948156929);
INSERT INTO `task_img` VALUES (2029399676290531331, 'string', 2029399676290531330);
INSERT INTO `task_img` VALUES (2029399676353445889, 'nmsl', 2029399676290531330);
INSERT INTO `task_img` VALUES (2029399676420554754, 'woshinidie', 2029399676290531330);
INSERT INTO `task_img` VALUES (2029400594906021889, 'hiayoushenmedengdai', 2029388840419332097);
INSERT INTO `task_img` VALUES (2029452174023938049, 'string', 0);
INSERT INTO `task_img` VALUES (2029452252792967171, '我就是我', 2029452252792967170);
INSERT INTO `task_img` VALUES (2029452252860076033, '只是一场烟火散落的尘埃', 2029452252792967170);
INSERT INTO `task_img` VALUES (2029452685594808322, '我就是我，只是一场烟火散落的尘埃', NULL);
INSERT INTO `task_img` VALUES (2029452685594808323, '当你的眼睛眯着笑', NULL);
INSERT INTO `task_img` VALUES (2029901741357957122, 'string', 2029901741299236866);
INSERT INTO `task_img` VALUES (2029902217885495297, 'string', 2029902217856135170);
INSERT INTO `task_img` VALUES (2030176453199896578, 'string', 2030176453157953538);
INSERT INTO `task_img` VALUES (2030247678432014338, 'string', 0);
INSERT INTO `task_img` VALUES (2030249438294863873, 'string', 2030249438261309441);
INSERT INTO `task_img` VALUES (2030249438294863874, 'string', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '果树位置表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '任务-果树组关联表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '果树-果树组关联表' ROW_FORMAT = DYNAMIC;

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
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '天气类型（晴、阴、雨等）',
  `wind_direction` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风向',
  `wind_power` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风力',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区',
  `adcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市行政区划代码',
  `weather_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `temperature` double NULL DEFAULT NULL COMMENT '温度',
  `humidity` bigint NULL DEFAULT NULL COMMENT '相对湿度 %',
  `report_time` datetime NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2030521684477407239 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '天气信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of weather
-- ----------------------------
INSERT INTO `weather` VALUES (2029840475994148865, 1, '2026-03-06', '5', '晴', '东北风', '4级', '2026-03-06 16:44:16', '2026-03-06 16:44:16', '浙江省', '金华市', '婺城区', '330702', '100', 15.3, 76, '2026-03-06 16:26:10');
INSERT INTO `weather` VALUES (2030212168411463681, 1, '2026-03-07', '6', '晴', '东北风', '5级', '2026-03-07 17:21:14', '2026-03-07 17:21:14', '浙江省', '金华市', '婺城区', '330702', '100', 13.5, 68, '2026-03-07 16:46:37');
INSERT INTO `weather` VALUES (2030519935570079746, 1, '2026-03-08', '7', '晴', '东北风', '3级', '2026-03-08 13:44:12', '2026-03-08 13:44:12', '浙江省', '金华市', '婺城区', '330702', '100', 16.2, 61, '2026-03-08 13:25:57');
INSERT INTO `weather` VALUES (2030521684477407234, 4, '2026-03-08', '7', '晴', '东北风', '4级', '2026-03-08 15:13:44', '2026-03-08 15:13:44', '浙江省', '金华市', '婺城区', '330702', '100', 15.7, 64, '2026-03-08 14:26:07');
INSERT INTO `weather` VALUES (2030521684477407235, 1, '2026-03-09', '1', '多云', '东北风', '3级', '2026-03-09 08:48:17', '2026-03-09 08:48:17', '浙江省', '金华市', '婺城区', '330702', '101', 13.9, 65, '2026-03-09 07:26:10');
INSERT INTO `weather` VALUES (2030521684477407236, 1, '2026-03-10', '2', '晴', '东北风', '4级', '2026-03-10 16:05:36', '2026-03-10 16:05:36', '浙江省', '金华市', '婺城区', '330702', '100', 13.9, 54, '2026-03-10 15:26:13');
INSERT INTO `weather` VALUES (2030521684477407237, 1, '2026-03-11', '3', '晴', '西风', '1级', '2026-03-11 15:35:32', '2026-03-11 15:35:32', '浙江省', '金华市', '婺城区', '330702', '100', 20, 25, '2026-03-11 15:30:17');
INSERT INTO `weather` VALUES (2030521684477407238, 1, '2026-03-12', '4', '晴', '南风', '1级', '2026-03-12 08:01:19', '2026-03-12 08:01:19', '浙江省', '金华市', '婺城区', '330702', '100', 11, 64, '2026-03-12 07:57:02');

SET FOREIGN_KEY_CHECKS = 1;
