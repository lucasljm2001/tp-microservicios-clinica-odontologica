package com.clinica.service;

import com.clinica.entity.Paciente;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
public class PacienteService implements iService<Paciente> {
    private PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente guardar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente buscar(Long id) throws ResourceNotFoundException {
        return pacienteRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Paciente no encontrado con id: " + id));
    }

    @Override
    public void eliminar(Long id) {
        pacienteRepository.deleteById(id);
    }

    @Override
    public void actualizar(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    @Override
    public Paciente buscarGenerico(String parametro) throws ResourceNotFoundException {
        return pacienteRepository.findByNombre(parametro).orElseThrow(()
                -> new ResourceNotFoundException("Paciente no encontrado con el nombre: " + parametro));
    }

    @Override
    public List<Paciente> buscarTodos() {
        return pacienteRepository.findAll();
    }
}
