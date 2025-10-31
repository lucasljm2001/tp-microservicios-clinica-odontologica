package com.clinica.service;

import com.clinica.DTO.PacienteDTO;
import com.clinica.DTO.TurnoDTO;
import com.clinica.entity.Odontologo;
import com.clinica.entity.Paciente;
import com.clinica.entity.Turno;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService{
    @Autowired
    private PacienteRepository pacienteRepository;

    public List<PacienteDTO> listarPacientes() {
        List<Paciente> pacientes= pacienteRepository.findAll();
        return pacientes.stream().map(this::PacienteAPacienteDTO).toList();
    }

    public PacienteDTO guardarPaciente(Paciente paciente){
        Paciente pacienteGuardado= pacienteRepository.save(paciente);
        return PacienteAPacienteDTO(pacienteGuardado);
    }
    public PacienteDTO PacienteAPacienteDTO(Paciente paciente){
        PacienteDTO pacienteDTO= new PacienteDTO();
        pacienteDTO.setId(paciente.getId());
        pacienteDTO.setNombre(paciente.getNombre());
        pacienteDTO.setApellido(paciente.getApellido());
        pacienteDTO.setEmail(paciente.getEmail());
        pacienteDTO.setDomicilioID(paciente.getDomicilio().getId());
        pacienteDTO.setNumeroContacto(paciente.getNumeroContacto());

        return pacienteDTO;
    }
    public Optional<PacienteDTO> buscarPacientePorId(Long id) {
        Optional<Paciente> pacienteBuscado= pacienteRepository.findById(id);
        return pacienteBuscado.map(this::PacienteAPacienteDTO);
    }
    public Optional<Paciente> buscarPorEmail(String email){
        return pacienteRepository.findByEmail(email);
    }

    public void actualizar(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    public boolean eliminar(Long id) {
        Optional<Paciente> pacienteOpt= pacienteRepository.findById(id);
        if(pacienteOpt.isPresent()){
            pacienteRepository.deleteById(id);
            return true;
        }
        return false;
        }
    public Paciente buscarGenerico(String parametro) throws ResourceNotFoundException {
        return pacienteRepository.findByNombre(parametro).orElseThrow(()
                -> new ResourceNotFoundException("Paciente no encontrado con el nombre: " + parametro));
    }

    public Optional<Paciente> buscarId(Long id) {
        return pacienteRepository.findById(id);
    }
}
