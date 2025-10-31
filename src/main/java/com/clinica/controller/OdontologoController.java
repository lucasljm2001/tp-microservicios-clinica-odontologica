package com.clinica.controller;
import com.clinica.entity.Odontologo;
import com.clinica.exception.OdontologoExistenteException;
import com.clinica.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.clinica.service.OdontologoService;

import com.clinica.dto.OdontologoDTO;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/odontologo") //todo lo que venga con endpoint pacinete
@CrossOrigin(origins = "*")
public class OdontologoController {
    //Quien representa el modelo DAO?
    @Autowired
    private OdontologoService odontologoService;


    @GetMapping
    public ResponseEntity<List<OdontologoDTO>> listarOdontologos(){
        return ResponseEntity.ok(odontologoService.listarOdontologos());
    }

    @PutMapping
    public ResponseEntity<OdontologoDTO> actualizarOdontologo(@RequestBody OdontologoDTO odontologo) throws ResourceNotFoundException {
       Odontologo odontologObtenido = odontologoService.buscarPorMatricula(odontologo.getMatricula());
       odontologoService.actualizar(odontologObtenido);
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
    public ResponseEntity<OdontologoDTO> registrarOdontologo(@RequestBody OdontologoDTO odontologo) throws OdontologoExistenteException {
        // Buscar por matricula en lugar de id, ya que el id aún no existe
        try {
            Odontologo odontologoExistente = odontologoService.buscarPorMatricula(odontologo.getMatricula());
            throw new OdontologoExistenteException("Odontologo ya existente con matricula: " + odontologo.getMatricula());
        } catch (ResourceNotFoundException e) {
            Odontologo nuevoOdontologo = new Odontologo(odontologo.getNombre(), odontologo.getApellido(), odontologo.getMatricula());
            OdontologoDTO guardado = odontologoService.guardarOdontologo(nuevoOdontologo);
            return ResponseEntity.ok(guardado);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<OdontologoDTO> obtenerOdontologoPorId(@PathVariable Long id) throws ResourceNotFoundException {
        OdontologoDTO odontologoBuscado= odontologoService.buscarOdontologoPorId(id);
        return ResponseEntity.ok(odontologoBuscado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarOdontologo(@PathVariable Long id) throws ResourceNotFoundException {
        odontologoService.eliminar(id);
        return ResponseEntity.ok("Odontologo eliminado");
    }

}

