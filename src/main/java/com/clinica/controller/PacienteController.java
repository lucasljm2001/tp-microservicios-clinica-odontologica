package com.clinica.controller;


import com.clinica.dto.PacienteDTO;
import com.clinica.dto.TurnoDTO;
import com.clinica.dto.PacienteDTO;
import com.clinica.entity.Odontologo;
import com.clinica.entity.Paciente;
import com.clinica.entity.Turno;
import com.clinica.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.clinica.service.PacienteService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paciente") //todo lo que venga con endpoint pacinete
@CrossOrigin(origins = "*")
public class PacienteController {
    private PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<com.clinica.dto.PacienteDTO> buscarPorId(@PathVariable Long id) throws ResourceNotFoundException {
        PacienteDTO pacienteBuscando= pacienteService.buscarPacientePorId(id);
        return ResponseEntity.ok(pacienteBuscando);
    }
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes(){
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> registrarPaciente(@RequestBody Paciente paciente) {
        // Buscar por email en lugar de id, ya que el id aún no existe
        Optional<Paciente> pacienteExistente = pacienteService.buscarPorEmail(paciente.getEmail());

        if (pacienteExistente.isPresent()) {
            return ResponseEntity.badRequest().build();

        } else {
            PacienteDTO guardado = pacienteService.guardarPaciente(paciente);
            return ResponseEntity.ok(guardado);
        }
    }

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> obtenerPacientes(){
        System.out.println("Obteniendo lista de pacientes...");
        System.out.println(pacienteService.listarPacientes());
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }

    @PutMapping
    public ResponseEntity<Paciente> actualizarPaciente(@RequestBody Paciente paciente){
        pacienteService.actualizar(paciente);
        return ResponseEntity.ok(paciente);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) {
        boolean eliminado = pacienteService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Paciente eliminado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<Paciente> buscarPacientePorNombre(@RequestParam String nombre) throws ResourceNotFoundException {
        Paciente pacienteBuscado = pacienteService.buscarGenerico(nombre);
        return ResponseEntity.ok(pacienteBuscado);
    }

}
