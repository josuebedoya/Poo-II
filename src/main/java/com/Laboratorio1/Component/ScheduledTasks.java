package com.Laboratorio1.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.Laboratorio1.APIs.GoogleMaps.Geocoder;
import com.Laboratorio1.Entities.Coordenadas;
import com.Laboratorio1.Entities.Persona;
import com.Laboratorio1.Repository.CoordenadasRepository;
import com.Laboratorio1.Repository.PersonaRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    // ========== INYECCIÓN DE DEPENDENCIAS ==========
    @Autowired
    @Qualifier("IPersonaRepo")
    private PersonaRepository IPersonaRepository;

    @Autowired
    @Qualifier("ICoordenadasRepository")
    private CoordenadasRepository ICoordenadaRepository;

    // ========== DIRECCIONES ESTÁTICAS (punto 7) ==========
    private static final String[] UBICACIONES = {
            "Universidad de Ibagué, Ibagué, Tolima",
            "Universidad del Tolima, Ibagué, Tolima",
            "Centro Comercial Multicentro, Ibagué, Tolima",
            "Centro Comercial La Estación, Ibagué, Tolima",
            "Parque Centenario, Ibagué, Tolima",
            "Estadio Manuel Murillo Toro, Ibagué, Tolima",
            "Conservatorio del Tolima, Ibagué, Tolima",
            "Panóptico de Ibagué, Ibagué, Tolima",
            "Museo de Arte del Tolima, Ibagué, Tolima",
            "Terminal de Transportes de Ibagué, Ibagué, Tolima"
    };
    private final Random random = new Random();

    // Cada 30 segundos: obtiene las coordenadas de la ubicación de cada persona
    @Scheduled(cron = "*/30 * * * * ?")
    public void scheduleTaskWithCronExpression() {
        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try
        {
            List<Persona> listPersonas = IPersonaRepository.getPersonas();
            if(listPersonas !=null) {
                if(listPersonas.size()>0) {
                    Geocoder geocoder = new Geocoder();
                    Coordenadas coorXper = new Coordenadas();
                    for (Persona persona : listPersonas) {
                        String LatLng = geocoder.getLatLng(persona.getUbicacion());
                        String[] coor = LatLng.split(",");
                        logger.info(LatLng + " - {}", dateTimeFormatter.format(LocalDateTime.now()));

                        coorXper = ICoordenadaRepository.getCoordenadaXPersona(persona.Id);
                        if(coorXper == null) {
                            ICoordenadaRepository.save(new Coordenadas(persona.Id, persona.getPNombre(),
                                    Double.parseDouble(coor[0].toString()),
                                    Double.parseDouble(coor[1].toString())));
                        }else if(coorXper.id>0) {
                            ICoordenadaRepository.save(new Coordenadas(coorXper.id,persona.Id, persona.getPNombre(),
                                    Double.parseDouble(coor[0].toString()),
                                    Double.parseDouble(coor[1].toString())));
                        }

                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ========== PUNTO 7: CAMBIO ALEATORIO DE UBICACIONES ==========
    // Cada 2 minutos (punto 8): asigna una dirección aleatoria a cada persona
    @Scheduled(cron = "0 */2 * * * ?")
    public void cambiarUbicacionesAleatorias() {
        logger.info("Cron Task Ubicaciones Aleatorias :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            List<Persona> listPersonas = IPersonaRepository.findAll();
            for (Persona persona : listPersonas) {
                persona.setUbicacion(UBICACIONES[random.nextInt(UBICACIONES.length)]);
                IPersonaRepository.save(persona);
                logger.info("{} -> {}", persona.getPNombre(), persona.getUbicacion());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ========== PUNTO 8: OTRAS CONFIGURACIONES CRON ==========
    // Todos los lunes a las 5:17 pm
    @Scheduled(cron = "0 17 17 * * MON")
    public void tareaLunes517pm() {
        logger.info("Cron Task Lunes 5:17 pm :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }

    // Tres veces al día (8:00 am, 12:00 m y 6:00 pm) los lunes, miércoles y viernes
    @Scheduled(cron = "0 0 8,12,18 * * MON,WED,FRI")
    public void tareaTresVecesLunMieVie() {
        logger.info("Cron Task 3 veces Lun/Mie/Vie :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }
}
