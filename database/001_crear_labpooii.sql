-- =====================================================================
-- Migración 001: creación de la base de datos LABPOOII, usuario y
-- tabla persona con datos iniciales (Laboratorio 1 - PPOOII)
-- Ejecutar con un usuario administrador (root):
--   mysql -u root -p < database/001_crear_labpooii.sql
-- =====================================================================

-- Base de datos
CREATE DATABASE IF NOT EXISTS `LABPOOII`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

-- Usuario de la aplicación (ver application.properties)
CREATE USER IF NOT EXISTS 'poo'@'localhost' IDENTIFIED BY 'poo';
GRANT ALL PRIVILEGES ON `LABPOOII`.* TO 'poo'@'localhost';
FLUSH PRIVILEGES;

USE `LABPOOII`;

-- LABPOOII.persona definition
CREATE TABLE IF NOT EXISTS `persona` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `PNOMBRE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `EDAD` int DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Datos iniciales
INSERT INTO `persona` (`ID`, `PNOMBRE`, `EDAD`) VALUES
  (1, 'Jose', 35),
  (2, 'Luisa', 27),
  (3, 'Pedro', 28),
  (4, 'Marcela', 21)
ON DUPLICATE KEY UPDATE
  `PNOMBRE` = VALUES(`PNOMBRE`),
  `EDAD` = VALUES(`EDAD`);
