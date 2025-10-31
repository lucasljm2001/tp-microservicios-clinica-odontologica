package com.clinica.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


public class PacienteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaIngreso;
    private Integer numeroContacto;
    private Long domicilioID;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(Integer numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public Long getDomicilioID() {
        return domicilioID;
    }

    public void setDomicilioID(Long domicilioID) {
        this.domicilioID = domicilioID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
