package com.clinica.controller;
import com.clinica.model.Odontologo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.clinica.service.OdontologoService;

import java.util.List;

@RestController //Sin tecnologia de vista
@RequestMapping("/odontologo") //todo lo que venga con endpoint Odonotlogo
@CrossOrigin(origins = "*") //Cualquier dominio puede consumir mi api
public class OdontologoController {
    //Quien representa el modelo DAO?
    private OdontologoService odontologoService;
    @Autowired
    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }
    //aqui deberian venir todos los metodos que conectan al com.clinica.service
    @GetMapping("/{id}")
    public ResponseEntity<Odontologo> buscarPorId(@PathVariable Integer id){
        Odontologo OdontologoBuscando= odontologoService.buscar(id);
        if(OdontologoBuscando.getId()!=null){
            return ResponseEntity.ok(OdontologoBuscando);
        }
        else{
            return ResponseEntity.notFound().build(); //404
        }
    }
    @GetMapping
    public ResponseEntity<List<Odontologo>> listarOdontologos(){
        return ResponseEntity.ok(odontologoService.buscarTodos());
    }
    @PostMapping
    public ResponseEntity<Odontologo> registrarOdontologo(@RequestBody Odontologo odontologo){
        return ResponseEntity.ok(odontologoService.guardar(odontologo));
    }
    @PutMapping
    public ResponseEntity<Odontologo> actualizarOdontologo(@RequestBody Odontologo odontologo){
       odontologoService.actualizar(odontologo);
         return ResponseEntity.ok(odontologo);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarOdontologo(@PathVariable Integer id) {
        odontologoService.eliminar(id);
        return ResponseEntity.ok("Odontologo con id " + id + " eliminado");
    }
    @GetMapping("/buscar")
    public ResponseEntity<Odontologo> buscarPorNombre(@RequestParam String nombre) {
        Odontologo odontologo = odontologoService.buscarGenerico(nombre);
        if (odontologo != null) {
            return ResponseEntity.ok(odontologo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
