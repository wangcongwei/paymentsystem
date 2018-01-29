/*
Navicat MySQL Data Transfer

Source Server         : costManager
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : paysystem

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2015-09-20 20:56:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_order`
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `BUSORDER_NO` varchar(255) DEFAULT NULL,
  `BUS_DETAIL` varchar(255) DEFAULT NULL,
  `AMOUNT` double DEFAULT NULL,
  `ORDER_NO` varchar(255) DEFAULT NULL,
  `CREATE_TIME` date DEFAULT NULL,
  `OUT_TIME` date DEFAULT NULL,
  `UPDATE_TIME` date DEFAULT NULL,
  `ORDER_STATUS` varchar(255) DEFAULT NULL,
  `BUS_NUM` varchar(255) DEFAULT NULL,
   `CRT_USER_ID` int(11) DEFAULT NULL,
  `CRT_DTTM` datetime DEFAULT NULL,
  `LASTUPT_USER_ID` int(11) DEFAULT NULL,
  `LASTUPT_DTTM` datetime DEFAULT NULL,
  `EXT1` varchar(255) DEFAULT NULL,
  `EXT2` varchar(255) DEFAULT NULL,
  `EXT3` varchar(255) DEFAULT NULL,
  `EXT4` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (1, '123', '保单一', 99, '123', '2015-9-22', '2015-9-23', '2015-9-22', '2', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES ('8', '234', '测试二', 98, '8aff42bfae7e466681cedcae642d6019', '2015-09-20', '2015-09-20', '2015-09-20', 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES ('9', '1233', '测试三', 97, '7c3949ca1b5941dbb64ccd9a3cf48dae', '2015-09-20', '2015-09-20', '2015-09-20', 2, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_order` VALUES ('10', '7867', '测试四', '96', '318019dc696f46288772ce51b7f0f834', '2015-09-20', '2015-09-21', '2015-09-20', 3, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
