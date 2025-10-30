package com.clinica.service;

import com.clinica.entity.Odontologo;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.repository.OdontologoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class OdontologoService implements iService<Odontologo> {
    private OdontologoRepository odontologoRepository;

    public OdontologoService(OdontologoRepository odontologoRepository) {
        this.odontologoRepository = odontologoRepository;
    }

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        return odontologoRepository.save(odontologo);
    }

    @Override
    public Odontologo buscar(Long id) throws ResourceNotFoundException {
        return odontologoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Odontologo no encontrado con id: " + id));
    }

    @Override
    public void eliminar(Long id) {
        odontologoRepository.deleteById(id);
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        odontologoRepository.save(odontologo);
    }

    @Override
    public Odontologo buscarGenerico(String parametro) {
        return odontologoRepository.findByNombre(parametro);
    }

    @Override
    public List<Odontologo> buscarTodos() {
        return odontologoRepository.findAll();
    }
}
