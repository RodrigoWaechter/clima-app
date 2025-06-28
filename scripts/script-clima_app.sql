-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: clima_app
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `configuracoes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuracoes` (
  `id_configuracao` int(11) NOT NULL AUTO_INCREMENT,
  `cidade_preferida` varchar(255) DEFAULT NULL,
  `tema_app` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_configuracao`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dados_diarios`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dados_diarios` (
  `id_dado_diario` int(11) NOT NULL AUTO_INCREMENT,
  `id_localizacao` int(11) NOT NULL,
  `data` date NOT NULL,
  `temperatura_max` decimal(5,2) DEFAULT NULL,
  `temperatura_min` decimal(5,2) DEFAULT NULL,
  `precipitacao_total` decimal(5,2) DEFAULT NULL,
  `velocidade_vento_max` decimal(5,2) DEFAULT NULL,
  `cd_clima` int(11) NOT NULL,
  PRIMARY KEY (`id_dado_diario`),
  UNIQUE KEY `unq_local_data` (`id_localizacao`,`data`),
  CONSTRAINT `dados_diarios_ibfk_1` FOREIGN KEY (`id_localizacao`) REFERENCES `localizacoes` (`id_localizacao`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=939 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dados_horarios`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dados_horarios` (
  `id_dado_horario` int(11) NOT NULL AUTO_INCREMENT,
  `id_localizacao` int(11) NOT NULL,
  `horario` datetime NOT NULL,
  `temperatura` decimal(5,2) DEFAULT NULL,
  `umidade_relativa` decimal(5,2) DEFAULT NULL,
  `sensacao_termica` decimal(5,2) DEFAULT NULL,
  `velocidade_vento` decimal(5,2) DEFAULT NULL,
  `direcao_vento` smallint(6) DEFAULT NULL,
  `precipitacao` decimal(5,2) DEFAULT NULL,
  `cd_clima` int(11) NOT NULL,
  PRIMARY KEY (`id_dado_horario`),
  UNIQUE KEY `unq_local_horario` (`id_localizacao`,`horario`),
  CONSTRAINT `dados_horarios_ibfk_1` FOREIGN KEY (`id_localizacao`) REFERENCES `localizacoes` (`id_localizacao`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9093 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `localizacoes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `localizacoes` (
  `id_localizacao` int(11) NOT NULL AUTO_INCREMENT,
  `nome_cidade` varchar(255) NOT NULL,
  `latitude` decimal(10,8) NOT NULL,
  `longitude` decimal(11,8) NOT NULL,
  `data_hora_registro` datetime NOT NULL,
  PRIMARY KEY (`id_localizacao`),
  UNIQUE KEY `nome_cidade` (`nome_cidade`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'clima_app'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-27 21:02:22
