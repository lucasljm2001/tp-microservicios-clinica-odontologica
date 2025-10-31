package com.clinica.service;

import com.clinica.DTO.OdontologoDTO;
import com.clinica.DTO.PacienteDTO;
import com.clinica.entity.Odontologo;
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
    public OdontologoDTO OdontologoAOdontologoDTO(Odontologo odontologo){
        OdontologoDTO odontologoDTO= new OdontologoDTO();
        odontologoDTO.setId(odontologo.getId());
        odontologoDTO.setNombre(odontologo.getNombre());
        odontologoDTO.setApellido(odontologo.getApellido());
        odontologoDTO.setMatricula(odontologo.getMatricula());
        return odontologoDTO;
    }
    public Optional<OdontologoDTO> buscarOdontologoPorId(Long id) {
        Optional<Odontologo> odontologoBuscado= odontologoRepository.findById(id);
        return odontologoBuscado.map(this::OdontologoAOdontologoDTO);
    }
    public Optional<Odontologo> buscarPorMatricula(String matricula){
        return odontologoRepository.findByMatricula(matricula);
    }

    public void actualizar(Odontologo odontologo) {
        odontologoRepository.save(odontologo);
    }

    public boolean eliminar(Long id) {
        Optional<Odontologo> odontologoOpt= odontologoRepository.findById(id);
        if(odontologoOpt.isPresent()){
            odontologoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Odontologo> buscarId(Long id) {
        return odontologoRepository.findById(id);
    }
}
