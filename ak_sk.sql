/*
 Navicat Premium Data Transfer

 Source Server         : ak-sk
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : ak_sk

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 24/04/2023 11:06:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = MyISAM AUTO_INCREMENT = 82 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bucket
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 67 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of databases_instance
-- ----------------------------
INSERT INTO `databases_instance` VALUES (66, 'rm-bp19eo4sreo559257', 'rm-bp19eo4sreo559257', '', 'cn-hangzhou', '0', 98, NULL, NULL, 'MySQL');

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 446 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of instance
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 99 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of key
-- ----------------------------
INSERT INTO `key` VALUES ('LTAI5tCDaSyN8Ti469ig4JmA', 'fBaer7haylKgoXwiHW8dfNBPObqR0H', 15, 98, 'AliYun', '', NULL, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 90 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单和权限规则表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (3, 0, 'menu', 'AKSK管理', 'auth/ak', 'ak', 'fa fa-group', 'tab', '', '/src/views/backend/auth/ak/index.vue', 1, 'none', '', 99, '1', 1648162157, 1646927597);
INSERT INTO `menu` VALUES (4, 3, 'button', '查看', 'auth/group/index', '', '', NULL, '', '', 0, 'none', '', 99, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (5, 3, 'button', '添加', 'auth/group/add', '', '', NULL, '', '', 0, 'none', '', 99, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (6, 3, 'button', '编辑', 'auth/group/edit', '', '', NULL, '', '', 0, 'none', '', 99, '1', 1648065864, 1647806129);
INSERT INTO `menu` VALUES (7, 3, 'button', '删除', 'auth/group/del', '', '', NULL, '', '', 0, 'none', '', 99, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (8, 0, 'menu', '账号管理', 'auth/admin', 'user', 'el-icon-UserFilled', 'tab', '', '/src/views/backend/auth/admin/index.vue', 1, 'none', '', 98, '1', 1648067239, 1647549566);
INSERT INTO `menu` VALUES (9, 8, 'button', '查看', 'auth/admin/index', '', '', NULL, '', '', 0, 'none', '', 98, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (10, 8, 'button', '添加', 'auth/admin/add', '', '', NULL, '', '', 0, 'none', '', 98, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (11, 8, 'button', '编辑', 'auth/admin/edit', '', '', NULL, '', '', 0, 'none', '', 98, '1', 1648065864, 1647806129);
INSERT INTO `menu` VALUES (12, 8, 'button', '删除', 'auth/admin/del', '', '', NULL, '', '', 0, 'none', '', 98, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (21, 0, 'menu_dir', '服务器管理', 'user', 'user', 'fa fa-drivers-license', NULL, '', '', 0, 'none', '', 95, '1', 1648947448, 1648049553);
INSERT INTO `menu` VALUES (22, 0, 'menu', '实例列表', 'instance', 'instance', 'fa fa-th-list', 'tab', '', '/src/views/backend/instance/index.vue', 1, 'none', '', 94, '1', 1648255019, 1648049712);
INSERT INTO `menu` VALUES (23, 22, 'button', '查看', 'user/user/index', '', '', NULL, '', '', 0, 'none', '', 94, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (24, 22, 'button', '添加', 'user/user/add', '', '', NULL, '', '', 0, 'none', '', 94, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (25, 22, 'button', '编辑', 'user/user/edit', '', '', NULL, '', '', 0, 'none', '', 94, '1', 1648065864, 1647806129);
INSERT INTO `menu` VALUES (26, 22, 'button', '删除', 'user/user/del', '', '', NULL, '', '', 0, 'none', '', 94, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (27, 0, 'menu', '存储桶列表', 'bucket', 'bucket', 'fa fa-group', 'tab', '', '/src/views/backend/bucket/index.vue', 1, 'none', '', 93, '1', 1648067248, 1648051141);
INSERT INTO `menu` VALUES (28, 27, 'button', '查看', 'user/group/index', '', '', NULL, '', '', 0, 'none', '', 93, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (29, 27, 'button', '添加', 'user/group/add', '', '', NULL, '', '', 0, 'none', '', 93, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (30, 27, 'button', '编辑', 'user/group/edit', '', '', NULL, '', '', 0, 'none', '', 93, '1', 1648065864, 1647806129);
INSERT INTO `menu` VALUES (31, 27, 'button', '删除', 'user/group/del', '', '', NULL, '', '', 0, 'none', '', 93, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (33, 32, 'button', '查看', 'user/rule/index', '', '', NULL, '', '', 0, 'none', '', 92, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (34, 32, 'button', '添加', 'user/rule/add', '', '', NULL, '', '', 0, 'none', '', 92, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (35, 32, 'button', '编辑', 'user/rule/edit', '', '', NULL, '', '', 0, 'none', '', 92, '1', 1648065864, 1647806129);
INSERT INTO `menu` VALUES (36, 32, 'button', '删除', 'user/rule/del', '', '', NULL, '', '', 0, 'none', '', 92, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (37, 32, 'button', '快速排序', 'user/rule/sortable', '', '', NULL, '', '', 0, 'none', '', 92, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (39, 38, 'button', '查看', 'user/moneyLog/index', '', '', NULL, '', '', 0, 'none', '', 91, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (40, 38, 'button', '添加', 'user/moneyLog/add', '', '', NULL, '', '', 0, 'none', '', 91, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (42, 41, 'button', '查看', 'user/scoreLog/index', '', '', NULL, '', '', 0, 'none', '', 90, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (43, 41, 'button', '添加', 'user/scoreLog/add', '', '', NULL, '', '', 0, 'none', '', 90, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (46, 45, 'button', '查看', 'routine/config/index', '', '', NULL, '', '', 0, 'none', '', 88, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (47, 45, 'button', '编辑', 'routine/config/edit', '', '', NULL, '', '', 0, 'none', '', 88, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (48, 44, 'menu', '附件管理', 'routine/attachment', 'routine/attachment', 'fa fa-folder', 'tab', '', '/src/views/backend/routine/attachment/index.vue', 1, 'none', 'remark_text', 87, '1', 1648067228, 1647105410);
INSERT INTO `menu` VALUES (49, 48, 'button', '查看', 'routine/attachment/index', '', '', NULL, '', '', 0, 'none', '', 87, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (50, 48, 'button', '编辑', 'routine/attachment/edit', '', '', NULL, '', '', 0, 'none', '', 87, '1', 1648065864, 1647806129);
INSERT INTO `menu` VALUES (51, 48, 'button', '删除', 'routine/attachment/del', '', '', NULL, '', '', 0, 'none', '', 87, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (52, 44, 'menu', '个人资料', 'routine/adminInfo', 'routine/adminInfo', 'fa fa-user', 'tab', '', '/src/views/backend/routine/adminInfo.vue', 1, 'none', '', 86, '1', 1648067229, 1645876529);
INSERT INTO `menu` VALUES (53, 52, 'button', '查看', 'routine/adminInfo/index', '', '', NULL, '', '', 0, 'none', '', 86, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (54, 52, 'button', '编辑', 'routine/adminInfo/edit', '', '', NULL, '', '', 0, 'none', '', 86, '1', 1648065864, 1647806129);
INSERT INTO `menu` VALUES (57, 56, 'button', '查看', 'security/dataRecycleLog/index', '', '', NULL, '', '', 0, 'none', '', 84, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (58, 56, 'button', '删除', 'security/dataRecycleLog/del', '', '', NULL, '', '', 0, 'none', '', 84, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (59, 56, 'button', '还原', 'security/dataRecycleLog/restore', '', '', NULL, '', '', 0, 'none', '', 84, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (60, 56, 'button', '查看详情', 'security/dataRecycleLog/info', '', '', NULL, '', '', 0, 'none', '', 84, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (61, 55, 'menu', '敏感数据修改记录', 'security/sensitiveDataLog', 'security/sensitiveDataLog', 'fa fa-expeditedssl', 'tab', '', '/src/views/backend/security/sensitiveDataLog/index.vue', 1, 'none', '', 83, '1', 1649112262, 1649059604);
INSERT INTO `menu` VALUES (62, 61, 'button', '查看', 'security/sensitiveDataLog/index', '', '', NULL, '', '', 0, 'none', '', 83, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (63, 61, 'button', '删除', 'security/sensitiveDataLog/del', '', '', NULL, '', '', 0, 'none', '', 83, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (64, 61, 'button', '回滚', 'security/sensitiveDataLog/rollback', '', '', NULL, '', '', 0, 'none', '', 83, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (65, 61, 'button', '查看详情', 'security/sensitiveDataLog/info', '', '', NULL, '', '', 0, 'none', '', 83, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (66, 55, 'menu', '数据回收规则管理', 'security/dataRecycle', 'security/dataRecycle', 'fa fa-database', 'tab', '', '/src/views/backend/security/dataRecycle/index.vue', 1, 'none', '在此定义需要回收的数据，实现数据自动统一回收', 82, '1', 1651603319, 1648948215);
INSERT INTO `menu` VALUES (67, 66, 'button', '查看', 'security/dataRecycle/index', '', '', NULL, '', '', 0, 'none', '', 82, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (68, 66, 'button', '添加', 'security/dataRecycle/add', '', '', NULL, '', '', 0, 'none', '', 82, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (69, 66, 'button', '编辑', 'security/dataRecycle/edit', '', '', NULL, '', '', 0, 'none', '', 82, '1', 1648065864, 1647806129);
INSERT INTO `menu` VALUES (70, 66, 'button', '删除', 'security/dataRecycle/del', '', '', NULL, '', '', 0, 'none', '', 82, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (71, 55, 'menu', '敏感字段规则管理', 'security/sensitiveData', 'security/sensitiveData', 'fa fa-expeditedssl', 'tab', '', '/src/views/backend/security/sensitiveData/index.vue', 1, 'none', '在此定义需要保护的敏感字段，随后系统将自动监听该字段的修改操作，并提供了敏感字段的修改回滚功能', 81, '1', 1649112263, 1649005119);
INSERT INTO `menu` VALUES (72, 71, 'button', '查看', 'security/sensitiveData/index', '', '', NULL, '', '', 0, 'none', '', 81, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (73, 71, 'button', '添加', 'security/sensitiveData/add', '', '', NULL, '', '', 0, 'none', '', 81, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (74, 71, 'button', '编辑', 'security/sensitiveData/edit', '', '', NULL, '', '', 0, 'none', '', 81, '1', 1648065864, 1647806129);
INSERT INTO `menu` VALUES (75, 71, 'button', '删除', 'security/sensitiveData/del', '', '', NULL, '', '', 0, 'none', '', 81, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (77, 45, 'button', '添加', 'routine/config/add', '', '', NULL, '', '', 0, 'none', '', 88, '1', 1655375826, 1655375812);
INSERT INTO `menu` VALUES (79, 78, 'button', '查看', 'moduleStore/moduleStore/index', '', '', NULL, '', '', 0, 'none', '', 1, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (80, 78, 'button', '安装', 'moduleStore/moduleStore/install', '', '', NULL, '', '', 0, 'none', '', 2, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (81, 78, 'button', '调整状态', 'moduleStore/moduleStore/changeState', '', '', NULL, '', '', 0, 'none', '', 3, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (82, 78, 'button', '卸载', 'moduleStore/moduleStore/uninstall', '', '', NULL, '', '', 0, 'none', '', 4, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (83, 78, 'button', '更新', 'moduleStore/moduleStore/update', '', '', NULL, '', '', 0, 'none', '', 5, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (85, 84, 'button', '查看', 'crud/crud/index', '', '', NULL, '', '', 0, 'none', '', 3, '1', 1668848809, 1668848770);
INSERT INTO `menu` VALUES (86, 84, 'button', '生成', 'crud/crud/generate', '', '', NULL, '', '', 0, 'none', '', 2, '1', 1668848809, 1668848770);
INSERT INTO `menu` VALUES (87, 84, 'button', '删除', 'crud/crud/delete', '', '', NULL, '', '', 0, 'none', '', 1, '1', 1668848921, 1668848921);
INSERT INTO `menu` VALUES (88, 0, 'menu', '控制台用户', 'consoleUser', 'console', 'fa fa-expeditedssl', 'tab', '', '/src/views/backend/consoleUser/index.vue', 1, 'none', '', 0, '1', NULL, NULL);
INSERT INTO `menu` VALUES (89, 0, 'menu', '数据库列表', 'mysql', 'databases', 'fa fa-th-list', 'tab', '', '/src/views/backend/mysql/index.vue', 1, 'none', '', 94, '1', 1648255019, 1648049712);

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
-- Records of menu2
-- ----------------------------
INSERT INTO `menu2` VALUES (1, 0, 'menu', '控制台', 'dashboard/dashboard', 'dashboard', 'fa fa-dashboard', 'tab', '', '/src/views/backend/dashboard.vue', 1, 'none', 'remark_text', 999, '1', 1651926966, 1646889188);
INSERT INTO `menu2` VALUES (2, 0, 'menu_dir', '权限管理', 'auth', 'auth', 'fa fa-group', NULL, '', '', 0, 'none', '', 100, '1', 1648948034, 1645876529);
INSERT INTO `menu2` VALUES (3, 2, 'menu', '角色组管理', 'auth/group', 'auth/group', 'fa fa-group', 'tab', '', '/src/views/backend/auth/group/index.vue', 1, 'none', '', 99, '1', 1648162157, 1646927597);
INSERT INTO `menu2` VALUES (4, 3, 'button', '查看', 'auth/group/index', '', '', NULL, '', '', 0, 'none', '', 99, '1', 1648065864, 1647806112);
INSERT INTO `menu2` VALUES (5, 3, 'button', '添加', 'auth/group/add', '', '', NULL, '', '', 0, 'none', '', 99, '1', 1648065864, 1647806112);
INSERT INTO `menu2` VALUES (8, 2, 'menu', '管理员管理', 'auth/admin', 'auth/admin', 'el-icon-UserFilled', 'tab', '', '/src/views/backend/auth/admin/index.vue', 1, 'none', '', 98, '1', 1648067239, 1647549566);
INSERT INTO `menu2` VALUES (12, 8, 'button', '删除', 'auth/admin/del', '', '', NULL, '', '', 0, 'none', '', 98, '1', 1648065864, 1647806112);

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
) ENGINE = MyISAM AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (15, 'admin', 'admin123', 'admin', NULL, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
