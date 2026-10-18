package com.Laboratorio1.Repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.Laboratorio1.Entities.Persona;

@Repository("IPersonaRepo")
public interface PersonaRepository extends JpaRepository<Persona, Serializable>, PagingAndSortingRepository<Persona, Serializable>{

    //Hay Métodos que JPA ya los tiene desarrollados, se pueden crear para tener
    //una manipulación más específica a la hora de usarlos en el service

    public abstract Persona findById(int id);

    public abstract List<Persona> findByPNombre(String pnombre);

    public abstract List<Persona> findByEdad(int edad);

    public abstract Page<Persona> findAll(Pageable pageable);

    //PERSONAS CON UBICACION REGISTRADA (tarea programada Laboratorio 3)
    @Query("SELECT p FROM Persona p WHERE p.Ubicacion IS NOT NULL")
    public abstract List<Persona> getPersonas();
    
}