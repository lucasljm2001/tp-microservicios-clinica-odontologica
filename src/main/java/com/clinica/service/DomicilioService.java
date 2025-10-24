package com.clinica.service;

import com.clinica.entity.Domicilio;
import com.clinica.repository.DomicilioRepository;
import org.springframework.stereotype.Service;

@Service
public class DomicilioService implements  iService<Domicilio> {
    private DomicilioRepository domicilioRepository;

    public DomicilioService(DomicilioRepository domicilioRepository) {
        this.domicilioRepository = domicilioRepository;
    }

    @Override
    public Domicilio guardar(Domicilio domicilio) {
        return null;
    }

    @Override
    public Domicilio buscar(Long id) {
        return domicilioRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {

    }

    @Override
    public void actualizar(Domicilio domicilio) {

    }

    @Override
    public Domicilio buscarGenerico(String parametro) {
        return null;
    }

    @Override
    public java.util.List<Domicilio> buscarTodos() {
        return null;
    }
}
