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

 Date: 23/07/2023 22:59:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE database `key`;
use `key`;


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
                           `owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                           `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                           `key_id` int(11) NOT NULL,
                           `create_by_id` int(11) NOT NULL,
                           `key_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2450 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bucket
-- ----------------------------

-- ----------------------------
-- Table structure for cluster
-- ----------------------------
DROP TABLE IF EXISTS `cluster`;
CREATE TABLE `cluster`  (
                            `id` int(10) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
                            `cluster_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                            `cluster_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                            `cluster_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                            `cluster_os` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                            `cluster_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                            `cluster_version` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                            `network_info` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
                            `container_runtime` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                            `region` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                            `key_id` int(11) NOT NULL,
                            `key_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                            `endpoint_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                            `endpoint_security_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cluster
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
                                 `key_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2028 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
                                       `key_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2131 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = MyISAM AUTO_INCREMENT = 1838163982 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of files
-- ----------------------------

-- ----------------------------
-- Table structure for huawei_ecs_region
-- ----------------------------
DROP TABLE IF EXISTS `huawei_ecs_region`;
CREATE TABLE `huawei_ecs_region`  (
                                      `region_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `region_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `endpoint` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `id` int(11) NOT NULL,
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of huawei_ecs_region
-- ----------------------------
INSERT INTO `huawei_ecs_region` VALUES ('华南-广州', 'cn-south-1', 'bms.cn-south-1.myhuaweicloud.com\r', 1);
INSERT INTO `huawei_ecs_region` VALUES ('华东-上海一', 'cn-east-3', 'bms.cn-east-3.myhuaweicloud.com\r', 2);
INSERT INTO `huawei_ecs_region` VALUES ('华东-上海二', 'cn-east-2', 'bms.cn-east-2.myhuaweicloud.com\r', 3);
INSERT INTO `huawei_ecs_region` VALUES ('华北-北京一', 'cn-north-1', 'bms.cn-north-1.myhuaweicloud.com\r', 4);
INSERT INTO `huawei_ecs_region` VALUES ('华北-北京四', 'cn-north-4', 'bms.cn-north-4.myhuaweicloud.com\r', 5);
INSERT INTO `huawei_ecs_region` VALUES ('华北-北京二', 'cn-north-2', 'bms.cn-north-2.myhuaweicloud.com\r', 6);
INSERT INTO `huawei_ecs_region` VALUES ('中国-香港', 'ap-southeast-1', 'ecs.ap-southeast-1.myhuaweicloud.com\r', 7);
INSERT INTO `huawei_ecs_region` VALUES ('中东-阿布扎比-OP5', 'ae-ad-1', 'ecs.ae-ad-1.myhuaweicloud.com\r', 8);
INSERT INTO `huawei_ecs_region` VALUES ('亚太-新加坡', 'ap-southeast-3', 'ecs.ap-southeast-3.myhuaweicloud.com\r', 9);
INSERT INTO `huawei_ecs_region` VALUES ('亚太-曼谷', 'ap-southeast-2', 'ecs.ap-southeast-2.myhuaweicloud.com\r', 10);
INSERT INTO `huawei_ecs_region` VALUES ('西南-贵阳一', 'cn-southwest-2', 'ecs.cn-southwest-2.myhuaweicloud.com\r', 11);
INSERT INTO `huawei_ecs_region` VALUES ('土耳其-伊斯坦布尔', 'tr-west-1', 'ecs.tr-west-1.myhuaweicloud.com\r', 12);
INSERT INTO `huawei_ecs_region` VALUES ('欧洲-都柏林', 'eu-west-101', 'ecs.eu-west-101.myhuaweicloud.com\r', 13);
INSERT INTO `huawei_ecs_region` VALUES ('欧洲-巴黎', 'eu-west-0', 'ecs.eu-west-0.myhuaweicloud.com\r', 14);
INSERT INTO `huawei_ecs_region` VALUES ('拉美-圣地亚哥', 'la-south-2', 'ecs.la-south-2.myhuaweicloud.com\r', 15);
INSERT INTO `huawei_ecs_region` VALUES ('拉美-墨西哥城一', 'na-mexico-1', 'ecs.na-mexico-1.myhuaweicloud.com\r', 16);
INSERT INTO `huawei_ecs_region` VALUES ('华南-深圳', 'cn-south-2', 'ecs.cn-south-2.myhuaweicloud.com\r', 17);
INSERT INTO `huawei_ecs_region` VALUES ('华南-广州-友好用户环境', 'cn-south-4', 'ecs.cn-south-4.myhuaweicloud.com\r', 18);
INSERT INTO `huawei_ecs_region` VALUES ('华南-广州', 'cn-south-1', 'ecs.cn-south-1.myhuaweicloud.com\r', 19);
INSERT INTO `huawei_ecs_region` VALUES ('华东-上海一', 'cn-east-3', 'ecs.cn-east-3.myhuaweicloud.com\r', 20);
INSERT INTO `huawei_ecs_region` VALUES ('华东-上海二', 'cn-east-2', 'ecs.cn-east-2.myhuaweicloud.com\r', 21);
INSERT INTO `huawei_ecs_region` VALUES ('华北-乌兰察布一', 'cn-north-9', 'ecs.cn-north-9.myhuaweicloud.com\r', 22);
INSERT INTO `huawei_ecs_region` VALUES ('华北-北京一', 'cn-north-1', 'ecs.cn-north-1.myhuaweicloud.com\r', 23);
INSERT INTO `huawei_ecs_region` VALUES ('华北-北京四', 'cn-north-4', 'ecs.cn-north-4.myhuaweicloud.com\r', 24);
INSERT INTO `huawei_ecs_region` VALUES ('非洲-约翰内斯堡', 'af-south-1', 'ecs.af-south-1.myhuaweicloud.com\r', 25);
INSERT INTO `huawei_ecs_region` VALUES ('华南-广州-友好用户环境', 'cn-south-4', 'bms.cn-south-4.myhuaweicloud.com\r', 26);
INSERT INTO `huawei_ecs_region` VALUES ('拉美-圣地亚哥', 'la-south-2', 'bms.la-south-2.myhuaweicloud.com\r', 27);
INSERT INTO `huawei_ecs_region` VALUES ('欧洲-巴黎', 'eu-west-0', 'bms.eu-west-0.myhuaweicloud.com\r', 28);
INSERT INTO `huawei_ecs_region` VALUES ('亚太-曼谷', 'ap-southeast-2', 'bms.ap-southeast-2.myhuaweicloud.com\r', 29);
INSERT INTO `huawei_ecs_region` VALUES ('亚太-新加坡', 'ap-southeast-3', 'bms.ap-southeast-3.myhuaweicloud.com\r', 30);
INSERT INTO `huawei_ecs_region` VALUES ('中国-香港', 'ap-southeast-1', 'bms.ap-southeast-1.myhuaweicloud.com\r', 31);

-- ----------------------------
-- Table structure for huawei_obs_region
-- ----------------------------
DROP TABLE IF EXISTS `huawei_obs_region`;
CREATE TABLE `huawei_obs_region`  (
                                      `id` int(11) NULL DEFAULT NULL,
                                      `region_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                                      `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of huawei_obs_region
-- ----------------------------
INSERT INTO `huawei_obs_region` VALUES (1, 'af-south-1', '非洲-约翰内斯堡\r');
INSERT INTO `huawei_obs_region` VALUES (2, 'cn-north-4', '华北-北京四\r');
INSERT INTO `huawei_obs_region` VALUES (3, 'cn-north-1', '华北-北京一\r');
INSERT INTO `huawei_obs_region` VALUES (4, 'cn-north-9', '华北-乌兰察布一\r');
INSERT INTO `huawei_obs_region` VALUES (5, 'cn-east-2', '华东-上海二\r');
INSERT INTO `huawei_obs_region` VALUES (6, 'cn-east-3', '华东-上海一\r');
INSERT INTO `huawei_obs_region` VALUES (7, 'cn-south-1', '华南-广州\r');
INSERT INTO `huawei_obs_region` VALUES (8, 'cn-south-4', '华南-广州-友好用户环境\r');
INSERT INTO `huawei_obs_region` VALUES (9, 'cn-south-2', '华南-深圳\r');
INSERT INTO `huawei_obs_region` VALUES (10, 'la-north-2', '拉美-墨西哥城二\r');
INSERT INTO `huawei_obs_region` VALUES (11, 'la-south-2', '拉美-圣地亚哥\r');
INSERT INTO `huawei_obs_region` VALUES (12, 'eu-west-0', '欧洲-巴黎\r');
INSERT INTO `huawei_obs_region` VALUES (13, 'eu-west-101', '欧洲-都柏林\r');
INSERT INTO `huawei_obs_region` VALUES (14, 'tr-west-1', '土耳其-伊斯坦布尔\r');
INSERT INTO `huawei_obs_region` VALUES (15, 'cn-southwest-2', '西南-贵阳一\r');
INSERT INTO `huawei_obs_region` VALUES (16, 'ap-southeast-2', '亚太-曼谷\r');
INSERT INTO `huawei_obs_region` VALUES (17, 'ap-southeast-3', '亚太-新加坡\r');
INSERT INTO `huawei_obs_region` VALUES (18, 'ap-southeast-4', '亚太-雅加达\r');
INSERT INTO `huawei_obs_region` VALUES (19, 'ap-southeast-1', '中国-香港\r');

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
                             `key_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2640 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
                        `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                        `update_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                        `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE INDEX `keyName`(`name`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 182 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of key
-- ----------------------------
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
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单和权限规则表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (4, 0, 'menu', 'AKSK管理', 'auth/ak', 'ak', 'el-icon-HomeFilled', 'tab', '', '/src/views/backend/auth/ak/index.vue', 0, 'none', '', 99, '1', 1648065864, 1647806112);
INSERT INTO `menu` VALUES (8, 0, 'menu', '账号管理', 'auth/admin', 'user', 'el-icon-UserFilled', 'tab', '', '/src/views/backend/auth/admin/index.vue', 0, 'none', '', 98, '1', 1648067239, 1647549566);
INSERT INTO `menu` VALUES (22, 0, 'menu', '实例列表', 'instance', 'instance', 'fa fa-th-list', 'tab', '', '/src/views/backend/instance/index.vue', 0, 'none', '', 94, '1', 1648255019, 1648049712);
INSERT INTO `menu` VALUES (27, 0, 'menu', '存储桶列表', 'bucket', 'bucket', 'fa fa-group', 'tab', '', '/src/views/backend/bucket/index.vue', 1, 'none', '', 93, '1', 1648067248, 1648051141);
INSERT INTO `menu` VALUES (52, 44, 'menu', '个人资料', 'routine/adminInfo', 'routine/adminInfo', 'fa fa-user', 'tab', '', '/src/views/backend/routine/adminInfo.vue', 1, 'none', '', 86, '1', 1648067229, 1645876529);
INSERT INTO `menu` VALUES (87, 0, 'menu', '集群', 'cluster', 'cluster', 'el-icon-Film', 'tab', '', '/src/views/backend/cluster/index.vue', 1, 'none', '', 0, '1', NULL, NULL);
INSERT INTO `menu` VALUES (88, 0, 'menu', '控制台用户', 'consoleUser', 'console', 'fa fa-expeditedssl', 'tab', '', '/src/views/backend/consoleUser/index.vue', 1, 'none', '', 0, '1', NULL, NULL);
INSERT INTO `menu` VALUES (89, 0, 'menu', '数据库列表', 'mysql', 'databases', 'fa fa-th-list', 'tab', '', '/src/views/backend/mysql/index.vue', 1, 'none', '', 94, '1', 1648255019, 1648049712);
INSERT INTO `menu` VALUES (90, 0, 'menu', '文件下载列表', 'files', 'files', 'fa fa-th-list', 'tab', '', '/src/views/backend/file/index.vue', 1, 'none', '', 93, '1', 1648255019, 1648049712);
INSERT INTO `menu` VALUES (91, 0, 'menu', '个人资料', 'routine/adminInfo', 'routine/adminInfo', 'fa fa-user', 'tab', '', '/src/views/backend/routine/adminInfo.vue', 1, 'none', '', 0, '1', NULL, NULL);
INSERT INTO `menu` VALUES (92, 0, 'menu', '导入key列表', 'dashboard/dashboard', 'dashboard', 'fa fa-dashboard', 'tab', '', '/src/views/backend/dashboard.vue', 1, 'none', '', 0, '1', NULL, NULL);

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
                         `key_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 101053 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

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
