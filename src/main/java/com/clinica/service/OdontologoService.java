package com.clinica.service;

import com.clinica.dto.OdontologoDTO;
import com.clinica.dto.PacienteDTO;
import com.clinica.entity.Odontologo;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.entity.Paciente;
import com.clinica.repository.OdontologoRepository;
import com.clinica.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Component
public class OdontologoService{
    @Autowired
    private OdontologoRepository odontologoRepository;

    public List<OdontologoDTO> listarOdontologos() {
        List<Odontologo> odontologos= odontologoRepository.findAll();
        return odontologos.stream().map(this::OdontologoAOdontologoDTO).toList();
    }

    public OdontologoDTO guardarOdontologo(Odontologo odontologo){
        Odontologo odontologoGuardado= odontologoRepository.save(odontologo);
        return OdontologoAOdontologoDTO(odontologoGuardado);
    }
    public OdontologoDTO OdontologoAOdontologoDTO(Odontologo odontologo) {
        OdontologoDTO odontologoDTO = new OdontologoDTO();
        odontologoDTO.setId(odontologo.getId());
        odontologoDTO.setNombre(odontologo.getNombre());
        odontologoDTO.setApellido(odontologo.getApellido());
        odontologoDTO.setMatricula(odontologo.getMatricula());
        return odontologoDTO;
    }

    public OdontologoDTO buscarOdontologoPorId(Long id) throws ResourceNotFoundException {
        Odontologo odontologoBuscado= odontologoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Odontologo no encontrado con id: " + id));
        return OdontologoAOdontologoDTO(odontologoBuscado);
    }
    public Odontologo buscarPorMatricula(String matricula) throws ResourceNotFoundException {
        return odontologoRepository.findByMatricula(matricula).orElseThrow(() -> new ResourceNotFoundException("Odontologo no encontrado con matricula: " + matricula));
    }

    public void actualizar(Odontologo odontologo) {
        odontologoRepository.save(odontologo);
    }

    public void eliminar(Long id) throws ResourceNotFoundException {
        Odontologo odontologoOpt= odontologoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Odontologo no encontrado con id: " + id));
        odontologoRepository.deleteById(id);
    }

    public Optional<Odontologo> buscarId(Long id) {
        return odontologoRepository.findById(id);
    }
}
