package service;

import dao.iDao;
import model.Paciente;

import java.util.List;

public class PacienteService {
    private iDao<Paciente> pacienteiDao;

    public PacienteService(iDao<Paciente> pacienteiDao) {
        this.pacienteiDao = pacienteiDao;
    }
    public Paciente guardarPaciente(Paciente paciente){
        return pacienteiDao.guardar(paciente);
    }
    public Paciente buscarPacientePorId(Integer id){
        return pacienteiDao.buscar(id);
    }
    public List<Paciente> buscarPacientes(){
        return pacienteiDao.buscarTodos();
    }
}
