package com.clinica.service;


import com.clinica.dto.TurnoDTO;
import com.clinica.entity.Odontologo;
import com.clinica.entity.Paciente;
import com.clinica.entity.Turno;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.repository.OdontologoRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {
    @Autowired
    private TurnoRepository turnoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private OdontologoRepository odontologoRepository;

    public TurnoDTO guardarTurno(Long pacienteId, Long odontologoId, TurnoDTO turnoDTO) throws ResourceNotFoundException {
        Turno turno= new Turno();
        var pacienteOpt= pacienteRepository.findById(pacienteId);
        if(pacienteOpt.isEmpty()){
            throw new ResourceNotFoundException("Paciente no encontrado con id: " + pacienteId);
        }
        var odontologoOpt= odontologoRepository.findById(odontologoId);
        if(odontologoOpt.isEmpty()){
            throw new ResourceNotFoundException("Odontologo no encontrado con id: " + odontologoId);
        }
        turno.setPaciente(pacienteOpt.get());
        turno.setOdontologo(odontologoOpt.get());
        turno.setFecha(turnoDTO.getFecha());

        Turno turnoGuardado= turnoRepository.save(turno);
        return turnoATurnoDTO(turnoGuardado);
    }
    public TurnoDTO turnoATurnoDTO(Turno turno){
        TurnoDTO turnoDTO= new TurnoDTO();
        turnoDTO.setId(turno.getId());
        // Evitar NPE si el turno no tiene paciente u odontologo asociados
        if (turno.getPaciente() != null) {
            turnoDTO.setPacienteId(turno.getPaciente().getId());
        } else {
            turnoDTO.setPacienteId(null);
        }
        if (turno.getOdontologo() != null) {
            turnoDTO.setOdontologoId(turno.getOdontologo().getId());
        } else {
            turnoDTO.setOdontologoId(null);
        }
        turnoDTO.setFecha(turno.getFecha());
        return turnoDTO;
    }

    public List<TurnoDTO> listarTurnos() {
        List<Turno> turnos= turnoRepository.findAll();
        return turnos.stream().map(this::turnoATurnoDTO).toList();
    }

    public void eliminarTurno(Long id) {
        turnoRepository.deleteById(id);
    }

    public TurnoDTO buscarTurnoPorId(Long id) throws ResourceNotFoundException {
        Turno turnoBuscado= turnoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id: " + id));
        return turnoATurnoDTO(turnoBuscado);
    }

    public TurnoDTO actualizar(TurnoDTO turno) {
        Turno turnoEntity= turnoRepository.getReferenceById(turno.getId());
        Odontologo odontologo = odontologoRepository.getReferenceById(turno.getOdontologoId());
        Paciente paciente = pacienteRepository.getReferenceById(turno.getPacienteId());
        Turno updatedTurno= new Turno();
        updatedTurno.setId(turnoEntity.getId());
        updatedTurno.setFecha(turno.getFecha());
        updatedTurno.setOdontologo(odontologo);
        updatedTurno.setPaciente(paciente);
        return turnoATurnoDTO(turnoRepository.save(updatedTurno));
    }
}
