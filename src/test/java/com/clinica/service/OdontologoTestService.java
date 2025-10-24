package com.clinica.service;

import com.clinica.entity.Odontologo;
import com.clinica.repository.OdontologoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class OdontologoTestService {

    @Autowired
    OdontologoService odontologoService;

    @Autowired
    OdontologoRepository odontologoRepository;

    Odontologo doctorHibert;

    @BeforeEach
    public void setup(){
        odontologoRepository.deleteAll();
        doctorHibert = odontologoService.guardar(new Odontologo("Doctor","Hibert","12345"));
    }

    @Test
    public void buscarOdontologo(){
        //DADO
        //CUANDO
        Odontologo odontologo= odontologoService.buscar(doctorHibert.getId());
        System.out.println("datos encontrados: "+odontologo.toString());
        //ENTONCES
        Assertions.assertNotNull(odontologo);
    }

    @Test
    public void guardarOdontologo(){
        //DADO
        Odontologo odontologAGuardar= new Odontologo("Apu","Nahasapeemapetilon","67891234678");

        //CUANDO
        Odontologo guardado = odontologoService.guardar(odontologAGuardar);

        Odontologo odontologoBuscado= odontologoService.buscarGenerico(odontologAGuardar.getNombre());

        //ENTONCES
        Assertions.assertNotNull(guardado);
        Assertions.assertNotNull(odontologoBuscado);
        Assertions.assertEquals(odontologAGuardar.getNombre(),odontologoBuscado.getNombre());
        Assertions.assertEquals(odontologAGuardar.getApellido(),odontologoBuscado.getApellido());

    }

    @Test
    public void eliminarOdontologo(){
        //DADO
        Odontologo nuevo = odontologoService.guardar(new Odontologo("Temporal","Borrar","99999"));

        //CUANDO
        odontologoService.eliminar(nuevo.getId());
        Odontologo odontologoBuscado= odontologoService.buscar(nuevo.getId());
        //ENTONCES
        Assertions.assertNull(odontologoBuscado);
    }

    @Test
    public void actualizarOdontologo(){
        //DADO
        Odontologo odontologoAActualizar= new Odontologo(doctorHibert.getId(),"Apu","Nahasapeemapetilon","54321");

        Assertions.assertEquals("Doctor",odontologoService.buscar(doctorHibert.getId()).getNombre());
        //CUANDO
        odontologoService.actualizar(odontologoAActualizar);

        Odontologo odontologoBuscado= odontologoService.buscar(doctorHibert.getId());

        //ENTONCES
        Assertions.assertEquals("Apu",odontologoBuscado.getNombre());

    }

    @Test
    public void buscarPorMatricula(){
        //DADO;
        //CUANDO
        Odontologo odontologo= odontologoService.buscarGenerico(doctorHibert.getNombre());
        System.out.println("datos encontrados: "+odontologo.toString());
        //ENTONCES
        Assertions.assertNotNull(odontologo);
        Assertions.assertEquals("Doctor",odontologo.getNombre());
    }


    @Test
    public void buscarTodosLosPacientes(){
        //DADO
        List<Odontologo> iniciales = odontologoService.buscarTodos();
        int tamanioInicial = iniciales.size();

        Odontologo odontologAGuardar= new Odontologo("Apu","Nahasapeemapetilon","67890");
        odontologoService.guardar(odontologAGuardar);

        //CUANDO
        List<Odontologo> odontologos= odontologoService.buscarTodos();
        odontologos.forEach(odontologo -> System.out.println(odontologo.toString()));
        //ENTONCES
        Assertions.assertEquals(tamanioInicial + 1, odontologos.size());
    }

}
