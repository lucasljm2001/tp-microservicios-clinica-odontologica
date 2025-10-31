package com.clinica.controller;


import com.clinica.DTO.OdontologoDTO;
import com.clinica.entity.Odontologo;
import com.clinica.service.OdontologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/odontologo") //todo lo que venga con endpoint pacinete
@CrossOrigin(origins = "*")
public class OdontologoController {
    private OdontologoService odontologoService;

    @Autowired
    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }
    @PostMapping
    public ResponseEntity<OdontologoDTO> registrarOdontologo(@RequestBody Odontologo odontologo) {
        // Buscar por matricula en lugar de id, ya que el id aún no existe
        Optional<Odontologo> odontologoExistente = odontologoService.buscarPorMatricula(odontologo.getMatricula());

        if (odontologoExistente.isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            OdontologoDTO guardado = odontologoService.guardarOdontologo(odontologo);
            return ResponseEntity.ok(guardado);
        }
    }

    @GetMapping
    public ResponseEntity<List<OdontologoDTO>> obtenerOdontologos(){
        System.out.println("Obteniendo lista de pacientes...");
        System.out.println(odontologoService.listarOdontologos());
        return ResponseEntity.ok(odontologoService.listarOdontologos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<OdontologoDTO> obtenerOdontologoPorId(@PathVariable Long id){
        Optional<OdontologoDTO> odontologoBuscado= odontologoService.buscarOdontologoPorId(id);
        if(odontologoBuscado.isPresent()){
            return ResponseEntity.ok(odontologoBuscado.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping
    public ResponseEntity<Odontologo> actualizarPaciente(@RequestBody Odontologo odontologo){
        odontologoService.actualizar(odontologo);
        return ResponseEntity.ok(odontologo);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarOdontologo(@PathVariable Long id){
        boolean eliminado = odontologoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Odontologo eliminado");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

}
