package com.clinica.service;

import com.clinica.dto.PacienteDTO;
import com.clinica.dto.DomicilioDTO;
import com.clinica.entity.Domicilio;
import com.clinica.entity.Paciente;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.exception.TurnoComprometidoException;
import com.clinica.repository.DomicilioRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService{
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    public List<PacienteDTO> listarPacientes() {
        List<Paciente> pacientes= pacienteRepository.findAll();
        return pacientes.stream().map(this::pacienteAPacienteDTO).toList();
    }

    public PacienteDTO guardarPaciente(Paciente paciente){
        Domicilio domicilio = paciente.getDomicilio();

        if (domicilio != null && domicilio.getId() == null) {
            domicilioRepository.save(domicilio);
        }
        Paciente pacienteGuardado= pacienteRepository.save(paciente);
        return pacienteAPacienteDTO(pacienteGuardado);
    }
    public PacienteDTO pacienteAPacienteDTO(Paciente paciente){
        PacienteDTO pacienteDTO= new PacienteDTO();
        pacienteDTO.setId(paciente.getId());
        pacienteDTO.setNombre(paciente.getNombre());
        pacienteDTO.setApellido(paciente.getApellido());
        pacienteDTO.setEmail(paciente.getEmail());
        if (paciente.getDomicilio() != null) {
            DomicilioDTO domDto = new DomicilioDTO();
            domDto.setId(paciente.getDomicilio().getId());
            domDto.setCalle(paciente.getDomicilio().getCalle());
            domDto.setNumero(paciente.getDomicilio().getNumero());
            domDto.setLocalidad(paciente.getDomicilio().getLocalidad());
            domDto.setProvincia(paciente.getDomicilio().getProvincia());
            pacienteDTO.setDomicilio(domDto);
        } else {
            pacienteDTO.setDomicilio(null);
        }
        pacienteDTO.setNumeroContacto(paciente.getNumeroContacto());
        pacienteDTO.setFechaIngreso(paciente.getFechaIngreso());

        return pacienteDTO;
    }
    public PacienteDTO buscarPacientePorId(Long id) throws ResourceNotFoundException {
        Paciente pacienteBuscado= pacienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
        return pacienteAPacienteDTO(pacienteBuscado);
    }
    public Paciente buscarPorEmail(String email) throws ResourceNotFoundException {
        return pacienteRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con email: " + email));
    }

    public PacienteDTO actualizar(Paciente paciente) {
        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        return pacienteAPacienteDTO(pacienteGuardado);
    }

    public void eliminar(Long id) throws ResourceNotFoundException, TurnoComprometidoException {
        Paciente pacienteOpt = pacienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
        if(turnoRepository.existsByPacienteId(id)){
            throw new TurnoComprometidoException("No se puede eliminar el paciente con id " + id + " porque tiene turnos asociados.");
        }
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
