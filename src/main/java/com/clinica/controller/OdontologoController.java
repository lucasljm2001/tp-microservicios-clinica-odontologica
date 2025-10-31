package com.clinica.controller;
import com.clinica.entity.Odontologo;
import com.clinica.exception.OdontologoExistenteException;
import com.clinica.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.clinica.service.OdontologoService;

import com.clinica.dto.OdontologoDTO;
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
    //Quien representa el modelo DAO?
    @Autowired
    private OdontologoService odontologoService;

    //aqui deberian venir todos los metodos que conectan al com.clinica.service
    @GetMapping("/{id}")
    public ResponseEntity<OdontologoDTO> buscarPorId(@PathVariable Long id) throws ResourceNotFoundException {
        OdontologoDTO OdontologoBuscando= odontologoService.buscarOdontologoPorId(id);
        return ResponseEntity.ok(OdontologoBuscando);
    }
    @GetMapping
    public ResponseEntity<List<OdontologoDTO>> listarOdontologos(){
        return ResponseEntity.ok(odontologoService.listarOdontologos());
    }

    @PutMapping
    public ResponseEntity<Odontologo> actualizarOdontologo(@RequestBody Odontologo odontologo){
       odontologoService.actualizar(odontologo);
       return ResponseEntity.ok(odontologo);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Odontologo> buscarPorNombre(@RequestParam String matricula) throws ResourceNotFoundException {
        Odontologo odontologo = odontologoService.buscarPorMatricula(matricula);
        return ResponseEntity.ok(odontologo);
    }


    @Autowired
    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }
    @PostMapping
    public ResponseEntity<OdontologoDTO> registrarOdontologo(@RequestBody Odontologo odontologo) throws ResourceNotFoundException {
        // Buscar por matricula en lugar de id, ya que el id aún no existe
        try {
            Odontologo odontologoExistente = odontologoService.buscarPorMatricula(odontologo.getMatricula());
            throw new OdontologoExistenteException("Odontologo ya existente con matricula: " + odontologo.getMatricula());
        } catch (ResourceNotFoundException e) {
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
    public ResponseEntity<OdontologoDTO> obtenerOdontologoPorId(@PathVariable Long id) throws ResourceNotFoundException {
        OdontologoDTO odontologoBuscado= odontologoService.buscarOdontologoPorId(id);
        return ResponseEntity.ok(odontologoBuscado);
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
