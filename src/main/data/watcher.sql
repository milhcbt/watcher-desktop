-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Nov 29, 2013 at 10:07 PM
-- Server version: 5.5.27-log
-- PHP Version: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `watcher`
--

-- --------------------------------------------------------

--
-- Table structure for table `alarm_log`
--

CREATE TABLE IF NOT EXISTS `alarm_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `notes` varchar(400) NOT NULL,
  `device` bigint(20) NOT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `device` (`device`),
  KEY `type` (`type`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `city`
--

CREATE TABLE IF NOT EXISTS `city` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  `area` varchar(50) NOT NULL,
  `zip_code` varchar(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `city`
--

INSERT INTO `city` (`id`, `description`, `area`, `zip_code`) VALUES
(0, 'dummy', 'none', '10000'),
(1, 'Bandung', 'pusat', '40000'),
(2, 'Bandung', 'dago', '42000'),
(3, 'Bandung', 'Ledeng', '41000'),
(4, 'Jakarta', 'Pusat', '10000'),
(5, 'Jakarta', 'Barat', '11000');

-- --------------------------------------------------------

--
-- Table structure for table `contact`
--

CREATE TABLE IF NOT EXISTS `contact` (
  `name` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `description` varchar(100) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group` (`group`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `contact`
--

INSERT INTO `contact` (`name`, `phone`, `email`, `description`, `id`, `group`) VALUES
('bahlul', '4242424', 'bahlul@yahoo.com', 'petugas yang malas', 1, 1),
('leha', '646262646', 'leha@leha.com', 'petugas yang suka leha-leha', 2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `contact_group`
--

CREATE TABLE IF NOT EXISTS `contact_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `contact_group`
--

INSERT INTO `contact_group` (`id`, `description`) VALUES
(1, 'group1'),
(2, 'group2');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `address` varchar(2000) NOT NULL,
  `primary_phone` varchar(50) NOT NULL,
  `secondary_phone` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `city` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `city` (`city`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=46 ;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `name`, `address`, `primary_phone`, `secondary_phone`, `email`, `city`) VALUES
(1, 'dummy', 'ooo', 'ooo', 'ooo', 'salami@email.com', 0),
(2, 'iman', 'bandung', '6262626262', '', 'iman@garuda.com', 0),
(3, 'badu', 'jakarta', '1939242404040', '', 'badu@email.com', 1),
(4, 'salami', 'garut', '42626262', '', 'salami@email.com', 0),
(5, 'sakala', 'bogor', '424626666666', '', 'sakala@yahoo.com', 4),
(6, 'saya', 'other addres', '64246262', '', 'saya2@yahoo.com', 4),
(7, 'sakala', 'rain city', '6262626', '262626262', 'sakala2@yahoo.com', 0),
(8, 'sajak', 'bandung', '4242424', '', 'sajak@sastra.com', 0),
(9, 'sakara', 'Bekasi', '4242664', '', 'sakara@gmail.com', 0),
(10, 'kabul', 'jakarta', '6262626', '', 'kabul@yahoo.com', 0),
(11, 'akmal', 'mars', '460404040', '', 'akmal@margahayu.com', 0),
(33, 'saya', 'garuda 1', '081234567', '', 'saya@garuda.com', 0),
(34, 'badubaba', 'jakarta', '1939242404040', '', 'badu@email.com', 1),
(35, 'badudada', 'jakarta', '1939242404040', '', 'badu@email.com', 0),
(36, 'baduhaha', 'jakarta', '1939242404040', '', 'badu@email.com', 0),
(37, 'badunana', 'jakarta', '1939242404040', '', 'badu@email.com', 0),
(38, 'badunana', 'jakarta', '1939242404040', '7373737373', 'badu@email.com', 0),
(39, 'test', 'test', '2020', '462666', 'email@mail.com', 0),
(40, 'test', 'test', '2020', '462666', 'email@mail.com', 0),
(41, 'xsa', 'satu', '444', '2222', 'xsa@gmail.com', 0),
(42, 'lone Ranger', 'texas', '44444', '666666', 'lone@texas.com', 0),
(43, 'baru', 'baru', '4444', '00000', '202020', 3),
(44, 'doremi', 'antapani raya', '624222444', '+2222244242', 'do@rem.mi', 0),
(45, 'baru', 'baru', '64646', '646464', 'baru@yahoo.com', 2);

-- --------------------------------------------------------

--
-- Table structure for table `device`
--

CREATE TABLE IF NOT EXISTS `device` (
  `id` bigint(20) NOT NULL,
  `loc_x` int(5) NOT NULL DEFAULT '0',
  `loc_y` int(5) NOT NULL DEFAULT '0',
  `last_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resolve` tinyint(2) NOT NULL DEFAULT '0' COMMENT '-1 neet tobe resolved, 0 =normal , 1 =resolved',
  `customer` int(11) unsigned NOT NULL,
  `digit1` tinyint(2) NOT NULL DEFAULT '-1' COMMENT '-1= UNKNOW, 0 = LOW, 1 = HIGH',
  `digit2` tinyint(2) NOT NULL DEFAULT '-1' COMMENT '-1= UNKNOW, 0 = LOW, 1 = HIGH',
  `digit3` tinyint(2) NOT NULL DEFAULT '-1' COMMENT '-1= UNKNOW, 0 = LOW, 1 = HIGH',
  `digit4` tinyint(2) NOT NULL DEFAULT '-1' COMMENT '-1= UNKNOW, 0 = LOW, 1 = HIGH',
  `analog1` smallint(6) NOT NULL DEFAULT '-1' COMMENT 'need tracehold',
  `analog2` smallint(6) NOT NULL DEFAULT '-1',
  `analog3` smallint(6) NOT NULL DEFAULT '-1',
  `analog4` smallint(6) NOT NULL DEFAULT '-1',
  `address` varchar(100) NOT NULL DEFAULT 'NA',
  `city` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer`),
  KEY `fk_device_customer_idx` (`customer`),
  KEY `city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `device`
--

INSERT INTO `device` (`id`, `loc_x`, `loc_y`, `last_time`, `resolve`, `customer`, `digit1`, `digit2`, `digit3`, `digit4`, `analog1`, `analog2`, `analog3`, `analog4`, `address`, `city`) VALUES
(6420, 970, 3090, '2013-10-20 10:12:15', -1, 7, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2130706430, 101, 103, '2013-10-29 05:42:45', -1, 0, -1, -1, -1, -1, 0, 0, 0, 0, '', 0),
(2130706431, 100, 100, '2013-10-15 02:22:24', 1, 33, 0, -1, -1, -1, 0, 3520, 0, 0, '', 0),
(2130706433, 0, 0, '2013-10-29 06:38:47', -1, 0, 0, -1, -1, -1, -1, -1, -1, -1, 'NA', 0),
(2147484896, 840, 668, '2013-11-03 08:50:52', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 'NA', 0),
(2147485768, 483, 104, '2013-10-20 23:39:15', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147485842, 955, 716, '2013-10-21 09:41:16', 1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147485977, 99, 592, '2013-10-17 03:47:41', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147486846, 756, 99, '2013-10-20 23:42:34', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147487088, 208, 255, '2013-11-01 18:08:11', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 'NA', 0),
(2147487603, 933, 969, '2013-10-21 00:30:09', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147487782, 366, 545, '2013-11-03 08:18:01', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 'NA', 0),
(2147488543, 297, 882, '2013-10-17 03:42:52', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147489482, 360, 369, '2013-11-05 09:43:02', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 'NA', 0),
(2147489492, 965, 518, '2013-10-21 10:20:59', 1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147490414, 473, 845, '2013-10-21 09:44:55', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147490473, 783, 776, '2013-11-06 03:50:32', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 'NA', 0),
(2147492140, 303, 683, '2013-10-30 07:54:10', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 'NA', 0),
(2147492336, 815, 296, '2013-10-21 09:42:24', 1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147492505, 524, 560, '2013-10-21 09:44:06', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(2147492625, 508, 313, '2013-11-06 03:51:38', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 'NA', 0),
(2147493141, 827, 995, '2013-10-21 06:08:44', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(3232235877, 1164, 1009, '2013-10-20 08:25:31', -1, 33, 0, 1, 1, 1, 2222, -1, -1, -1, '', 0),
(3232261381, 173, 175, '2013-10-22 12:20:13', -1, 11, 0, 0, 0, 0, 0, 0, 0, 0, '', 0),
(3232261478, 400, 100, '2013-10-15 02:22:24', -1, 33, 0, -1, -1, -1, 0, 3520, 0, 0, '', 0);

-- --------------------------------------------------------

--
-- Table structure for table `log_type`
--

CREATE TABLE IF NOT EXISTS `log_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(50) NOT NULL COMMENT 'deskripsi',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE IF NOT EXISTS `message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `msgtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when message received',
  `message` varchar(200) NOT NULL COMMENT 'body of message',
  `device` bigint(20) NOT NULL COMMENT 'ip source of message in integer format',
  `msg_type` tinyint(1) unsigned NOT NULL COMMENT '1 = in (from device to app), 2 = out (from app to device)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `fk_message_device1_idx` (`device`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=29242 ;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`id`, `msgtime`, `message`, `device`, `msg_type`) VALUES
(29202, '2013-10-31 10:21:52', 'Metajasa01{strBody=IO.IO.07.01111111.000000000.11*.*}', 2130706433, 1),
(29203, '2013-10-31 10:28:47', 'Metajasa01{strBody=IO.IO.07.01111111.000000000.11*.*}', 2130706433, 1),
(29204, '2013-10-31 10:32:57', 'Metajasa01{strBody=IO.IO.07.01111111.000000073.11*.*}', 2130706433, 1),
(29205, '2013-10-31 10:40:25', 'IO.11.11111111.000000000.11*', 2130706433, 1),
(29206, '2013-10-31 10:57:04', 'RST.0.01111111.000000000.11*', 2130706433, 1),
(29207, '2013-10-31 10:57:05', 'IO.11.11111111.000000000.11*', 2130706433, 1),
(29208, '2013-10-31 10:57:06', 'IO.06.01111111.000007537.11*', 2130706433, 1),
(29209, '2013-10-31 10:57:07', 'IO.11.11111111.000000000.11*', 2130706433, 1),
(29210, '2013-10-31 10:57:08', 'IO.07.01111101.000000000.11*', 2130706433, 1),
(29211, '2013-10-31 10:57:09', 'IO.05.01111111.000000000.11*', 2130706433, 1),
(29212, '2013-10-31 10:57:10', 'IO.11.11111111.000000000.11*', 2130706433, 1),
(29213, '2013-10-31 10:57:11', 'IO.05.01111111.000000353.11*', 2130706433, 1),
(29214, '2013-10-31 10:57:12', 'IO.11.11111111.000000000.11*', 2130706433, 1),
(29215, '2013-10-31 10:57:13', 'IO.05.01111111.000000353.11*', 2130706433, 1),
(29216, '2013-10-31 10:57:51', 'IO.07.01111111.000000073.11*', 2130706433, 1),
(29217, '2013-10-31 10:57:52', 'IO.07.01111111.000000000.11*', 2130706433, 1),
(29218, '2013-10-31 10:57:53', 'RST.0.01111111.000000000.11*', 2130706433, 1),
(29219, '2013-10-31 10:57:54', 'IO.05.01111111.000000000.11*', 2130706433, 1),
(29220, '2013-10-31 10:57:55', 'RST.0.01111111.000000000.11*', 2130706433, 1),
(29221, '2013-10-31 10:57:56', 'IO.07.01111101.000000000.11*', 2130706433, 1),
(29222, '2013-10-31 10:57:57', 'IO.07.01111111.000000000.11*', 2130706433, 1),
(29223, '2013-10-31 10:57:58', 'RST.0.01111111.000000000.11*', 2130706433, 1),
(29224, '2013-10-31 10:57:59', 'RST.0.01111111.000000000.11*', 2130706433, 1),
(29225, '2013-10-31 10:58:00', 'IO.05.01111111.000000353.11*', 2130706433, 1),
(29226, '2013-10-31 10:58:01', 'IO.07.01111111.000000000.11*', 2130706433, 1),
(29227, '2013-10-31 10:58:02', 'IO.11.11111111.000000000.11*', 2130706433, 1),
(29228, '2013-10-31 10:58:03', 'IO.07.01111111.000000000.11*', 2130706433, 1),
(29229, '2013-10-31 10:58:04', 'IO.07.01111101.000000000.11*', 2130706433, 1),
(29230, '2013-10-31 10:58:05', 'IO.07.01111111.000000000.11*', 2130706433, 1),
(29231, '2013-10-31 10:58:06', 'IO.07.01111111.000000000.11*', 2130706433, 1),
(29232, '2013-10-31 10:58:07', 'IO.07.01111111.000000000.11*', 2130706433, 1),
(29233, '2013-10-31 10:58:08', 'IO.07.01111111.000000000.11*', 2130706433, 1),
(29234, '2013-10-31 10:58:09', 'IO.11.11111111.000000000.11*', 2130706433, 1),
(29235, '2013-10-31 10:58:10', 'IO.06.01111111.000007537.11*', 2130706433, 1),
(29236, '2013-11-01 04:16:03', 'IO.07.01111111.000000073.11*', 2130706433, 1),
(29237, '2013-11-01 04:16:03', 'IO.07.01111111.000000000.11*', 2130706433, 1),
(29238, '2013-11-01 04:16:35', 'IO.07.01111111.000000073.11*', 2130706433, 1),
(29239, '2013-11-01 04:16:36', 'IO.11.11111111.000000000.11*', 2130706433, 1),
(29240, '2013-11-01 06:12:29', 'IO.05.01111111.000000353.11*', 2130706433, 1),
(29241, '2013-11-01 06:12:29', 'IO.06.01111111.000007537.11*', 2130706433, 1);

-- --------------------------------------------------------

--
-- Table structure for table `property`
--

CREATE TABLE IF NOT EXISTS `property` (
  `key` varchar(100) NOT NULL COMMENT 'key of property',
  `type` int(2) NOT NULL COMMENT '0=boolean, 1 = integer, 2 = floating point, 3 = text, 4 = time, 5 = date, 8 = date time',
  `value` varchar(200) NOT NULL COMMENT 'value',
  `role` varchar(200) NOT NULL COMMENT 'regular expresion for validation',
  `doc` varchar(200) NOT NULL COMMENT 'documentation',
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `property`
--

INSERT INTO `property` (`key`, `type`, `value`, `role`, `doc`) VALUES
('digit1_alarm', 1, '0', '[0-9]', '0 = all off, 1 = high on low off, 2 = low on high off, 3 = high on low on'),
('digit1_group1', 1, '0', '[0-1]', '0 = off, don''t contact group one, 1 = on, contact group one'),
('digit1_group2', 1, '0', '[0-1]', '0 = off, don''t contact group two, 1 = on, contact group two'),
('digit1_log', 0, '1', '[0-1]', '0 = off, don''t log, 1 = on, log it'),
('digit2_alarm', 1, '0', '[0-9]', '0 = all off, 1 = high on low off, 2 = low on high off, 3 = high on low on'),
('digit2_group1', 1, '0', '[0-1]', '0 = off, don''t contact group one, 1 = on, contact group one'),
('digit2_group2', 1, '0', '[0-1]', '0 = off, don''t contact group two, 1 = on, contact group two'),
('digit2_log', 0, '1', '[0-1]', '0 = off, don''t log, 1 = on, log it'),
('digit3_alarm', 1, '0', '[0-9]', '0 = all off, 1 = high on low off, 2 = low on high off, 3 = high on low on'),
('digit3_group1', 1, '0', '[0-1]', '0 = off, don''t contact group one, 1 = on, contact group one'),
('digit3_group2', 1, '0', '[0-1]', '0 = off, don''t contact group two, 1 = on, contact group two'),
('digit3_log', 0, '1', '[0-1]', '0 = off, don''t log, 1 = on, log it'),
('digit4_alarm', 1, '0', '[0-9]', '0 = all off, 1 = high on low off, 2 = low on high off, 3 = high on low on'),
('digit4_group1', 1, '0', '[0-1]', '0 = off, don''t contact group one, 1 = on, contact group one'),
('digit4_group2', 1, '0', '[0-1]', '0 = off, don''t contact group two, 1 = on, contact group two'),
('digit4_log', 0, '1', '[0-1]', '0 = off, don''t log, 1 = on, log it');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `type` int(11) NOT NULL COMMENT '1 = normal user view only, 2 = admin, may add new user, new device, new customer',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=35 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `name`, `email`, `password`, `type`) VALUES
(0, 'system', '', '', 0),
(11, 'saya', 'saya@yahoo.com', 'pass', 1),
(12, 'kamu', 'kamu@yahoo.com', 'pass', 2),
(13, 'dia', 'dia@yahoo.com', 'pass', 1),
(14, 'sasa', 'sasa@yahoo.com', 'salah pas', 1),
(16, 'saya2', 'saya@yahoo.com', 'passjuga', 1),
(17, 'kamu4', 'kamu@yahoo.com', 'pass', 2),
(18, 'sayahhhh', 'saya@yahoo.org', 'pass,,,,,', 1),
(19, 'sayahh', 'saya@yahoo.com', 'pass++++++', 1),
(20, 'say', 'saya@yahoo.com', 'pass', 1),
(22, 'kamunohnu', 'kamu@yahoo.com', 'pass', 2),
(23, 'kamunu', 'kamu@yahoo.com', 'pass', 2),
(24, 'kamuna', 'kamu@yahoo.com', 'pass', 2),
(25, 'kamkh', 'kamu@yahoo.com', 'pass', 2),
(26, 'dara', 'dara@manis.ku', 'dara', 1),
(27, 'gadis', 'gadis@cantik.com', 'cantik', 2),
(28, 'wanita', 'wanita@penggoda.com', 'wanita', 1),
(33, 'saya', 'saya@yahoo.uk', 'sayajuga', 1),
(34, 'nami', 'nami@email.com', 'password', 2);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `alarm_log`
--
ALTER TABLE `alarm_log`
  ADD CONSTRAINT `alarm_log_ibfk_1` FOREIGN KEY (`device`) REFERENCES `device` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `alarm_log_ibfk_2` FOREIGN KEY (`type`) REFERENCES `log_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `alarm_log_ibfk_3` FOREIGN KEY (`user`) REFERENCES `user` (`id`);

--
-- Constraints for table `contact`
--
ALTER TABLE `contact`
  ADD CONSTRAINT `contact_ibfk_1` FOREIGN KEY (`group`) REFERENCES `contact_group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`city`) REFERENCES `city` (`id`);

--
-- Constraints for table `device`
--
ALTER TABLE `device`
  ADD CONSTRAINT `device_ibfk_3` FOREIGN KEY (`city`) REFERENCES `city` (`id`),
  ADD CONSTRAINT `device_ibfk_4` FOREIGN KEY (`customer`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `message_ibfk_1` FOREIGN KEY (`device`) REFERENCES `device` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
