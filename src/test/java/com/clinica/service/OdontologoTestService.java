package com.clinica.service;

import com.clinica.DTO.OdontologoDTO;
import com.clinica.entity.Odontologo;
import com.clinica.repository.OdontologoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


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
        doctorHibert= new Odontologo("Doctor","Hibert","12345");
        odontologoService.guardarOdontologo(doctorHibert);
    }

    @Test
    public void buscarOdontologo(){
        //DADO
        //CUANDO
        Optional<OdontologoDTO> odontologo= odontologoService.buscarOdontologoPorId(doctorHibert.getId());
        System.out.println("datos encontrados: "+odontologo.toString());
        //ENTONCES
        Assertions.assertNotNull(odontologo);
    }

    @Test
    public void guardarOdontologo(){
        //DADO
        Odontologo odontologAGuardar= new Odontologo("Apu","Nahasapeemapetilon","67891234678");

        //CUANDO
        OdontologoDTO guardado = odontologoService.guardarOdontologo(odontologAGuardar);

        Optional<OdontologoDTO> odontologoBuscado= odontologoService.buscarOdontologoPorId(odontologAGuardar.getId());

        //ENTONCES
        Assertions.assertNotNull(guardado);
        Assertions.assertNotNull(odontologoBuscado);
        Assertions.assertEquals(
                odontologAGuardar.getNombre(),
                odontologoBuscado.map(OdontologoDTO::getNombre).orElse(null)
        );
        Assertions.assertEquals(
                odontologAGuardar.getApellido(),
                odontologoBuscado.map(OdontologoDTO::getApellido).orElse(null)
        );

    }

    @Test
    public void eliminarOdontologo(){
        //DADO
        Odontologo nuevo = new Odontologo("Temporal","Borrar","99999");
        odontologoService.guardarOdontologo(nuevo);

        //CUANDO
        odontologoService.eliminar(nuevo.getId());
        Optional<OdontologoDTO> odontologoBuscado= odontologoService.buscarOdontologoPorId(nuevo.getId());
        //ENTONCES
        Assertions.assertTrue(odontologoBuscado.isEmpty());
    }

    @Test
    public void actualizarOdontologo(){
        //DADO
        Odontologo odontologoAActualizar= new Odontologo(doctorHibert.getId(),"Apu","Nahasapeemapetilon","54321");

        Assertions.assertEquals("Doctor",odontologoService.buscarOdontologoPorId(doctorHibert.getId()).map(OdontologoDTO::getNombre).orElse(""));
        //CUANDO
        odontologoService.actualizar(odontologoAActualizar);

        Optional<OdontologoDTO> odontologoBuscado= odontologoService.buscarOdontologoPorId(doctorHibert.getId());

        //ENTONCES
        Assertions.assertEquals("Apu",odontologoBuscado.map(OdontologoDTO::getNombre).orElse(""));

    }

    @Test
    public void buscarPorMatricula(){
        //DADO;
        //CUANDO
        Optional<Odontologo> odontologo= odontologoService.buscarPorMatricula(doctorHibert.getNombre());
        System.out.println("datos encontrados: "+odontologo.toString());
        //ENTONCES
        Assertions.assertNotNull(odontologo);
        Assertions.assertEquals("Doctor",odontologo.map(Odontologo::getNombre).orElse(""));
    }


    @Test
    public void buscarTodosLosPacientes(){
        //DADO
        List<OdontologoDTO> iniciales = odontologoService.listarOdontologos();
        int tamanioInicial = iniciales.size();

        Odontologo odontologAGuardar= new Odontologo("Apu","Nahasapeemapetilon","67890");
        odontologoService.guardarOdontologo(odontologAGuardar);

        //CUANDO
        List<OdontologoDTO> odontologos= odontologoService.listarOdontologos();
        odontologos.forEach(odontologo -> System.out.println(odontologo.toString()));
        //ENTONCES
        Assertions.assertEquals(tamanioInicial + 1, odontologos.size());
    }

}
