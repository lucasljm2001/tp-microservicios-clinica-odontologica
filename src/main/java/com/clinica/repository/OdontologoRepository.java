package com.clinica.repository;

import com.clinica.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OdontologoRepository  extends JpaRepository<Odontologo,Long> {
    public Odontologo findByNombre(String nombre);

    Optional<Odontologo> findByMatricula(String matricula);
}
