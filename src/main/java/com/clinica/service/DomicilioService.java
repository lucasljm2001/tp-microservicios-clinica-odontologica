package com.clinica.service;

import com.clinica.dao.iDao;
import com.clinica.model.Domicilio;

public class DomicilioService implements  iService<Domicilio> {
    private iDao<Domicilio> domicilioiDao;

    public DomicilioService(iDao<Domicilio> domicilioiDao) {
        this.domicilioiDao = domicilioiDao;
    }

    @Override
    public Domicilio guardar(Domicilio domicilio) {
        return null;
    }

    @Override
    public Domicilio buscar(Integer id) {
        return domicilioiDao.buscar(id);
    }

    @Override
    public void eliminar(Integer id) {

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
