import dao.BD;
import dao.PacienteDAOH2;
import model.Paciente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.PacienteService;

public class PacienteTestService {
    @Test
    public void buscarPaciente(){
        //DADO
        BD.crearTablas();
        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());
        //CUANDO
        Paciente paciente= pacienteService.buscarPacientePorId(2);
        //ENTONCES
        Assertions.assertTrue(paciente!=null);
    }
}
