package com.clinica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String  nombre;
    @Column
    private String apellido;
    @Column
    private Integer numeroContacto;
    @Column
    private LocalDate fechaIngreso;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "domicilio_id",referencedColumnName = "id")
    private Domicilio domicilio;
    @Column(unique = true)
    private String email;

    public Paciente(Long id, String nombre, String apellido, Integer numeroContacto, LocalDate fechaIngreso, Domicilio domicilio, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroContacto = numeroContacto;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
        this.email = email;
        this.id = id;
    }

    public Paciente(String nombre, String apellido, Integer numeroContacto, LocalDate fechaIngreso, Domicilio domicilio, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroContacto = numeroContacto;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
        this.email = email;
    }

    public Paciente() {
    }

    public Long getId() {
        return id;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }
}
