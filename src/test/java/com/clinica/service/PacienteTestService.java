package com.clinica.service;

import com.clinica.dto.PacienteDTO;
import com.clinica.entity.Domicilio;
import com.clinica.entity.Paciente;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.exception.TurnoComprometidoException;
import com.clinica.repository.DomicilioRepository;
import com.clinica.repository.PacienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class PacienteTestService {
    @Autowired
    PacienteService pacienteService;

    @Autowired
    DomicilioRepository domicilioRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    Domicilio domicilioInicial;
    Paciente pacienteInicial;

    @BeforeEach
    public void setup(){
        pacienteRepository.deleteAll();
        domicilioRepository.deleteAll();
        domicilioInicial = domicilioRepository.save(new Domicilio("Evergreen",742,"Springfield","AnyState"));
        pacienteInicial = new Paciente("Homero","Simpson",123456,java.time.LocalDate.now(),domicilioInicial,"homero@gmail.com");
        pacienteService.guardarPaciente(pacienteInicial);
    }

    @Test
    public void buscarPaciente() throws ResourceNotFoundException {
        //CUANDO
        PacienteDTO paciente= pacienteService.buscarPacientePorId(pacienteInicial.getId());
        System.out.println("datos encontrados: "+paciente.toString());
        //ENTONCES
        Assertions.assertNotNull(paciente);
    }

    @Test
    public void guardarPaciente() throws ResourceNotFoundException {
        //DADO
        Domicilio domicilioBart = domicilioRepository.save(new Domicilio("Evergreen",743,"Springfield","AnyState"));
        Paciente pacienteAGuardar= new Paciente("Bart","Simpson",123457,java.time.LocalDate.of(2025,10,10),domicilioBart,"bart@gmail.com");

        //CUANDO
        PacienteDTO guardado = pacienteService.guardarPaciente(pacienteAGuardar);

        PacienteDTO pacienteBuscado= pacienteService.buscarPacientePorId(pacienteAGuardar.getId());



        //ENTONCES
        Assertions.assertNotNull(guardado);
        Assertions.assertNotNull(pacienteBuscado);
        Assertions.assertEquals(
                pacienteAGuardar.getNombre(),
                pacienteBuscado.getNombre()
        );

        Assertions.assertEquals(
                pacienteAGuardar.getApellido(),
                pacienteBuscado.getApellido()
        );

    }

    @Test
    public void eliminarPaciente() throws ResourceNotFoundException, TurnoComprometidoException {
        //DADO
        Domicilio domicilioTemp = domicilioRepository.save(new Domicilio("Main",1,"Springfield","AnyState"));
        Paciente nuevo= new Paciente("Temporal","Borrar",111111,java.time.LocalDate.now(),domicilioTemp,"temp@example.com");
        pacienteService.guardarPaciente(nuevo);

        //CUANDO
        pacienteService.eliminar(nuevo.getId());
        //ENTONCES
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            pacienteService.buscarPacientePorId(nuevo.getId());
        });
    }

    @Test
    public void actualizarPaciente() throws ResourceNotFoundException {
        //DADO
        Paciente pacienteAActualizar= new Paciente(pacienteInicial.getId(),"Abraham","Simpson",123456,java.time.LocalDate.of(2025,10,10),domicilioInicial,"homero@gmail.com");

        Assertions.assertEquals("Homero",pacienteService.buscarPacientePorId(pacienteInicial.getId()).getNombre());
        //CUANDO
        pacienteService.actualizar(pacienteAActualizar);

        PacienteDTO pacienteBuscado= pacienteService.buscarPacientePorId(pacienteInicial.getId());

        //ENTONCES
        Assertions.assertEquals("Abraham",pacienteBuscado.getNombre());

    }

    /*@Test
    public void buscarPorNombre(){
        //CUANDO
        Paciente paciente= pacienteService.buscarGenerico(pacienteInicial.getNombre());
        System.out.println("datos encontrados: "+paciente.toString());
        //ENTONCES
        Assertions.assertNotNull(paciente);
        Assertions.assertEquals("Homero",paciente.getNombre());
    }
*/
    @Test
    public void buscarTodosLosPacientes(){
        //DADO
        List<PacienteDTO> iniciales = pacienteService.listarPacientes();
        int tamanioInicial = iniciales.size();

        Domicilio domicilioApu = domicilioRepository.save(new Domicilio("Evergreen",744,"Springfield","AnyState"));
        Paciente pacienteAGuardar= new Paciente("Apu","Nahasapeemapetilon",123458,java.time.LocalDate.of(2025,10,10),domicilioApu,"apu@example.com");
        pacienteService.guardarPaciente(pacienteAGuardar);

        //CUANDO
        List<PacienteDTO> pacientes= pacienteService.listarPacientes();
        pacientes.forEach(paciente -> System.out.println(paciente.toString()));
        //ENTONCES
        Assertions.assertEquals(tamanioInicial + 1, pacientes.size());
    }
}
