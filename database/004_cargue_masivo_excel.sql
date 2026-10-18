-- =====================================================================
-- Migración 004: cargue masivo desde Excel (Laboratorio 4)
-- Tabla temporal persona_tmp y procedimientos almacenados
-- CARGAR_DATOS_EXCEL, VALIDAR_DATOS_CARGUE y PROCESAR_DATOS_EXCEL.
-- Requiere 001 a 003. Ejecutar con el cliente mysql (usa DELIMITER):
--   mysql -u root -p < database/004_cargue_masivo_excel.sql
-- =====================================================================

USE `LABPOOII`;

-- CREACIÓN DE TABLA TEMPORAL QUE ALMACENARA EL CARGUE DE
-- DATOS DEL ARCHIVO EXCEL
-- LABPOOII.persona_tmp definition
CREATE TABLE IF NOT EXISTS `persona_tmp` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `PNOMBRE` varchar(100) DEFAULT NULL,
  `EDAD` varchar(3) DEFAULT NULL,
  `UBICACION` varchar(100) DEFAULT NULL,
  `ESTADO` varchar(10) NOT NULL,
  `OBSERVACION` varchar(500) DEFAULT NULL,
  `ID_CARGUE` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  -- RESTRICCIÓN DEL CAMPO ESTADO, EL CUAL DESCRIBE
  -- EL ESTADO DEL CARGUE DEL REGISTRO PERSONA
  CONSTRAINT `persona_tmp_check` CHECK ((`ESTADO` in ('CARGADO','VALIDADO','PROCESADO','ERROR')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP PROCEDURE IF EXISTS CARGAR_DATOS_EXCEL;
DROP PROCEDURE IF EXISTS VALIDAR_DATOS_CARGUE;
DROP PROCEDURE IF EXISTS PROCESAR_DATOS_EXCEL;

-- CREACIÓN DEL PROCEDIMIENTO DE CARGUE INICIAL CON
-- DATOS DE UN ARCHIVO EXCEL A UNA TABLA TEMPORAL DE
-- BASE DE DATOS.
DELIMITER $$

CREATE PROCEDURE CARGAR_DATOS_EXCEL(IN pnombre VARCHAR(100),
                                    IN edad VARCHAR(3),
                                    IN ubicacion VARCHAR(100),
                                    IN idCargue DOUBLE )
BEGIN
    insert into persona_tmp (PNOMBRE,EDAD,UBICACION,ESTADO,OBSERVACION,ID_CARGUE)
    values (pnombre, edad, ubicacion,'CARGADO','',idCargue);
end $$

-- CREACIÓN DEL PROCEDIMIENTO DE VALIDACIÓN DE DATOS
-- CARGADOS EN LA TABLA TEMPORAL SEGÚN LO REQUIERA EL
-- NEGOCIO
CREATE PROCEDURE VALIDAR_DATOS_CARGUE (IN idCargue DOUBLE )
begin
    -- VALIDACIÓN CAMPOS NULOS
    -- VALIDACIÓN DEL CAMPO PNOMBRE SI ES NULL, EL REGISTRO SE MARCA COMO ERROR PARA NO SER PROCESADO.
    update persona_tmp
    set ESTADO = 'ERROR', OBSERVACION= CONCAT (OBSERVACION,' El campo Nombre no puede ser vacio.')
    where ID_CARGUE = idCargue and ESTADO = 'CARGADO' and PNOMBRE is null ;

    commit;

    -- VALIDACIÓN DEL CAMPO EDAD SI ES NULL Y CON TRES CARACTERES COMO MÁXIMO Y QUE ESTE EN EL RANGO DE EDAD DE 18 A 35,
    -- EL REGISTRO SE MARCA COMO ERROR PARA NO SER PROCESADO.
    update persona_tmp
    set ESTADO = 'ERROR', OBSERVACION= CONCAT (OBSERVACION,' El campo Edad no puede ser vacio.')
    where ID_CARGUE = idCargue and ESTADO = 'CARGADO' and EDAD is null ;

    commit;

    -- Validar que tenga como minimo un caracter y maximo 3
    update persona_tmp
    set ESTADO = 'ERROR',
        OBSERVACION= CONCAT (OBSERVACION,' El campo Edad no cumple con las especificaciones numericas.')
    where ID_CARGUE = idCargue and ESTADO = 'CARGADO' and EDAD not REGEXP '^[0-9]{1,3}$' ;

    commit;

    -- Validar el rango de 18 a 35 años de edad (inclusive)
    update persona_tmp
    set ESTADO = 'ERROR', OBSERVACION= CONCAT (OBSERVACION,' El campo Edad no cumple con el rango de valores parametrizados de 18 a 35.')
    where ID_CARGUE = idCargue and ESTADO = 'CARGADO'
      and (CONVERT(EDAD, SIGNED INTEGER) < 18 or CONVERT(EDAD, SIGNED INTEGER) > 35 );

    commit;

    -- VALIDACIÓN DEL CAMPO UBICACION SI ES NULL, EL REGISTRO SE MARCA COMO ERROR PARA NO SER PROCESADO.
    update persona_tmp
    set ESTADO = 'ERROR', OBSERVACION= CONCAT (OBSERVACION,' El campo ubicacion no puede ser vacio.')
    where ID_CARGUE = idCargue and ESTADO = 'CARGADO' and UBICACION is null ;

    commit;

    -- TERMINADO EL BLOQUE DE VALIDACIÓN SE MARCAN LOS REGISTROS QUE PASARON LAS VALIDACIONES CON EL
    -- ESTADO --> VALIDADO, SIENDO ESTOS LOS REGISTROS CANDIDATOS A PROCESAR.
    update persona_tmp
    set ESTADO = 'VALIDADO'
    where ID_CARGUE = idCargue and ESTADO = 'CARGADO' and (OBSERVACION is null or OBSERVACION = '') ;

    commit ;
end $$

-- CREACIÓN DEL PROCEDIMIENTO DE PROCESADO DE LOS
-- DATOS VALIDADOS Y LLEVARLOS A TABLA TRANSACCIONAL
-- DE LA APLICACIÓN.
CREATE PROCEDURE PROCESAR_DATOS_EXCEL(IN idCargue DOUBLE )
begin
    -- INSERTAR EN LA TABLA TRANSACCIONAL LOS REGISTRO VALIDADOS Y VERIFICADOS.
    insert into persona (PNOMBRE,EDAD,UBICACION)
    select PNOMBRE, CONVERT(EDAD, SIGNED INTEGER) as EDAD, UBICACION
    from persona_tmp where ID_CARGUE = idCargue and ESTADO = 'VALIDADO';

    commit;

    -- TERMINADO EL BLOQUE DE PROCESADO DE LOS DATOS VERIFICADOS SE MARCAN LOS REGISTROS CON EL
    -- ESTADO --> PROCESADO, SIENDO ESTOS LOS REGISTROS CARGADOS A LA TABLA TRANSACCIONAL DE LA APLICACIÓN
    update persona_tmp
    set ESTADO = 'PROCESADO'
    where ID_CARGUE = idCargue and ESTADO = 'VALIDADO';

    commit ;
end $$

DELIMITER ;

-- SQL DE PRUEBA PARA EL CONSUMO DE LOS PROCEDIMIENTOS ALMACENADOS
-- call CARGAR_DATOS_EXCEL('Homero', '31', 'Centro Comercial Multicentro, Ibagué, Tolima', 202509301518);
-- call VALIDAR_DATOS_CARGUE(202509301518);
-- call PROCESAR_DATOS_EXCEL(202509301518);
