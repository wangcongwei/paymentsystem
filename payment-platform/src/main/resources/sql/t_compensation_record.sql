/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : payment

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2015-09-30 10:16:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_compensation_record`
-- ----------------------------
DROP TABLE IF EXISTS `t_compensation_record`;
CREATE TABLE `t_compensation_record` (
  `ID` int(19) NOT NULL AUTO_INCREMENT,
  `trans_no` varchar(100) DEFAULT NULL,
  `trans_Status` varchar(20) DEFAULT NULL,
  `NEXT_TIME` datetime DEFAULT NULL,
  `COMPENSATION_TIMES` int(11) DEFAULT NULL,
  `creat_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_compensation_record
-- ----------------------------
INSERT INTO `t_compensation_record` VALUES ('1', '20150831000001', 'FAILED', null, '5', '2015-09-29 23:33:01', '2015-09-29 23:33:01');
INSERT INTO `t_compensation_record` VALUES ('2', '20150831000001', 'FAILED', null, '6', '2015-09-29 23:35:54', '2015-09-29 23:35:54');
INSERT INTO `t_compensation_record` VALUES ('3', '20150831000001', 'FAILED', null, '7', '2015-09-29 23:39:10', '2015-09-29 23:39:10');
INSERT INTO `t_compensation_record` VALUES ('4', '20150831000001', 'FAILED', null, '8', '2015-09-29 23:54:36', '2015-09-29 23:54:36');
INSERT INTO `t_compensation_record` VALUES ('5', '112', 'CREATE', '2015-09-27 23:31:00', '1', '2015-09-29 23:54:54', '2015-09-29 23:54:54');
INSERT INTO `t_compensation_record` VALUES ('6', '112', 'CREATE', '2015-09-27 23:33:00', '2', '2015-09-29 23:55:00', '2015-09-29 23:55:00');
INSERT INTO `t_compensation_record` VALUES ('7', '112', 'CREATE', '2015-09-27 23:37:00', '3', '2015-09-29 23:56:52', '2015-09-29 23:56:52');
INSERT INTO `t_compensation_record` VALUES ('8', '112', 'CREATE', '2015-09-27 23:43:00', '4', '2015-09-29 23:57:20', '2015-09-29 23:57:20');
INSERT INTO `t_compensation_record` VALUES ('9', '112', 'CREATE', '2015-09-27 23:51:00', '5', '2015-09-29 23:58:00', '2015-09-29 23:58:00');
INSERT INTO `t_compensation_record` VALUES ('10', '112', 'CREATE', '2015-09-28 00:01:00', '6', '2015-09-29 23:59:00', '2015-09-29 23:59:00');
INSERT INTO `t_compensation_record` VALUES ('11', '112', 'CREATE', '2015-09-28 00:13:00', '7', '2015-09-30 00:00:00', '2015-09-30 00:00:00');
INSERT INTO `t_compensation_record` VALUES ('12', '112', 'CREATE', '2015-09-28 00:27:00', '8', '2015-09-30 00:01:00', '2015-09-30 00:01:00');
INSERT INTO `t_compensation_record` VALUES ('13', '112', 'CREATE', '2015-09-28 00:43:00', '9', '2015-09-30 00:02:00', '2015-09-30 00:02:00');
INSERT INTO `t_compensation_record` VALUES ('14', '112', 'CREATE', '2015-09-28 01:01:00', '10', '2015-09-30 00:03:00', '2015-09-30 00:03:00');
INSERT INTO `t_compensation_record` VALUES ('15', '112', 'CREATE', '2015-09-28 01:21:00', '11', '2015-09-30 00:04:01', '2015-09-30 00:04:01');
INSERT INTO `t_compensation_record` VALUES ('16', '112', 'CREATE', '2015-09-28 01:43:00', '12', '2015-09-30 00:05:01', '2015-09-30 00:05:01');
INSERT INTO `t_compensation_record` VALUES ('17', '112', 'CREATE', '2015-09-28 02:07:00', '13', '2015-09-30 00:06:00', '2015-09-30 00:06:00');
INSERT INTO `t_compensation_record` VALUES ('18', '112', 'CREATE', '2015-09-28 02:33:00', '14', '2015-09-30 00:07:00', '2015-09-30 00:07:00');
INSERT INTO `t_compensation_record` VALUES ('19', '112', 'CREATE', '2015-09-28 03:01:00', '15', '2015-09-30 00:08:00', '2015-09-30 00:08:00');
INSERT INTO `t_compensation_record` VALUES ('20', '112', 'CREATE', '2015-09-28 03:31:00', '16', '2015-09-30 00:09:00', '2015-09-30 00:09:00');
INSERT INTO `t_compensation_record` VALUES ('21', '112', 'CREATE', '2015-09-28 04:03:00', '17', '2015-09-30 00:10:00', '2015-09-30 00:10:00');
INSERT INTO `t_compensation_record` VALUES ('22', '112', 'FAILED', null, '18', '2015-09-30 00:11:00', '2015-09-30 00:11:00');
