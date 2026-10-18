package com.Laboratorio1.Services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Laboratorio1.Repository.PersonaRepository;
import com.Laboratorio1.Services.Interfaces.IExcelService;

@Service("ExcelService")
public class ExcelService implements IExcelService{

    // ========== INYECCIÓN DE DEPENDENCIAS ==========
    @Autowired
    @Qualifier("IPersonaRepo")
    private PersonaRepository IPersonaRepository;
    //====================== LOGS ======================
    //LOGS DE ERROR
    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(PersonaServiceImpl.class);

    public void processExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            // Asume que el primer sheet es el que quieres procesar
            Sheet sheet = workbook.getSheetAt(0);
            LocalDateTime now = LocalDateTime.now();
            // Custom format pattern
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            String formattedDateTime = now.format(formatter);
            double idCargue = Double.parseDouble(formattedDateTime);
            // Devuelve el valor de la celda tal como se ve en Excel (ej. 32 y no 32.0)
            DataFormatter dataFormatter = new DataFormatter();
            for (Row row : sheet) {
                // Itera sobre las celdas y extrae los datos
                if(row.getRowNum()>0) {
                    String pnombre = null;
                    String edad = null;
                    String ubicacion = null;
                    for (Cell cell : row) {
                        // Procesa el contenido de la celda
                        String valor = dataFormatter.formatCellValue(cell).trim();
                        if (valor.isEmpty()) {
                            valor = null;
                        }
                        switch (cell.getColumnIndex()) {
                            case 0:  pnombre = valor; break;
                            case 1:  edad = valor; break;
                            case 2:  ubicacion = valor; break;
                        }
                    }
                    // Filas completamente vacías del archivo no se cargan
                    if (pnombre == null && edad == null && ubicacion == null) {
                        continue;
                    }
                    System.out.println("Dato: "+pnombre+" - "+edad+" - "+ubicacion+" - "+idCargue );
                    if(this.cargarDatosExcel(pnombre, edad, ubicacion, idCargue)) {
                        System.out.println("Cargue de datos inicial. EXITOSO!!!!!");
                    }
                }
            }
            workbook.close();
            if(this.validarDatosExcel(idCargue)) {
                System.out.println("Validación de datos cargados. EXITOSO!!!!!");
                if(this.procesarDatosExcel(idCargue))
                    System.out.println("Procesado de datos validados. EXITOSO!!!!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de errores de lectura del archivo
        }
    }

    @Override
    public boolean cargarDatosExcel(String pnombre, String edad, String ubicacion, Double idCargue) {
        try {
            IPersonaRepository.CARGAR_DATOS_EXCEL(pnombre, edad, ubicacion, idCargue);
            logger.info("Se carga registro de excel a tabla temporal de forma exitosa!!!!");
            return true;
        }catch(Exception e) {
            logger.error("ERROR CARGAR DATOS DE ARCHIVO EXCEL: PROBLEMAS CON LA INVOCACIÓN DEL PROCEDIMIENTO ALMACENADO CARGAR_DATOS_EXCEL!", e);
            return false;
        }
    }

    @Override
    public boolean validarDatosExcel(Double idCargue) {
        try {
            IPersonaRepository.VALIDAR_DATOS_CARGUE(idCargue);
            logger.info("Se validan los registros de la tabla temporal de forma exitosa!!!!");
            return true;
        }catch(Exception e) {
            logger.error("ERROR VALIDANDO DATOS DE ARCHIVO EXCEL: PROBLEMAS CON LA INVOCACIÓN DEL PROCEDIMIENTO ALMACENADO VALIDAR_DATOS_CARGUE!", e);
            return false;
        }
    }

    @Override
    public boolean procesarDatosExcel(Double idCargue) {
        try {
            IPersonaRepository.PROCESAR_DATOS_EXCEL(idCargue);
            logger.info("Se procesan los datos validados de forma exitosa y se transladan a la tabla tansaccional!!!!");
            return true;
        }catch(Exception e) {
            logger.error("ERROR PROCESAR DATOS DE ARCHIVO EXCEL: PROBLEMAS CON LA INVOCACIÓN DEL PROCEDIMIENTO ALMACENADO PROCESAR_DATOS_EXCEL!", e);
            return false;
        }
    }
}
