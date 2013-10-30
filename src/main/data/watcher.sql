-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 17, 2013 at 05:44 AM
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
-- Table structure for table `customer`
--

CREATE TABLE IF NOT EXISTS `customer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nama` varchar(50) NOT NULL,
  `alamat` varchar(2000) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `nama`, `alamat`, `phone`, `email`) VALUES
(1, 'saya', 'garuda 1', '081234567', 'saya@garuda.com'),
(2, 'iman', 'bandung', '6262626262', 'iman@garuda.com'),
(3, 'badu', 'jakarta', '1939242404040', 'badu@email.com'),
(4, 'salami', 'garut', '42626262', 'salami@email.com'),
(5, 'sakala', 'bogor', '424626666666', 'sakala@yahoo.com'),
(6, 'saya', 'other addres', '64246262', 'saya2@yahoo.com'),
(7, 'sakala', 'other city', '6262626', 'sakala2@yahoo.com'),
(8, 'sajak', 'bandung', '4242424', 'sajak@sastra.com'),
(9, 'sakara', 'Bekasi', '4242664', 'sakara@gmail.com'),
(10, 'kabul', 'jakarta', '6262626', 'kabul@yahoo.com'),
(11, 'akmal', 'mars', '460404040', 'akmal@margahayu.com');

-- --------------------------------------------------------

--
-- Table structure for table `device`
--

CREATE TABLE IF NOT EXISTS `device` (
  `id` bigint(20) NOT NULL,
  `locX` int(5) NOT NULL,
  `locY` int(5) NOT NULL,
  `last_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resolve` tinyint(2) NOT NULL DEFAULT '0' COMMENT '-1 neet tobe resolved, 0 =normal , 1 =resolved',
  `customer_id` bigint(20) unsigned NOT NULL,
  `digit1` tinyint(2) NOT NULL COMMENT '-1 Off, 0 = no status, 1 = On',
  `digit2` tinyint(2) NOT NULL COMMENT '-1 Off, 0 = no status, 1 = On',
  `digit3` tinyint(2) NOT NULL COMMENT '-1 Off, 0 = no status, 1 = On',
  `digit4` tinyint(2) NOT NULL COMMENT '-1 Off, 0 = no status, 1 = On',
  `analog1` smallint(6) NOT NULL,
  `analog2` smallint(6) NOT NULL,
  `analog3` smallint(6) NOT NULL,
  `analog4` smallint(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  KEY `fk_device_customer_idx` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `device`
--

INSERT INTO `device` (`id`, `locX`, `locY`, `last_time`, `resolve`, `customer_id`, `digit1`, `digit2`, `digit3`, `digit4`, `analog1`, `analog2`, `analog3`, `analog4`) VALUES
(2130706433, 100, 100, '2013-10-15 02:22:24', -1, 1, 1, -1, -1, -1, 0, 3520, 0, 0),
(2130706434, 100, 100, '2013-10-15 02:22:24', 0, 1, 0, -1, -1, -1, 0, 3520, 0, 0),
(2147488543, 297, 882, '2013-10-17 03:42:52', 0, 11, 0, 0, 0, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE IF NOT EXISTS `message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `msgtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when message received',
  `message` varchar(50) NOT NULL COMMENT 'body of message',
  `device_id` bigint(20) NOT NULL COMMENT 'ip source of message in integer format',
  `msg_type` tinyint(1) unsigned NOT NULL COMMENT '1 = in (from device to app), 2 = out (from app to device)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `fk_message_device1_idx` (`device_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=29197 ;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`id`, `msgtime`, `message`, `device_id`, `msg_type`) VALUES
(29147, '2013-10-15 08:55:45', 'I\r\n', 2130706433, 1),
(29148, '2013-10-15 08:56:00', 'N3520\r\n', 2130706433, 1),
(29149, '2013-10-15 09:14:39', 'i\r\n', 2130706433, 1),
(29150, '2013-10-15 09:14:54', 'I\r\n', 2130706433, 1),
(29151, '2013-10-15 09:15:08', 'N3520\r\n', 2130706433, 1),
(29152, '2013-10-15 09:15:10', 'N3520\r\n', 2130706433, 1),
(29153, '2013-10-15 09:15:10', 'N3520\r\n', 2130706433, 1),
(29154, '2013-10-15 09:15:11', 'N3520\r\n', 2130706433, 1),
(29155, '2013-10-15 09:15:11', 'N3520\r\n', 2130706433, 1),
(29156, '2013-10-15 09:15:12', 'N3520\r\n', 2130706433, 1),
(29157, '2013-10-15 09:15:12', 'L\r\n', 2130706433, 1),
(29158, '2013-10-15 09:15:13', 'l\r\n', 2130706433, 1),
(29159, '2013-10-15 09:15:14', 'L\r\n', 2130706433, 1),
(29160, '2013-10-15 09:15:15', 'J\r\n', 2130706433, 1),
(29161, '2013-10-15 09:15:16', 'j\r\n', 2130706433, 1),
(29162, '2013-10-15 09:15:17', 'K\r\n', 2130706433, 1),
(29163, '2013-10-15 09:15:17', 'k\r\n', 2130706433, 1),
(29164, '2013-10-15 09:15:18', 'l\r\n', 2130706433, 1),
(29165, '2013-10-15 09:15:20', 'i\r\n', 2130706433, 1),
(29166, '2013-10-15 09:16:02', 'I\r\n', 2130706433, 1),
(29167, '2013-10-15 09:48:18', 'J\r\n', 2130706433, 1),
(29168, '2013-10-15 09:48:19', 'j\r\n', 2130706433, 1),
(29169, '2013-10-15 09:48:20', 'J\r\n', 2130706433, 1),
(29170, '2013-10-15 09:48:21', 'j\r\n', 2130706433, 1),
(29171, '2013-10-15 09:48:22', 'J\r\n', 2130706433, 1),
(29172, '2013-10-15 09:48:22', 'j\r\n', 2130706433, 1),
(29173, '2013-10-15 09:48:23', 'J\r\n', 2130706433, 1),
(29174, '2013-10-15 09:48:24', 'j\r\n', 2130706433, 1),
(29175, '2013-10-15 09:48:25', 'N3520\r\n', 2130706433, 1),
(29176, '2013-10-15 09:48:29', 'N3520\r\n', 2130706433, 1),
(29177, '2013-10-15 09:48:30', 'N3520\r\n', 2130706433, 1),
(29178, '2013-10-15 09:48:30', 'N3520\r\n', 2130706433, 1),
(29179, '2013-10-15 09:48:31', 'N3520\r\n', 2130706433, 1),
(29180, '2013-10-15 09:48:31', 'N3520\r\n', 2130706433, 1),
(29181, '2013-10-15 09:48:31', 'N3520\r\n', 2130706433, 1),
(29182, '2013-10-15 09:48:32', 'N3520\r\n', 2130706433, 1),
(29183, '2013-10-15 09:48:32', 'N3520\r\n', 2130706433, 1),
(29184, '2013-10-15 09:48:32', 'N3520\r\n', 2130706433, 1),
(29185, '2013-10-15 09:48:35', 'i\r\n', 2130706433, 1),
(29186, '2013-10-15 23:32:50', 'L\r\n', 2130706433, 1),
(29187, '2013-10-15 23:33:04', 'l\r\n', 2130706433, 1),
(29188, '2013-10-15 23:33:08', 'I\r\n', 2130706433, 1),
(29189, '2013-10-15 23:36:46', 'J\r\n', 2130706433, 1),
(29190, '2013-10-15 23:36:52', 'j\r\n', 2130706433, 1),
(29191, '2013-10-15 23:36:54', 'i\r\n', 2130706433, 1),
(29192, '2013-10-15 23:44:25', 'I\r\n', 2130706433, 1),
(29193, '2013-10-15 23:45:35', 'i\r\n', 2130706433, 1),
(29194, '2013-10-15 23:47:29', 'I\r\n', 2130706433, 1),
(29195, '2013-10-16 00:03:05', 'i\r\n', 2130706433, 1),
(29196, '2013-10-16 00:03:12', 'I\r\n', 2130706433, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `type` int(11) NOT NULL COMMENT '1 = normal user view only, 2 = admin, may add new user, new device, new customer',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `device`
--
ALTER TABLE `device`
  ADD CONSTRAINT `device_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `message_ibfk_1` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
