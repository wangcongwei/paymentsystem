/*
Navicat MySQL Data Transfer

Source Server         : paysystem
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : paysystem

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2015-09-25 15:49:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_pay_platform_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `t_pay_platform_transaction`;
CREATE TABLE `t_pay_platform_transaction` (
  `ID` int(19) NOT NULL AUTO_INCREMENT,
  `CREATE_DATE` datetime DEFAULT NULL,
  `USER_TRANSACTION_NO` varchar(40) DEFAULT NULL,
  `REQNO` varchar(40) DEFAULT NULL,
  `RESNO` varchar(40) DEFAULT NULL,
  `ORDER_NO` varchar(20) DEFAULT NULL,
  `CHECK_NO` varchar(20) DEFAULT NULL,
  `AMOUNT` decimal(16,2) DEFAULT NULL,
  `PAYAMOUNT` decimal(16,2) DEFAULT NULL,
  `CURRENCY` varchar(20) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `SOURCE` varchar(20) DEFAULT NULL,
  `PAYER_CODE` varchar(20) DEFAULT NULL,
  `PAY_TYPE` varchar(20) DEFAULT NULL,
  `PAY_TIME` datetime DEFAULT NULL,
  `RETURN_TIME` datetime DEFAULT NULL,
  `PAYSUCCESS_TIME` datetime DEFAULT NULL,
  `SUB_MERCHANT_ID` datetime DEFAULT NULL,
  `ACCOUNT_NO` varchar(25) DEFAULT NULL,
  `ACCOUNT_NAME` varchar(30) DEFAULT NULL,
  `VALIDDATE` varchar(6) DEFAULT NULL,
  `CCV2` varchar(5) DEFAULT NULL,
  `ID_TYPE` varchar(5) DEFAULT NULL,
  `ID_NO` varchar(30) DEFAULT NULL,
  `STORABLECARDNO` varchar(30) DEFAULT NULL,
  `CARDORG` varchar(50) DEFAULT NULL,
  `ISSUER` varchar(50) DEFAULT NULL,
  `MOBILE` varchar(20) DEFAULT NULL,
  `VERIFYCODE` varchar(10) DEFAULT NULL,
  `TOKEN` varchar(10) DEFAULT NULL,
  `MESSAGECODE` varchar(10) DEFAULT NULL,
  `MESSAGE` varchar(100) DEFAULT NULL,
  `ENABLE_TIME` datetime DEFAULT NULL COMMENT '查询时间上限(24小时后)',
  `QUERY_TIME` datetime DEFAULT NULL COMMENT '查询开始时间（30秒后）',
  `QUERY_TIMES` int(10) DEFAULT NULL COMMENT '查询次数',
  PRIMARY KEY (`ID`),
  KEY `INX_ORDERNO` (`ORDER_NO`),
  KEY `INX_TRANSNO` (`USER_TRANSACTION_NO`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_pay_platform_transaction
-- ----------------------------
