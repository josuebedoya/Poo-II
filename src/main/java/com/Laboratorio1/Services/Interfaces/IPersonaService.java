package com.Laboratorio1.Services.Interfaces;
	
import org.springframework.data.domain.Pageable;
import com.Laboratorio1.Entities.Persona;
import java.util.List;

public interface IPersonaService {

    //METODOS CRUD
    public boolean guardar(Persona persona);

    public boolean actualizar(Persona persona);

    public boolean eliminar(int id);

    public List<Persona> consultarPersona(Pageable pageable);


    //LISTA DE PERSONA POR ID
    public Persona findById(int id);

    //LISTA DE PERSONA POR PRIMER NOMBRE
    public List<Persona> findByNombre(String pnombre);

    //LISTA DE PERSONA POR EDAD
    public List<Persona> findByEdad(int edad);
}