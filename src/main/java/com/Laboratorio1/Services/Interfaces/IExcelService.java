package com.Laboratorio1.Services.Interfaces;

public interface IExcelService {

    boolean cargarDatosExcel(String pnombre, String edad, String ubicacion, Double idCargue);

    boolean validarDatosExcel(Double idCargue);

    boolean procesarDatosExcel(Double idCargue);
}
