package com.Laboratorio1.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Laboratorio1.Services.ExcelService;

@RestController
@RequestMapping("/LaboratorioV3")
public class CargueFileExcel {

    private final ExcelService excelService;

    public CargueFileExcel(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/upload-excel")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        excelService.processExcel(file);
        return "Archivo subido y procesado exitosamente";
    }
}
