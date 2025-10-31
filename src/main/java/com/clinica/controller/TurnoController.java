package com.clinica.controller;


import com.clinica.dto.OdontologoDTO;
import com.clinica.dto.PacienteDTO;
import com.clinica.dto.TurnoDTO;
import com.clinica.entity.Odontologo;
import com.clinica.entity.Paciente;
import com.clinica.entity.Turno;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.service.OdontologoService;
import com.clinica.service.PacienteService;
import com.clinica.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private OdontologoService odontologoService;
    private PacienteService pacienteService;
    private TurnoService turnoService;

    @Autowired
    public TurnoController(OdontologoService odontologoService, PacienteService pacienteService, TurnoService turnoService) {
        this.odontologoService = odontologoService;
        this.pacienteService = pacienteService;
        this.turnoService = turnoService;
    }
    @PostMapping
    public ResponseEntity<TurnoDTO> registrarTurno(@RequestBody Turno turno) throws ResourceNotFoundException {
        PacienteDTO pacienteBuscado= pacienteService.buscarPacientePorId(turno.getPaciente().getId());
        OdontologoDTO odontologoBuscado= odontologoService.buscarOdontologoPorId(turno.getOdontologo().getId());
        return ResponseEntity.ok(turnoService.guardarTurno(turno));
    }
    @GetMapping
    public ResponseEntity<List<TurnoDTO>> obtenerTurnos(){
        return ResponseEntity.ok(turnoService.listarTurnos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTO> obtenerTurnoPorId(@PathVariable Long id) throws ResourceNotFoundException {
        TurnoDTO turnoBuscado= turnoService.buscarTurnoPorId(id);
        return ResponseEntity.ok(turnoBuscado);
    }
    @PutMapping
    public ResponseEntity<Turno> actualizarTurno(@RequestBody Turno turno){
        turnoService.actualizar(turno);
        return ResponseEntity.ok(turno);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTurno(@PathVariable Long id){
        turnoService.eliminarTurno(id);
        return ResponseEntity.ok("Turno eliminado");
    }

}
