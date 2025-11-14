package com.clinica;

import com.clinica.dto.OdontologoDTO;
import com.clinica.dto.PacienteDTO;
import com.clinica.dto.TurnoDTO;
import com.clinica.entity.Domicilio;
import com.clinica.entity.Odontologo;
import com.clinica.entity.Paciente;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.repository.DomicilioRepository;
import com.clinica.repository.OdontologoRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.TurnoRepository;
import com.clinica.service.OdontologoService;
import com.clinica.service.PacienteService;
import com.clinica.service.TurnoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TurnosTestIntegracion {
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;

    @Autowired
    OdontologoRepository odontologoRepository;

    @Autowired
    DomicilioRepository domicilioRepository;

    @Autowired
    TurnoRepository turnoRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private PacienteDTO pacienteGuardado;
    private OdontologoDTO odontologoGuardado;
    private TurnoDTO turnoCreado;


    @BeforeEach
    public void cargaDatos() throws ResourceNotFoundException {
        turnoRepository.deleteAll();
        pacienteRepository.deleteAll();
        odontologoRepository.deleteAll();
        domicilioRepository.deleteAll();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Paciente paciente= new Paciente("Jorgito","Pereyra",111111111, LocalDate.of(2025,11,13),new Domicilio("Siempre viva",123,"Springfield","Argentina"),"jorgeagustinpereyra@gmail.com");
        pacienteGuardado= pacienteService.guardarPaciente(paciente);

        odontologoGuardado= odontologoService.guardarOdontologo(new Odontologo("Sergio","Conti","12345"));

    }

    @Test
    public void registrarTurno() throws Exception {
        TurnoDTO turnoAGuardar= new TurnoDTO();
        turnoAGuardar.setFecha(LocalDate.of(2025,12,12));
        turnoAGuardar.setPacienteId(pacienteGuardado.getId());
        turnoAGuardar.setOdontologoId(odontologoGuardado.getId());

        MvcResult resultado = mockMvc.perform(MockMvcRequestBuilders.post("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(turnoAGuardar))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String json = resultado.getResponse().getContentAsString();
        TurnoDTO creado = mapper.readValue(json, TurnoDTO.class);
        assertNotNull(creado.getId());
        assertEquals(turnoAGuardar.getFecha(), creado.getFecha());
        assertEquals(pacienteGuardado.getId(), creado.getPacienteId());
        assertEquals(odontologoGuardado.getId(), creado.getOdontologoId());

        this.turnoCreado = creado;
    }

    @Test
    public void listarTurnos() throws Exception {
        registrarTurno();

        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/turnos").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String body = respuesta.getResponse().getContentAsString();
        assertFalse(body.isEmpty());
        assertTrue(body.contains(String.valueOf(turnoCreado.getId())));
    }

    @Test
    public void obtenerTurnoPorId() throws Exception {
        registrarTurno();

        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.get("/turnos/" + turnoCreado.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String json = respuesta.getResponse().getContentAsString();
        TurnoDTO encontrado = mapper.readValue(json, TurnoDTO.class);
        assertEquals(turnoCreado.getId(), encontrado.getId());
        assertEquals(turnoCreado.getFecha(), encontrado.getFecha());
    }

    @Test
    public void actualizarTurno() throws Exception {
        registrarTurno();

        turnoCreado.setFecha(LocalDate.of(2025,12,31));

        MvcResult resultado = mockMvc.perform(MockMvcRequestBuilders.put("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(turnoCreado))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String json = resultado.getResponse().getContentAsString();
        TurnoDTO actualizado = mapper.readValue(json, TurnoDTO.class);
        assertEquals(LocalDate.of(2025,12,31), actualizado.getFecha());
    }

    @Test
    public void eliminarTurno() throws Exception {
        registrarTurno();

        mockMvc.perform(MockMvcRequestBuilders.delete("/turnos/" + turnoCreado.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // al pedir por id debe devolver NOT_FOUND
        mockMvc.perform(MockMvcRequestBuilders.get("/turnos/" + turnoCreado.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
