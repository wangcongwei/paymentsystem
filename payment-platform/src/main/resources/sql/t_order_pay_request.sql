/*
Navicat MySQL Data Transfer

Source Server         : costManager
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : paysystem

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2015-09-20 20:56:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `order_pay_request`
-- ----------------------------
DROP TABLE IF EXISTS `t_order_pay_request`;
CREATE TABLE `t_order_pay_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `AMOUNT` double DEFAULT NULL,
  `ORDER_NO` varchar(255) DEFAULT NULL,
  `CREATE_TIME` date DEFAULT NULL,
  `UPDATE_TIME` date DEFAULT NULL,
  `bus_Num` varchar(255) DEFAULT NULL,
  `pay_SeriNo` varchar(255) DEFAULT NULL,
  `user_Name` varchar(255) DEFAULT NULL,
  `user_CerType` varchar(255) DEFAULT NULL,
  `user_CerNum` varchar(255) DEFAULT NULL,
  `user_Account` varchar(255) DEFAULT NULL,
  `user_Mobile` varchar(255) DEFAULT NULL,
   `CRT_USER_ID` int(11) DEFAULT NULL,
  `CRT_DTTM` datetime DEFAULT NULL,
  `LASTUPT_USER_ID` int(11) DEFAULT NULL,
  `LASTUPT_DTTM` datetime DEFAULT NULL,
   `SMS_CODE` varchar(255) DEFAULT NULL,
  `EXT1` varchar(255) DEFAULT NULL,
  `EXT2` varchar(255) DEFAULT NULL,
  `EXT3` varchar(255) DEFAULT NULL,
  `EXT4` varchar(255) DEFAULT NULL,
  `pay_Request_Status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_pay_request
-- ----------------------------
INSERT INTO `t_order_pay_request` VALUES ('3', '99', '318019dc696f46288772ce51b7f0f834', '2015-09-20', '2015-09-20', '1', '1838e0b9673b421a86980b96cac84b41', null, null, null, null, null, null, null, null, null, null);
