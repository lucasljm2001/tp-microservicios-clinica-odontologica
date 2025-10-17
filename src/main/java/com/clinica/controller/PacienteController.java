package com.clinica.controller;


import com.clinica.model.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.clinica.service.PacienteService;

import java.util.List;

@RestController //Sin tecnologia de vista
@RequestMapping("/paciente") //todo lo que venga con endpoint pacinete
@CrossOrigin(origins = "*") //Cualquier dominio puede consumir mi api
public class PacienteController {
    //Quien representa el modelo DAO?
        private PacienteService pacienteService;
    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }
    //aqui deberian venir todos los metodos que conectan al com.clinica.service
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Integer id){
        Paciente pacienteBuscando= pacienteService.buscar(id);
        if(pacienteBuscando.getId()!=null){
              return ResponseEntity.ok(pacienteBuscando);
    }
        else{
        return ResponseEntity.notFound().build(); //404
        }
    }
    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes(){
        return ResponseEntity.ok(pacienteService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente){
        return ResponseEntity.ok(pacienteService.guardar(paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Integer id){
        pacienteService.eliminar(id);
        return ResponseEntity.ok("Paciente eliminado");
    }

    @PutMapping
    public ResponseEntity<Paciente> actualizarPaciente(@RequestBody Paciente paciente){
        pacienteService.actualizar(paciente);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Paciente> buscarPacientePorNombre(@RequestParam String nombre){
        Paciente pacienteBuscado= pacienteService.buscarGenerico(nombre);
        if(pacienteBuscado!=null){
            return ResponseEntity.ok(pacienteBuscado);
        }
        else{
            return ResponseEntity.notFound().build(); //404
        }
    }

}
