package com.Laboratorio1.Services.Interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.Laboratorio1.Entities.Coordenadas;

public interface ICoordenadasService {

    List<Coordenadas> consultarAllCoordenadas(Pageable pageable);
}
