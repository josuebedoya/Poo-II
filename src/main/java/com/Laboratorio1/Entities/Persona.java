package com.Laboratorio1.Entities;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;


@Entity
@Table(name = "Persona", schema = "LABPOOII")
public class Persona implements Serializable{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    public int Id;

    @Column(name="PNombre")
    private String PNombre;

    @Column(name="Edad")
    private int Edad;

    @Column(name="Ubicacion")
    private String Ubicacion;

    public Persona () {}

    public Persona(String PNombre, int Edad) {
        super();
        this.PNombre = PNombre;
        this.Edad = Edad;
    }

    public Persona(int Id, String PNombre, int Edad) {
        super();
        this.Id = Id;
        this.PNombre = PNombre;
        this.Edad = Edad;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPNombre() {
        return PNombre;
    }

    public void setPNombre(String pNombre) {
        PNombre = pNombre;
    }

    public int getEdad() {
        return Edad;
    }

    public void setEdad(int edad) {
        Edad = edad;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        return "Persona [id=" + this.Id + ", Primer Nombre=" + this.PNombre + ", Edad=" + this.Edad + ", Ubicacion=" + this.Ubicacion + "]";
    }
}