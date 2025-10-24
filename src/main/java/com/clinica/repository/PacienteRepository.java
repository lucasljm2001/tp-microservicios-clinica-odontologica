package com.clinica.repository;

import com.clinica.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente,Long> {
    Optional<Paciente> findByEmail(String email);

    Optional<Paciente> findByNombre(String nombre);
}
