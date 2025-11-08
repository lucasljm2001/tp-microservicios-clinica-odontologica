package com.clinica.controller;


import com.clinica.dto.PacienteDTO;
import com.clinica.dto.TurnoDTO;
import com.clinica.dto.PacienteDTO;
import com.clinica.entity.Odontologo;
import com.clinica.entity.Paciente;
import com.clinica.entity.Turno;
import com.clinica.exception.PacienteExistenteException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.exception.TurnoComprometidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.clinica.service.PacienteService;
import com.clinica.service.DomicilioService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paciente") //todo lo que venga con endpoint pacinete
@CrossOrigin(origins = "*")
public class PacienteController {
    private PacienteService pacienteService;
    private DomicilioService domicilioService;

    @Autowired
    public PacienteController(PacienteService pacienteService, DomicilioService domicilioService) {
        this.pacienteService = pacienteService;
        this.domicilioService = domicilioService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscarPorId(@PathVariable Long id) throws ResourceNotFoundException {
        PacienteDTO pacienteBuscando= pacienteService.buscarPacientePorId(id);
        return ResponseEntity.ok(pacienteBuscando);
    }
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes(){
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> registrarPaciente(@RequestBody PacienteDTO paciente) throws PacienteExistenteException, ResourceNotFoundException{
        // Buscar por email en lugar de id, ya que el id aún no existe
        try {
            Paciente pacienteExistente = pacienteService.buscarPorEmail(paciente.getEmail());
            throw new PacienteExistenteException("Paciente ya existente con email: " + paciente.getEmail());
        } catch (ResourceNotFoundException e) {
            Paciente nuevoPaciente = new Paciente();
            nuevoPaciente.setNombre(paciente.getNombre());
            nuevoPaciente.setApellido(paciente.getApellido());
            nuevoPaciente.setEmail(paciente.getEmail());
            nuevoPaciente.setNumeroContacto(paciente.getNumeroContacto());
            // Asociar domicilio existente si se proporcionó domicilioID
            if (paciente.getDomicilioID() != null) {
                var domOpt = domicilioService.buscarPorId(paciente.getDomicilioID());
                if (domOpt.isPresent()) {
                    nuevoPaciente.setDomicilio(domOpt.get());
                } else {
                    throw new ResourceNotFoundException("Domicilio no encontrado con id: " + paciente.getDomicilioID());
                }
            }
            PacienteDTO guardado = pacienteService.guardarPaciente(nuevoPaciente);
            return ResponseEntity.ok(guardado);
        }
    }



    @PutMapping
    public ResponseEntity<Paciente> actualizarPaciente(@RequestBody Paciente paciente){
        pacienteService.actualizar(paciente);
        return ResponseEntity.ok(paciente);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) throws ResourceNotFoundException, TurnoComprometidoException {
        pacienteService.eliminar(id);
        return ResponseEntity.ok("Paciente eliminado");
    }

    @GetMapping("/buscar")
    public ResponseEntity<Paciente> buscarPacientePorNombre(@RequestParam String nombre) throws ResourceNotFoundException {
        Paciente pacienteBuscado = pacienteService.buscarGenerico(nombre);
        return ResponseEntity.ok(pacienteBuscado);
    }

}
