package com.clinica.service;

import com.clinica.dto.PacienteDTO;
import com.clinica.entity.Paciente;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService{
    @Autowired
    private PacienteRepository pacienteRepository;

    public List<PacienteDTO> listarPacientes() {
        List<Paciente> pacientes= pacienteRepository.findAll();
        return pacientes.stream().map(this::pacienteAPacienteDTO).toList();
    }

    public PacienteDTO guardarPaciente(Paciente paciente){
        Paciente pacienteGuardado= pacienteRepository.save(paciente);
        return pacienteAPacienteDTO(pacienteGuardado);
    }
    public PacienteDTO pacienteAPacienteDTO(Paciente paciente){
        PacienteDTO pacienteDTO= new PacienteDTO();
        pacienteDTO.setId(paciente.getId());
        pacienteDTO.setNombre(paciente.getNombre());
        pacienteDTO.setApellido(paciente.getApellido());
        pacienteDTO.setEmail(paciente.getEmail());
        pacienteDTO.setDomicilioID(paciente.getDomicilio().getId());
        pacienteDTO.setNumeroContacto(paciente.getNumeroContacto());

        return pacienteDTO;
    }
    public PacienteDTO buscarPacientePorId(Long id) throws ResourceNotFoundException {
        Paciente pacienteBuscado= pacienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
        return pacienteAPacienteDTO(pacienteBuscado);
    }
    public Paciente buscarPorEmail(String email) throws ResourceNotFoundException {
        return pacienteRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con email: " + email));
    }

    public void actualizar(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    public void eliminar(Long id) throws ResourceNotFoundException {
        Paciente pacienteOpt = pacienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
        pacienteRepository.deleteById(id);
    }

    public Paciente buscarGenerico(String parametro) throws ResourceNotFoundException {
        return pacienteRepository.findByNombre(parametro).orElseThrow(()
                -> new ResourceNotFoundException("Paciente no encontrado con el nombre: " + parametro));
    }

    public Optional<Paciente> buscarId(Long id) {
        return pacienteRepository.findById(id);
    }
}
