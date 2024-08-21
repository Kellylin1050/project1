-- --------------------------------------------------------
-- 主機:                           127.0.0.1
-- 伺服器版本:                        11.4.2-MariaDB - mariadb.org binary distribution
-- 伺服器作業系統:                      Win64
-- HeidiSQL 版本:                  12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 傾印 project1 的資料庫結構
CREATE DATABASE IF NOT EXISTS `project1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `project1`;

-- 傾印  資料表 project1.book 結構
CREATE TABLE IF NOT EXISTS `book` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `sellprice` int(11) DEFAULT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 正在傾印表格  project1.book 的資料：~4 rows (近似值)
INSERT INTO `book` (`book_id`, `title`, `author`, `description`, `price`, `sellprice`) VALUES
	(2, 'Java Programming', 'John Doe', '一本關於java的指南', 500, 520),
	(3, 'Slow Dance', 'Rainbow Rowell', 'Shiloh questions if Kerry still wants to reconnect after all the time and changes.', 600, 550),
	(4, 'Slow Dance', 'Rainbow Rowell', 'Shiloh questions if Kerry still wants to reconnect after all the time and changes.', 600, 550),
	(6, 'Slow Dance', 'Rainbow Rowell', 'Shiloh questions if Kerry still wants to reconnect after all the time and changes.', 600, 550);

-- 傾印  資料表 project1.role 結構
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 正在傾印表格  project1.role 的資料：~3 rows (近似值)
INSERT INTO `role` (`role_id`, `name`) VALUES
	(1, 'ADMIN'),
	(2, 'ROLE_USER'),
	(3, 'ROLE_ADMIN');

-- 傾印  資料表 project1.user 結構
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 正在傾印表格  project1.user 的資料：~8 rows (近似值)
INSERT INTO `user` (`user_id`, `username`, `password`, `name`, `phone`, `email`, `enabled`) VALUES
	(10, 'dsadasd', 'e10adc3949ba59abbe56e057f20f883e', '2349808', '0947382647', 'save1234@gmail.com', b'0'),
	(11, 'fjdig', '827ccb0eea8a706c4c34a16891f84e7b', 'fjdig', '0937485123', 'test12@gmail.com', b'0'),
	(13, 'Diana_liu', '827ccb0eea8a706c4c34a16891f84e7b', 'Diana', '0947382613', 'test50@gmail.com', b'0'),
	(14, 'aaatgs', '81dc9bdb52d04dc20036dbd8313ed055', 'aaatgs', NULL, 'test10@gmail.com', b'0'),
	(15, 'john', 'fcea920f7412b5da7be0cf42b8c93759', 'john', NULL, 'john.doe@example.com', b'0'),
	(28, 'mmm', '827ccb0eea8a706c4c34a16891f84e7b', 'mmm', NULL, 'test54@gmail.com', b'1'),
	(33, 'William_chou', '4730jihfuiw', 'William', '0947382645', 'William38201@gmail.com', b'1'),
	(34, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', NULL, 'admin@gmail.com', b'1'),
	(35, 'Sandra_lin', '949c61f8b979ca45f14c724252a3d560', 'Sandra', NULL, 'Sandra_1849@gmail.com', b'1');

-- 傾印  資料表 project1.user_role 結構
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_role_id`),
  KEY `FK__user` (`user_id`),
  KEY `FK__role` (`role_id`),
  CONSTRAINT `FK__role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
  CONSTRAINT `FK__user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 正在傾印表格  project1.user_role 的資料：~8 rows (近似值)
INSERT INTO `user_role` (`user_role_id`, `user_id`, `role_id`) VALUES
	(5, 11, 2),
	(7, 13, 2),
	(8, 14, 2),
	(9, 15, 2),
	(22, 28, 2),
	(27, 33, 1),
	(28, 34, 3),
	(29, 35, 2);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
