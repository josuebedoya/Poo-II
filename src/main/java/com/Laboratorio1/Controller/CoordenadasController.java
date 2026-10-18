package com.Laboratorio1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Laboratorio1.Entities.Coordenadas;
import com.Laboratorio1.Services.CoordenadasServiceImpl;

@RestController
@RequestMapping("/LaboratorioV1")
public class CoordenadasController {

    // ==========INYECCION DEL SERVICE==========
    @Autowired
    @Qualifier("CoordenadasService")
    private CoordenadasServiceImpl coordenadaService;

    // ==========MÉTODOS HTTP==================
    // MÉTODO GET
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/coordenadas")
    public List<Coordenadas> consultarAllCoordenadas(Pageable pageable) {
        return coordenadaService.consultarAllCoordenadas(pageable);
    }

}
