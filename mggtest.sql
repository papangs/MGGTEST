/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 100421
 Source Host           : localhost:3306
 Source Schema         : mggtest

 Target Server Type    : MySQL
 Target Server Version : 100421
 File Encoding         : 65001

 Date: 04/01/2023 07:44:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mhost
-- ----------------------------
DROP TABLE IF EXISTS `mhost`;
CREATE TABLE `mhost`  (
  `kd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `host_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `product_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mhost
-- ----------------------------
INSERT INTO `mhost` VALUES ('PDAM_MALANG', 'Pdam Malang Kota', '1234');

-- ----------------------------
-- Table structure for mparam
-- ----------------------------
DROP TABLE IF EXISTS `mparam`;
CREATE TABLE `mparam`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `grp_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `kd_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mparam
-- ----------------------------
INSERT INTO `mparam` VALUES (1, 'PDAM_MALANG', 'url', 'http://27.112.79.212/api.php');
INSERT INTO `mparam` VALUES (2, 'PDAM_MALANG', 'username', 'fortuna');
INSERT INTO `mparam` VALUES (3, 'PDAM_MALANG', 'password', '8234klasue-2348eq-daklier3qkye89e3ci');

-- ----------------------------
-- Table structure for tb_trans
-- ----------------------------
DROP TABLE IF EXISTS `tb_trans`;
CREATE TABLE `tb_trans`  (
  `trxid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `trans_dt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `trans_tm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `retnum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `customer_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `customer_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `amount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fine` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `total` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `additional_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `product_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `merchant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `periode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_trans
-- ----------------------------
INSERT INTO `tb_trans` VALUES ('TRXIDMGG20230104073741025462', '20230104', '073741', '03', '1234567', '03062003526', '03062003526', '03062003526', '809500', '0', '809500', '{\"custno\":\"03062003526\",\"custname\":\"BUDI TISTIANTO\",\"custaddr\":\"Pondok Benda Indah - L 1 \\/ 24 #20213934 -\",\"numperiod\":11,\"periods\":\"202107,202201,202202,202203,202204,202205,202206,202207,202208,202209,202210\",\"used\":88,\"bill\":809500,\"fines\":0,\"total\":809500,\"respcode\":\"00\",\"message\":\"Sukses\"}', '', '1234', '', '202210');

-- ----------------------------
-- Table structure for tb_users
-- ----------------------------
DROP TABLE IF EXISTS `tb_users`;
CREATE TABLE `tb_users`  (
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_users
-- ----------------------------
INSERT INTO `tb_users` VALUES ('mgg', '$2a$10$yOO9xniTzkV2l93dhw4fGOrd.2EtKBBPYdYWc8pSSlpzT4huEouBa');

SET FOREIGN_KEY_CHECKS = 1;
