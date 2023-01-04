/*
 Navicat Premium Data Transfer

 Source Server         : Localhost
 Source Server Type    : MySQL
 Source Server Version : 50625
 Source Host           : localhost:3306
 Source Schema         : mggtest

 Target Server Type    : MySQL
 Target Server Version : 50625
 File Encoding         : 65001

 Date: 04/01/2023 11:31:37
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mhost
-- ----------------------------
INSERT INTO `mhost` VALUES ('PDAM_MALANG', 'Pdam Malang Kota', '1234');

-- ----------------------------
-- Table structure for mparam
-- ----------------------------
DROP TABLE IF EXISTS `mparam`;
CREATE TABLE `mparam`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `grp_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `kd_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mparam
-- ----------------------------
INSERT INTO `mparam` VALUES (1, 'PDAM_MALANG', 'url', 'dzav7aRB9FM7TtdTIsdrbxfWyK3tw7j0OaLqTtFr+xLXMQ+f2fyGNg==');
INSERT INTO `mparam` VALUES (2, 'PDAM_MALANG', 'username', 'KwF5hG9voexgvjxYqDVPAA==');
INSERT INTO `mparam` VALUES (3, 'PDAM_MALANG', 'password', 'bGoYNnn1SWlbDT36qk9znsw4wgHk7hZuQ/muRIkWzKln+7xdTnBh8MVaxpfUDQsA');

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
  `response_inquiry` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `additional_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `product_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `merchant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `periode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `response_payment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_trans
-- ----------------------------
INSERT INTO `tb_trans` VALUES ('TRXIDMGG20230104093924556932', '20230104', '093924', '03', '1234567', '03062003528', 'HERMIN ASTUTI', 'Pondok Benda Indah - L 1 / 20 #20214227 -', '725700', '0', '725700', '{\"custno\":\"03062003528\",\"custname\":\"HERMIN ASTUTI\",\"custaddr\":\"Pondok Benda Indah - L 1 \\/ 20 #20214227 -\",\"numperiod\":12,\"periods\":\"202111,202112,202201,202202,202203,202204,202205,202206,202207,202208,202209,202210\",\"used\":69,\"bill\":725700,\"fines\":0,\"total\":725700,\"respcode\":\"00\",\"message\":\"Sukses\"}', '', '1234', '', '202210', NULL);
INSERT INTO `tb_trans` VALUES ('TRXIDMGG20230104111030147930', '20230104', '111030', '03', '1234567', '03062003526', 'BUDI TISTIANTO', 'Pondok Benda Indah - L 1 / 24 #20213934 -', '809500', '0', '809500', '{\"custno\":\"03062003526\",\"custname\":\"BUDI TISTIANTO\",\"custaddr\":\"Pondok Benda Indah - L 1 \\/ 24 #20213934 -\",\"numperiod\":11,\"periods\":\"202107,202201,202202,202203,202204,202205,202206,202207,202208,202209,202210\",\"used\":88,\"bill\":809500,\"fines\":0,\"total\":809500,\"respcode\":\"00\",\"message\":\"Sukses\"}', '', '1234', '', '202210', NULL);
INSERT INTO `tb_trans` VALUES ('TRXIDMGG20230104112806526037', '20230104', '112828', '00', '1234567', '03062003521', 'HERMAN L. ACHFAS', 'Pondok Benda Indah - N 1 / 12 #20213187', '50000', '0', '50000', '{\"custno\":\"03062003521\",\"custname\":\"HERMAN L. ACHFAS\",\"custaddr\":\"Pondok Benda Indah - N 1 \\/ 12 #20213187\",\"numperiod\":2,\"periods\":\"202209,202210\",\"used\":0,\"bill\":50000,\"fines\":0,\"total\":50000,\"respcode\":\"00\",\"message\":\"Sukses\"}', '', '1234', '', '202210', '{\"status\":true,\"respcode\":\"00\",\"message\":\"Sukses\"}');
INSERT INTO `tb_trans` VALUES ('TRXIDMGG20230104112901306843', '20230104', '112921', '00', '1234567', '03062003522', 'SOLEH (A)', 'Pondok Benda Indah - T 9 #20214222 -', '260200', '0', '260200', '{\"custno\":\"03062003522\",\"custname\":\"SOLEH (A)\",\"custaddr\":\"Pondok Benda Indah - T 9 #20214222 -\",\"numperiod\":1,\"periods\":\"202210\",\"used\":34,\"bill\":260200,\"fines\":0,\"total\":260200,\"respcode\":\"00\",\"message\":\"Sukses\"}', '', '1234', '', '202210', '{\"status\":true,\"respcode\":\"00\",\"message\":\"Sukses\"}');

-- ----------------------------
-- Table structure for tb_users
-- ----------------------------
DROP TABLE IF EXISTS `tb_users`;
CREATE TABLE `tb_users`  (
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_users
-- ----------------------------
INSERT INTO `tb_users` VALUES ('mgg', '$2a$10$yOO9xniTzkV2l93dhw4fGOrd.2EtKBBPYdYWc8pSSlpzT4huEouBa');

SET FOREIGN_KEY_CHECKS = 1;
