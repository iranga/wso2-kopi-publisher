-- MySQL dump 10.13  Distrib 5.6.20, for osx10.7 (x86_64)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.6.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Rewards`
--
DROP DATABASE IF EXISTS `Inventory`;
CREATE DATABASE `Inventory`;
USE `Inventory`;
DROP TABLE IF EXISTS `Rewards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Rewards` (
  `subscriber` varchar(100) DEFAULT NULL,
  `points` double
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rewards`
--

LOCK TABLES `Rewards` WRITE;
/*!40000 ALTER TABLE `Rewards` DISABLE KEYS */;
INSERT INTO `Rewards` VALUES ('admin','0'),('sam','0');
/*!40000 ALTER TABLE `Rewards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drinks`
--
USE `Inventory`;
DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `drinkName` varchar(100),
  `qtyAvl` double DEFAULT NULL,
  `qtyOrder` double  DEFAULT NULL,
  `ROF` double  DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drink`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
insert into orders (drinkName,qtyAvl,qtyOrder,ROF) values ('DOPPIO',900,900,10);
insert into orders (drinkName,qtyAvl,qtyOrder,ROF) values ('ESPRESSO',800,800,9);
insert into orders (drinkName,qtyAvl,qtyOrder,ROF) values ('AMERICANO',1200,1200,13);
insert into orders (drinkName,qtyAvl,qtyOrder,ROF) values ('MACCIHIATO',900,900,11);
insert into orders (drinkName,qtyAvl,qtyOrder,ROF) values ('LATTE',1000,1000,10);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-17 12:15:08
