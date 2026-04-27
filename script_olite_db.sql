CREATE DATABASE  IF NOT EXISTS `olite_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `olite_db`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: olite_db
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `carrito`
--

DROP TABLE IF EXISTS `carrito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrito` (
  `id_carrito` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_carrito`),
  UNIQUE KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `fk_carrito_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito`
--

LOCK TABLES `carrito` WRITE;
/*!40000 ALTER TABLE `carrito` DISABLE KEYS */;
INSERT INTO `carrito` VALUES (1,2,'2026-04-15 14:29:15'),(2,1,'2026-04-15 14:29:15');
/*!40000 ALTER TABLE `carrito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carrito_producto`
--

DROP TABLE IF EXISTS `carrito_producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrito_producto` (
  `id_carrito_producto` int NOT NULL AUTO_INCREMENT,
  `id_carrito` int NOT NULL,
  `id_producto` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unidad` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_carrito_producto`),
  KEY `fk_cp_carrito` (`id_carrito`),
  KEY `fk_cp_producto` (`id_producto`),
  CONSTRAINT `fk_cp_carrito` FOREIGN KEY (`id_carrito`) REFERENCES `carrito` (`id_carrito`) ON DELETE CASCADE,
  CONSTRAINT `fk_cp_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`),
  CONSTRAINT `carrito_producto_chk_1` CHECK ((`cantidad` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito_producto`
--

LOCK TABLES `carrito_producto` WRITE;
/*!40000 ALTER TABLE `carrito_producto` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrito_producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `id_pedido` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `fecha_pedido` datetime DEFAULT CURRENT_TIMESTAMP,
  `total` decimal(10,2) NOT NULL,
  `estado_pedido` varchar(30) DEFAULT 'PENDIENTE',
  `metodo_pago` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_pedido`),
  KEY `fk_pedido_usuario` (`id_usuario`),
  CONSTRAINT `fk_pedido_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (1,1,'2026-04-15 15:30:56',5.95,'ENVIADO','Tarjeta'),(2,1,'2026-04-15 20:27:22',13.90,'FINALIZADO','Tarjeta'),(3,1,'2026-04-15 20:40:33',13.90,'PENDIENTE','Tarjeta'),(4,1,'2026-04-15 20:45:30',75.35,'PENDIENTE','Tarjeta'),(5,1,'2026-04-16 23:25:24',14.90,'ENVIADO','Tarjeta');
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_detalle`
--

DROP TABLE IF EXISTS `pedido_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido_detalle` (
  `id_pedido_detalle` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int NOT NULL,
  `id_producto` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_historico_venta` decimal(10,2) NOT NULL,
  `estado` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_pedido_detalle`),
  KEY `fk_pd_pedido` (`id_pedido`),
  KEY `fk_pd_producto` (`id_producto`),
  CONSTRAINT `fk_pd_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`) ON DELETE CASCADE,
  CONSTRAINT `fk_pd_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_detalle`
--

LOCK TABLES `pedido_detalle` WRITE;
/*!40000 ALTER TABLE `pedido_detalle` DISABLE KEYS */;
INSERT INTO `pedido_detalle` VALUES (1,1,1,1,5.95,'PENDIENTE'),(2,2,3,2,6.95,'PENDIENTE'),(3,3,3,2,6.95,'PENDIENTE'),(4,4,3,3,6.95,'PENDIENTE'),(5,4,2,5,4.95,'PENDIENTE'),(6,4,1,5,5.95,'PENDIENTE'),(7,5,4,1,14.90,'PENDIENTE');
/*!40000 ALTER TABLE `pedido_detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre_producto` varchar(100) NOT NULL,
  `descripcion` text,
  `precio` decimal(10,2) NOT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `imagen` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (1,'Jabón Seda Blanca','Jabón artesanal 100% natural para cuerpo y manos. Elaborado con aceite de oliva virgen extra, almendras dulces y arcilla blanca de caolín. Aporta suavidad y sedosidad a todo tipo de pieles, incluso las más sensibles.',6.95,50,'http://10.0.2.2:8080/uploads/jabon_sedablanca.png'),(2,'Desodorante Frescor Silvestre','Desodorante natural y cremoso, protección 100% natural durante todo el día. Sin parabenos, aluminio ni químicos agresivos. Con aceite de coco, cera de abejas y aceite esencial de árbol de té.',4.95,20,'http://10.0.2.2:8080/uploads/frescor_silvestre.png'),(3,'Loción Ultra Ligera','Crema de manos de rápida absorción y textura ligera. Hidrata y suaviza sin sensación grasa. Con aceite de almendras dulces, glicerina vegetal y vitamina E. Aroma natural a árbol de té y naranja.',6.95,33,'http://10.0.2.2:8080/uploads/ultraligera.png'),(4,'Luffa','Esponja vegetal 100% natural y biodegradable, cultivada en proximidad. Exfoliante suave para la piel y útil también para la limpieza del hogar. Producto sostenible y respetuoso con el medio ambiente.',14.90,14,'http://10.0.2.2:8080/uploads/luffa.png'),(5,'Morritos Naturalmente Jugosos','Bálsamo labial natural que hidrata, nutre y protege los labios. Fórmula artesanal con ingredientes ecológicos que deja los labios suaves y jugosos de forma natural.',6.95,35,'http://10.0.2.2:8080/uploads/morritos.png'),(6,'Refléjame el Sol','Protector solar respetuoso con tu piel y el medio ambiente. Fórmula natural que protege sin dañar los ecosistemas marinos, ideal para el cuidado diario bajo el sol.',10.95,20,'http://10.0.2.2:8080/uploads/reflejame_sol.png');
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL DEFAULT 'ROLE_USER',
  `activo` tinyint(1) DEFAULT '1',
  `fecha_registro` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Dani','Ejemplo','Calle Mar Menor 4','617345675','dani@ejemplo.com','$2a$10$MVz2GLPl9S3vsezM6s.Pk.WTMIi1QyomAS/2RG92tE74FSp1qaMXm','ROLE_USER',1,'2026-03-30 18:47:28'),(2,'Admin','Olité','Calle Admin 1','600000000','admin@olite.com','$2a$10$J8ZaaXPpaQM.oVsy.Ceaw.J/2J91cVlZtldxcol9eeuXiKBf5f/LW','ROLE_ADMIN',1,'2026-03-30 19:06:13');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-18 10:43:31
