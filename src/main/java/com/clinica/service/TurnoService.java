package com.clinica.service;


import com.clinica.dto.TurnoDTO;
import com.clinica.entity.Turno;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {
    @Autowired
    private TurnoRepository turnoRepository;

    public TurnoDTO guardarTurno(Turno turno){
        Turno turnoGuardado= turnoRepository.save(turno);
        return turnoATurnoDTO(turnoGuardado);
    }
    public TurnoDTO turnoATurnoDTO(Turno turno){
        TurnoDTO turnoDTO= new TurnoDTO();
        turnoDTO.setId(turno.getId());
        turnoDTO.setPacienteId(turno.getPaciente().getId());
        turnoDTO.setOdontologoId(turno.getOdontologo().getId());
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

    public void actualizar(Turno turno) {
        turnoRepository.save(turno);
    }
}
