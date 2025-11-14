package com.clinica.controller;


import com.clinica.dto.PacienteDTO;
import com.clinica.entity.Paciente;

import com.clinica.entity.Domicilio;
import com.clinica.dto.DomicilioDTO;
import com.clinica.exception.PacienteExistenteException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.exception.TurnoComprometidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.clinica.service.PacienteService;
import com.clinica.service.DomicilioService;

import java.util.List;

@RestController
@RequestMapping("/paciente")
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
        try {
            Paciente pacienteExistente = pacienteService.buscarPorEmail(paciente.getEmail());
            throw new PacienteExistenteException("Paciente ya existente con email: " + paciente.getEmail());
        } catch (ResourceNotFoundException e) {
            Paciente nuevoPaciente = new Paciente();
            nuevoPaciente.setNombre(paciente.getNombre());
            nuevoPaciente.setApellido(paciente.getApellido());
            nuevoPaciente.setEmail(paciente.getEmail());
            nuevoPaciente.setNumeroContacto(paciente.getNumeroContacto());
            nuevoPaciente.setFechaIngreso(paciente.getFechaIngreso());
            if (paciente.getDomicilio() != null) {
                DomicilioDTO domDto = paciente.getDomicilio();
                if (domDto.getId() != null) {
                    var domOpt = domicilioService.buscarPorId(domDto.getId());
                    if (domOpt.isPresent()) {
                        nuevoPaciente.setDomicilio(domOpt.get());
                    } else {
                        Domicilio nuevoDom = new Domicilio(domDto.getCalle(), domDto.getNumero(), domDto.getLocalidad(), domDto.getProvincia());
                        Domicilio domGuardado = domicilioService.registrarDomicilio(nuevoDom);
                        nuevoPaciente.setDomicilio(domGuardado);
                    }
                } else {
                    Domicilio nuevoDom = new Domicilio(domDto.getCalle(), domDto.getNumero(), domDto.getLocalidad(), domDto.getProvincia());
                    Domicilio domGuardado = domicilioService.registrarDomicilio(nuevoDom);
                    nuevoPaciente.setDomicilio(domGuardado);
                }
            }
            PacienteDTO guardado = pacienteService.guardarPaciente(nuevoPaciente);
            return ResponseEntity.ok(guardado);
        }
    }



    @PutMapping
    public ResponseEntity<PacienteDTO> actualizarPaciente(@RequestBody PacienteDTO pacienteDto) throws ResourceNotFoundException {
        if (pacienteDto.getId() == null) {
            throw new ResourceNotFoundException("Debe proveer el id del paciente a actualizar");
        }
        Paciente pacienteExistente = pacienteService.buscarId(pacienteDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + pacienteDto.getId()));
        pacienteExistente.setNombre(pacienteDto.getNombre());
        pacienteExistente.setApellido(pacienteDto.getApellido());
        pacienteExistente.setEmail(pacienteDto.getEmail());
        pacienteExistente.setNumeroContacto(pacienteDto.getNumeroContacto());
        pacienteExistente.setFechaIngreso(pacienteDto.getFechaIngreso());
        if (pacienteDto.getDomicilio() != null) {
            DomicilioDTO domDto = pacienteDto.getDomicilio();
            if (domDto.getId() != null) {
                var domOpt = domicilioService.buscarPorId(domDto.getId());
                if (domOpt.isPresent()) {
                    pacienteExistente.setDomicilio(domOpt.get());
                } else {
                    Domicilio nuevoDom = new Domicilio(domDto.getCalle(), domDto.getNumero(), domDto.getLocalidad(), domDto.getProvincia());
                    Domicilio domGuardado = domicilioService.registrarDomicilio(nuevoDom);
                    pacienteExistente.setDomicilio(domGuardado);
                }
            } else {
                Domicilio nuevoDom = new Domicilio(domDto.getCalle(), domDto.getNumero(), domDto.getLocalidad(), domDto.getProvincia());
                Domicilio domGuardado = domicilioService.registrarDomicilio(nuevoDom);
                pacienteExistente.setDomicilio(domGuardado);
            }
        }
        PacienteDTO actualizado = pacienteService.actualizar(pacienteExistente);
        return ResponseEntity.ok(actualizado);
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
