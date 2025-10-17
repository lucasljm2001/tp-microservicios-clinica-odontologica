package com.clinica.service;

import com.clinica.dao.iDao;
import com.clinica.model.Paciente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService implements iService<Paciente> {
    private iDao<Paciente> pacienteiDao;

    public PacienteService(iDao<Paciente> pacienteiDao) {
        this.pacienteiDao = pacienteiDao;
    }

    @Override
    public Paciente guardar(Paciente paciente) {
        return pacienteiDao.guardar(paciente);
    }

    @Override
    public Paciente buscar(Integer id) {
        return pacienteiDao.buscar(id);
    }

    @Override
    public void eliminar(Integer id) {
        pacienteiDao.eliminar(id);
    }

    @Override
    public void actualizar(Paciente paciente) {
        pacienteiDao.actualizar(paciente);
    }

    @Override
    public Paciente buscarGenerico(String parametro) {
        return pacienteiDao.buscarGenerico(parametro);
    }

    @Override
    public List<Paciente> buscarTodos() {
        return pacienteiDao.buscarTodos();
    }
}
