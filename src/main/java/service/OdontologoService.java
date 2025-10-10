package service;

import dao.iDao;
import model.Odontologo;

import java.util.List;

public class OdontologoService implements iService<Odontologo> {
    private iDao<Odontologo> odontologoiDao;

    public OdontologoService(iDao<Odontologo> odontologoiDao) {
        this.odontologoiDao = odontologoiDao;
    }

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        return odontologoiDao.guardar(odontologo);
    }

    @Override
    public Odontologo buscar(Integer id) {
        return odontologoiDao.buscar(id);
    }

    @Override
    public void eliminar(Integer id) {
        odontologoiDao.eliminar(id);
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        odontologoiDao.actualizar(odontologo);
    }

    @Override
    public Odontologo buscarGenerico(String parametro) {
        return odontologoiDao.buscarGenerico(parametro);
    }

    @Override
    public List<Odontologo> buscarTodos() {
        return odontologoiDao.buscarTodos();
    }
}
