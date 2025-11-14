package com.clinica.service;

import com.clinica.dto.TurnoDTO;
import com.clinica.entity.Domicilio;
import com.clinica.entity.Odontologo;
import com.clinica.entity.Paciente;
import com.clinica.entity.Turno;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.repository.DomicilioRepository;
import com.clinica.repository.OdontologoRepository;
import com.clinica.repository.PacienteRepository;
import com.clinica.repository.TurnoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class TurnoTestService {
    @Autowired
    TurnoService turnoService;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    OdontologoRepository odontologoRepository;

    @Autowired
    DomicilioRepository domicilioRepository;

    @Autowired
    TurnoRepository turnoRepository;

    Paciente pacienteInicial;
    Odontologo odontologoInicial;
    Turno turnoInicial;

    @BeforeEach
    public void setup(){
        turnoRepository.deleteAll();
        pacienteRepository.deleteAll();
        odontologoRepository.deleteAll();
        domicilioRepository.deleteAll();

        Domicilio domicilio = domicilioRepository.save(new Domicilio("Evergreen", 742, "Springfield", "AnyState"));
        pacienteInicial = pacienteRepository.save(new Paciente("Homero", "Simpson", 123456, LocalDate.now(), domicilio, "homero@gmail.com"));
        odontologoInicial = odontologoRepository.save(new Odontologo("Dr.", "Nick", "MAT123"));

        TurnoDTO turnoDTO = new TurnoDTO();
        turnoDTO.setFecha(LocalDate.of(2025, 11, 15));
        turnoInicial = turnoRepository.save(new Turno());
        turnoInicial.setPaciente(pacienteInicial);
        turnoInicial.setOdontologo(odontologoInicial);
        turnoInicial.setFecha(turnoDTO.getFecha());
        turnoInicial = turnoRepository.save(turnoInicial);
    }

    @Test
    public void buscarTurnoPorId() throws ResourceNotFoundException {
        // CUANDO
        TurnoDTO turno = turnoService.buscarTurnoPorId(turnoInicial.getId());
        System.out.println("datos encontrados: " + turno.toString());
        // ENTONCES
        Assertions.assertNotNull(turno);
        Assertions.assertEquals(turnoInicial.getId(), turno.getId());
    }

    @Test
    public void guardarTurno() throws ResourceNotFoundException {
        // DADO
        Domicilio domicilioNuevo = domicilioRepository.save(new Domicilio("Main", 1, "Springfield", "AnyState"));
        Paciente pacienteNuevo = pacienteRepository.save(new Paciente("Bart", "Simpson", 123457, LocalDate.now(), domicilioNuevo, "bart@gmail.com"));
        Odontologo odontologoNuevo = odontologoRepository.save(new Odontologo("Dr.", "Zoidberg", "MAT456"));

        TurnoDTO turnoDTO = new TurnoDTO();
        turnoDTO.setFecha(LocalDate.of(2025, 11, 16));

        // CUANDO
        TurnoDTO guardado = turnoService.guardarTurno(pacienteNuevo.getId(), odontologoNuevo.getId(), turnoDTO);
        TurnoDTO turnoBuscado = turnoService.buscarTurnoPorId(guardado.getId());

        // ENTONCES
        Assertions.assertNotNull(guardado);
        Assertions.assertNotNull(turnoBuscado);
        Assertions.assertEquals(turnoDTO.getFecha(), turnoBuscado.getFecha());
        Assertions.assertEquals(pacienteNuevo.getId(), turnoBuscado.getPacienteId());
        Assertions.assertEquals(odontologoNuevo.getId(), turnoBuscado.getOdontologoId());
    }

    @Test
    public void eliminarTurno() throws ResourceNotFoundException {
        // DADO
        Domicilio domicilioTemp = domicilioRepository.save(new Domicilio("Temp", 2, "Springfield", "AnyState"));
        Paciente pacienteTemp = pacienteRepository.save(new Paciente("Temporal", "Borrar", 111111, LocalDate.now(), domicilioTemp, "temp@example.com"));
        Odontologo odontologoTemp = odontologoRepository.save(new Odontologo("Dr.", "Temp", "MAT789"));

        TurnoDTO turnoDTO = new TurnoDTO();
        turnoDTO.setFecha(LocalDate.of(2025, 11, 17));
        TurnoDTO turnoGuardado = turnoService.guardarTurno(pacienteTemp.getId(), odontologoTemp.getId(), turnoDTO);

        // CUANDO
        turnoService.eliminarTurno(turnoGuardado.getId());
        // ENTONCES
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            turnoService.buscarTurnoPorId(turnoGuardado.getId());
        });
    }

    @Test
    public void actualizarTurno() throws ResourceNotFoundException {
        // DADO
        TurnoDTO turnoAActualizar = new TurnoDTO();
        turnoAActualizar.setId(turnoInicial.getId());
        turnoAActualizar.setFecha(LocalDate.of(2025, 11, 18));
        turnoAActualizar.setPacienteId(pacienteInicial.getId());
        turnoAActualizar.setOdontologoId(odontologoInicial.getId());

        Assertions.assertEquals(LocalDate.of(2025, 11, 15), turnoService.buscarTurnoPorId(turnoInicial.getId()).getFecha());
        // CUANDO
        turnoService.actualizar(turnoAActualizar);
        TurnoDTO turnoBuscado = turnoService.buscarTurnoPorId(turnoInicial.getId());

        // ENTONCES
        Assertions.assertEquals(LocalDate.of(2025, 11, 18), turnoBuscado.getFecha());
    }

    @Test
    public void listarTurnos() throws ResourceNotFoundException {
        // DADO
        List<TurnoDTO> iniciales = turnoService.listarTurnos();
        int tamanioInicial = iniciales.size();

        Domicilio domicilioNuevo = domicilioRepository.save(new Domicilio("New", 3, "Springfield", "AnyState"));
        Paciente pacienteNuevo = pacienteRepository.save(new Paciente("Apu", "Nahasapeemapetilon", 123458, LocalDate.now(), domicilioNuevo, "apu@example.com"));
        Odontologo odontologoNuevo = odontologoRepository.save(new Odontologo("Dr.", "New", "MAT000"));

        TurnoDTO turnoDTO = new TurnoDTO();
        turnoDTO.setFecha(LocalDate.of(2025, 11, 19));
        turnoService.guardarTurno(pacienteNuevo.getId(), odontologoNuevo.getId(), turnoDTO);

        // CUANDO
        List<TurnoDTO> turnos = turnoService.listarTurnos();
        turnos.forEach(turno -> System.out.println(turno.toString()));
        // ENTONCES
        Assertions.assertEquals(tamanioInicial + 1, turnos.size());
    }
}
