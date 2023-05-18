/*
 Navicat Premium Data Transfer

 Source Server         : ak-sk
 Source Server Type    : MySQL
 Source Server Version : 50726 (5.7.26)
 Source Host           : localhost:3306
 Source Schema         : ak_sk

 Target Server Type    : MySQL
 Target Server Version : 50726 (5.7.26)
 File Encoding         : 65001

 Date: 19/05/2023 01:15:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE `key`;
use `key`;

-- ----------------------------
-- Table structure for bucket
-- ----------------------------
DROP TABLE IF EXISTS `bucket`;
CREATE TABLE `bucket`  (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `region` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                           `end_point` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                           `owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                           `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                           `key_id` int(11) NOT NULL,
                           `create_by_id` int(11) NOT NULL,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2229 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------

-- ----------------------------
-- Table structure for console_user
-- ----------------------------
DROP TABLE IF EXISTS `console_user`;
CREATE TABLE `console_user`  (
                                 `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                 `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `ownerUin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `uin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `key_id` int(11) NOT NULL,
                                 `loginurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 `key_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2020 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of console_user
-- ----------------------------

-- ----------------------------
-- Table structure for databases_instance
-- ----------------------------
DROP TABLE IF EXISTS `databases_instance`;
CREATE TABLE `databases_instance`  (
                                       `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
                                       `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       `instance_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       `domain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       `region` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       `port` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       `key_id` int(11) NOT NULL,
                                       `user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       `whitelist` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2097 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of databases_instance
-- ----------------------------

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS `files`;
CREATE TABLE `files`  (
                          `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
                          `user_id` int(11) NOT NULL,
                          `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          `tpye` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          `original_file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          `file_size` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1838163979 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------

-- ----------------------------
-- Table structure for instance
-- ----------------------------
DROP TABLE IF EXISTS `instance`;
CREATE TABLE `instance`  (
                             `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
                             `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                             `key_id` int(11) NOT NULL,
                             `region` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                             `is_command` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                             `private_key` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
                             `public_key` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
                             `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                             `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                             `original_key_pair` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                             `os_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2521 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------

-- ----------------------------
-- Table structure for key
-- ----------------------------
DROP TABLE IF EXISTS `key`;
CREATE TABLE `key`  (
                        `secretId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `secretKey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `create_by_id` int(11) NOT NULL,
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `token` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
                        `is_temporary` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                        `expiration_time` datetime NULL DEFAULT NULL,
                        `bucket_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                        `task_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                        `create_time` datetime NULL DEFAULT NULL,
                        `update_time` datetime NULL DEFAULT NULL,
                        `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE INDEX `keyName`(`name`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 168 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of key
-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
                         `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
                         `pid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级菜单',
                         `type` enum('menu_dir','menu','button') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'menu' COMMENT '类型:menu_dir=菜单目录,menu=菜单项,button=页面按钮',
                         `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标题',
                         `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '规则名称',
                         `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '路由路径',
                         `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '图标',
                         `menu_type` enum('tab','link','iframe') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单类型:tab=选项卡,link=链接,iframe=Iframe',
                         `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Url',
                         `component` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '组件路径',
                         `keepalive` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '缓存:0=关闭,1=开启',
                         `extend` enum('none','add_rules_only','add_menu_only') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'none' COMMENT '扩展属性:none=无,add_rules_only=只添加为路由,add_menu_only=只添加为菜单',
                         `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
                         `weigh` int(10) NOT NULL DEFAULT 0 COMMENT '权重(排序)',
                         `status` enum('1','0') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '状态:0=禁用,1=启用',
                         `updatetime` int(10) NULL DEFAULT NULL COMMENT '更新时间',
                         `createtime` int(10) NULL DEFAULT NULL COMMENT '创建时间',
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `pid`(`pid`) USING BTREE,
                         INDEX `weigh`(`weigh`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单和权限规则表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (4, 0, 'menu', 'AKSK管理', 'auth/ak', 'ak', 'el-icon-HomeFilled', 'tab', '', '/src/views/backend/auth/ak/index.vue', 0, 'none', '', 99, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (8, 0, 'menu', '账号管理', 'auth/admin', 'user', 'el-icon-UserFilled', 'tab', '', '/src/views/backend/auth/admin/index.vue', 0, 'none', '', 98, '1', 1648067239, 1647549566);
INSERT INTO `menu` VALUES (22, 0, 'menu', '实例列表', 'instance', 'instance', 'fa fa-th-list', 'tab', '', '/src/views/backend/instance/index.vue', 0, 'none', '', 94, '1', 1648255019, 1648049712);
INSERT INTO `menu` VALUES (27, 0, 'menu', '存储桶列表', 'bucket', 'bucket', 'fa fa-group', 'tab', '', '/src/views/backend/bucket/index.vue', 1, 'none', '', 93, '1', 1648067248, 1648051141);
INSERT INTO `menu` VALUES (52, 44, 'menu', '个人资料', 'routine/adminInfo', 'routine/adminInfo', 'fa fa-user', 'tab', '', '/src/views/backend/routine/adminInfo.vue', 1, 'none', '', 86, '1', 1648067229, 1645876529);
INSERT INTO `menu` VALUES (88, 0, 'menu', '控制台用户', 'consoleUser', 'console', 'fa fa-expeditedssl', 'tab', '', '/src/views/backend/consoleUser/index.vue', 1, 'none', '', 0, '1', NULL, NULL);
INSERT INTO `menu` VALUES (89, 0, 'menu', '数据库列表', 'mysql', 'databases', 'fa fa-th-list', 'tab', '', '/src/views/backend/mysql/index.vue', 1, 'none', '', 94, '1', 1648255019, 1648049712);
INSERT INTO `menu` VALUES (90, 0, 'menu', '文件下载列表', 'files', 'files', 'fa fa-th-list', 'tab', '', '/src/views/backend/file/index.vue', 1, 'none', '', 93, '1', 1648255019, 1648049712);
INSERT INTO `menu` VALUES (91, 0, 'menu', '个人资料', 'routine/adminInfo', 'routine/adminInfo', 'fa fa-user', 'tab', '', '/src/views/backend/routine/adminInfo.vue', 1, 'none', '', 0, '1', NULL, NULL);

-- ----------------------------
-- Table structure for menu2
-- ----------------------------
DROP TABLE IF EXISTS `menu2`;
CREATE TABLE `menu2`  (
                          `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
                          `pid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '上级菜单',
                          `type` enum('menu_dir','menu','button') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'menu' COMMENT '类型:menu_dir=菜单目录,menu=菜单项,button=页面按钮',
                          `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标题',
                          `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '规则名称',
                          `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '路由路径',
                          `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '图标',
                          `menu_type` enum('tab','link','iframe') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单类型:tab=选项卡,link=链接,iframe=Iframe',
                          `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Url',
                          `component` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '组件路径',
                          `keepalive` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '缓存:0=关闭,1=开启',
                          `extend` enum('none','add_rules_only','add_menu_only') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'none' COMMENT '扩展属性:none=无,add_rules_only=只添加为路由,add_menu_only=只添加为菜单',
                          `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注',
                          `weigh` int(10) NOT NULL DEFAULT 0 COMMENT '权重(排序)',
                          `status` enum('1','0') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '状态:0=禁用,1=启用',
                          `updatetime` int(10) NULL DEFAULT NULL COMMENT '更新时间',
                          `createtime` int(10) NULL DEFAULT NULL COMMENT '创建时间',
                          PRIMARY KEY (`id`) USING BTREE,
                          INDEX `pid`(`pid`) USING BTREE,
                          INDEX `weigh`(`weigh`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单和权限规则表' ROW_FORMAT = DYNAMIC;

-- ----------------------------

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                         `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                         `user_id` int(11) NOT NULL,
                         `bucket` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 101017 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
                         `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `nike` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                         `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                         `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                         `status` int(255) UNSIGNED NULL DEFAULT 1,
                         PRIMARY KEY (`id`) USING BTREE,
                         UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (15, 'admin', 'admin123', 'admin', 'asd@qq.com', '18899009098', 1);

SET FOREIGN_KEY_CHECKS = 1;
