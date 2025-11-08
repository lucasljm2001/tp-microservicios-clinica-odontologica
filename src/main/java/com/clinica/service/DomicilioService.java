package com.clinica.service;

import com.clinica.entity.Domicilio;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.repository.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DomicilioService{
    private DomicilioRepository domicilioRepository;

    @Autowired
    public void setDomicilioRepository(DomicilioRepository domicilioRepository) {
        this.domicilioRepository = domicilioRepository;
    }

    public Domicilio registrarDomicilio(Domicilio domicilio ){
        return domicilioRepository.save(domicilio);
    }

    public Optional<Domicilio> buscarPorId(Long id){
        return domicilioRepository.findById(id);
    }

    public void eliminar(Long id) {

    }

    public void actualizar(Domicilio domicilio) {

    }

    public Domicilio buscarGenerico(String parametro) {
        return null;
    }

    public java.util.List<Domicilio> buscarTodos() {
        return null;
    }
}
