-- =====================================================================
-- Migración 003: ubicación de persona y tabla coordenadas
-- (Laboratorio 3 - tarea programada + Google Maps)
-- Requiere haber ejecutado 001 y 002. Ejecutar una sola vez:
--   mysql -u root -p < database/003_crear_tabla_coordenadas.sql
-- =====================================================================

USE `LABPOOII`;

-- Dirección de la persona que la tarea programada convierte en coordenadas
ALTER TABLE `persona`
  ADD COLUMN `UBICACION` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL;

UPDATE `persona` SET `UBICACION` = 'Universidad de Ibagué, Ibagué, Tolima'        WHERE `ID` = 1;
UPDATE `persona` SET `UBICACION` = 'Universidad del Tolima, Ibagué, Tolima'       WHERE `ID` = 2;
UPDATE `persona` SET `UBICACION` = 'Centro Comercial Multicentro, Ibagué, Tolima' WHERE `ID` = 3;
UPDATE `persona` SET `UBICACION` = 'Parque Centenario, Ibagué, Tolima'            WHERE `ID` = 4;

-- LABPOOII.coordenadas definition
CREATE TABLE IF NOT EXISTS `coordenadas` (
  `id_coordenada` bigint NOT NULL AUTO_INCREMENT,
  `persona` bigint NOT NULL,
  `marca` varchar(100) NOT NULL,
  `longitud` double NOT NULL,
  `latitud` double NOT NULL,
  PRIMARY KEY (`id_coordenada`),
  KEY `coordenadas_persona_FK` (`persona`),
  CONSTRAINT `coordenadas_persona_FK` FOREIGN KEY (`persona`) REFERENCES `persona` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
