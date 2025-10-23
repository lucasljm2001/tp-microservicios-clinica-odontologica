import com.clinica.dao.BD;
import com.clinica.dao.DomicilioDAOH2;
import com.clinica.dao.PacienteDAOH2;
import com.clinica.model.Paciente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.clinica.service.DomicilioService;
import com.clinica.service.PacienteService;

import java.util.List;

public class PacienteTestService {
    @Test
    public void buscarPaciente(){
        //DADO
        BD.crearTablas();
        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());
        //CUANDO
        Paciente paciente= pacienteService.buscar(1);
        System.out.println("datos encontrados: "+paciente.toString());
        //ENTONCES
        Assertions.assertTrue(paciente!=null);
    }

    @Test
    public void guardarPaciente(){
        //DADO
        BD.crearTablas();
        DomicilioService domicilioService = new DomicilioService(new DomicilioDAOH2());
        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());
        Paciente pacienteAGuardar= new Paciente("Bart",
                "Simpson",
                123456,
                java.time.LocalDate.of(2025,10,10),
                domicilioService.buscar(1),
                "bart@gmail.com");

        //CUANDO
        pacienteService.guardar(pacienteAGuardar);

        Paciente pacienteBuscado= pacienteService.buscar(3);

        //ENTONCES
        Assertions.assertEquals(pacienteAGuardar.getNombre(),pacienteBuscado.getNombre());
        Assertions.assertEquals(pacienteAGuardar.getApellido(),pacienteBuscado.getApellido());

    }

    @Test
    public void eliminarPaciente(){
        //DADO
        BD.crearTablas();
        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());
        //CUANDO
        pacienteService.eliminar(2);
        Paciente pacienteBuscado= pacienteService.buscar(2);
        //ENTONCES
        Assertions.assertTrue(pacienteBuscado==null);
    }

    @Test
    public void actualizarPaciente(){
        //DADO
        BD.crearTablas();
        DomicilioService domicilioService = new DomicilioService(new DomicilioDAOH2());
        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());
        Paciente pacienteAActualizar= new Paciente(1,
                "Abraham",
                "Simpson",
                123456,
                java.time.LocalDate.of(2025,10,10),
                domicilioService.buscar(1),
                "homero@gmail.com");

        Assertions.assertEquals("Homero",pacienteService.buscar(1).getNombre());
        //CUANDO
        pacienteService.actualizar(pacienteAActualizar);

        Paciente pacienteBuscado= pacienteService.buscar(1);

        //ENTONCES
        Assertions.assertEquals("Abraham",pacienteBuscado.getNombre());

    }

    @Test
    public void buscarPorNombre(){
        //DADO
        BD.crearTablas();
        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());
        //CUANDO
        Paciente paciente= pacienteService.buscarGenerico("Homero");
        System.out.println("datos encontrados: "+paciente.toString());
        //ENTONCES
        Assertions.assertTrue(paciente!=null);
        Assertions.assertEquals("Homero",paciente.getNombre());
    }


    @Test
    public void buscarTodosLosPacientes(){
        //DADO
        BD.crearTablas();
        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());
        //CUANDO
        List<Paciente> pacientes= pacienteService.buscarTodos();
        pacientes.forEach(paciente -> System.out.println(paciente.toString()));
        //ENTONCES
        Assertions.assertEquals(2, pacientes.size());
    }
}
