DROP DATABASE IF EXISTS `musicplayerdb`;
CREATE DATABASE `musicplayerdb`;
USE `musicplayerdb`;

-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: musicplayerdb
-- ------------------------------------------------------
-- Server version	8.4.5

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `song`
--

DROP TABLE IF EXISTS song;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE song (
  id bigint NOT NULL AUTO_INCREMENT,
  album varchar(255) DEFAULT NULL,
  artist varchar(255) DEFAULT NULL,
  duration int NOT NULL,
  release_year int NOT NULL,
  title varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `song`
--

LOCK TABLES song WRITE;
/*!40000 ALTER TABLE song DISABLE KEYS */;
INSERT INTO song VALUES (1,'Eziokwu Deluxe','Odumodublvck',300,2023,'Dog Eat Dog II (Remix)'),(2,'Eziokwu','Odumodublvck',240,2023,'Dog Eat Dog II'),(3,'Made in Lagos','Wizkid',230,2020,'Essence'),(4,'Rave & Roses','Rema',210,2022,'Calm Down'),(5,'Love, Damini','Burna Boy',280,2022,'Last Last'),(6,'Mr. Money','Asake',200,2022,'Terminator'),(7,'Timeless','Davido',230,2023,'Unavailable'),(8,'19 & Dangerous','Ayra Starr',195,2023,'Rush'),(9,'Boy Alone','Omah Lay',225,2022,'Understand'),(10,'Gbagada Express','BOJ',220,2022,'Lekki Love'),(11,'Mr. Money','Asake',205,2022,'Sungba Remix'),(12,'Soweto','Victony',210,2023,'Soweto'),(13,'Young Preacher','Blaqbonez',200,2022,'Back in Uni'),(14,'Ku Lo Sa','Oxlade',190,2022,'Ku Lo Sá'),(15,'Barnabas','Kizz Daniel',210,2022,'Buga (É Kè)'),(16,'Amapiano Grooves','Kabza De Small',230,2021,'Abalele'),(17,'Jerusalema','Master KG',250,2020,'Jerusalema'),(18,'Tequila Ever After','Adekunle Gold',215,2023,'Party No Dey Stop'),(19,'Work of Art','Asake',225,2023,'Lonely @ the Top'),(20,'Love Damini','Burna Boy',210,2023,'Big 7'),(21,NULL,'Fela Kuti',620,1980,'Zombie'),(22,NULL,'Fela Kuti',750,1981,'Water No Get Enemy'),(23,NULL,'Fela Kuti',710,1977,'Gentleman'),(24,NULL,'Yemi Aladé',230,2019,'Shekere ft. Angélique Kidjo'),(25,NULL,'Sauti Sol',240,2020,'Suzanna'),(26,NULL,'Runtown & Nasty C',270,2018,'No Permission 2.0'),(27,NULL,'King Sunny Adé',360,1982,'Ja Funmi'),(28,'Utopia','Travis Scott',245,2023,'Meltdown (feat. Drake)'),(29,'Renaissance','Beyoncé',265,2022,'Cuff It'),(30,'SOS','SZA',255,2023,'Snooze'),(31,'Lover','Taylor Swift',235,2019,'Cruel Summer'),(32,'Scorpion','Drake',250,2018,'God\'s Plan'),(33,'÷ (Divide)','Ed Sheeran',260,2017,'Shape of You'),(34,'Planet Her','Doja Cat',220,2021,'Kiss Me More'),(35,'Charlie','Charlie Puth',210,2022,'Light Switch'),(36,'1X1','Sauti Sol x BNXN',200,2023,'My Baby'),(37,NULL,'Tiwa Savage',215,2018,'One Milli'),(38,NULL,'Wizkid & Tems',240,2021,'Mood'),(39,NULL,'CKay',230,2021,'Love Nwantiti (Ah Ah Ah)'),(40,NULL,'Burna Boy',215,2020,'Onyeka (Baby)'),(41,NULL,'DJ Maphorisa',300,2023,'Mnike'),(42,NULL,'Tyla',230,2023,'Water');
/*!40000 ALTER TABLE song ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-07 11:53:27
