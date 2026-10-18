-- =====================================================================
-- Migración 002: tabla usuario (Laboratorio 2 - seguridad JWT)
-- Clave primaria compuesta (login, persona) y llave foránea a persona.
-- Requiere haber ejecutado 001_crear_labpooii.sql
--   mysql -u root -p < database/002_crear_tabla_usuario.sql
-- =====================================================================

USE `LABPOOII`;

-- LABPOOII.usuario definition
CREATE TABLE IF NOT EXISTS `usuario` (
  `login` varchar(100) NOT NULL,
  `persona` bigint NOT NULL,
  `password` varchar(100) NOT NULL,
  `apikey` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`login`,`persona`),
  KEY `usuario_persona_FK` (`persona`),
  CONSTRAINT `usuario_persona_FK` FOREIGN KEY (`persona`) REFERENCES `persona` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Usuario de prueba (luis / 12345, APIKey 12345) asociado a la persona 1
INSERT INTO `usuario` (`login`, `persona`, `password`, `apikey`) VALUES
  ('luis', 1, '12345', '12345')
ON DUPLICATE KEY UPDATE
  `password` = VALUES(`password`),
  `apikey` = VALUES(`apikey`);
