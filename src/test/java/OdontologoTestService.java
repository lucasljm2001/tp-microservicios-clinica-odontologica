import com.clinica.dao.BD;
import com.clinica.dao.OdontologoDAOH2;
import com.clinica.model.Odontologo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.clinica.service.OdontologoService;

import java.util.List;

public class OdontologoTestService {
    @Test
    public void buscarOdontologo(){
        //DADO
        BD.crearTablas();
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());
        //CUANDO
        Odontologo odontologo= odontologoService.buscar(1);
        System.out.println("datos encontrados: "+odontologo.toString());
        //ENTONCES
        Assertions.assertNotNull(odontologo);
    }

    @Test
    public void guardarOdontologo(){
        //DADO
        BD.crearTablas();
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());
        Odontologo odontologAGuardar= new Odontologo("Apu","Nahasapeemapetilon","12345");

        //CUANDO
        odontologoService.guardar(odontologAGuardar);

        Odontologo odontologoBuscado= odontologoService.buscar(2);

        //ENTONCES
        Assertions.assertEquals(odontologAGuardar.getNombre(),odontologoBuscado.getNombre());
        Assertions.assertEquals(odontologAGuardar.getApellido(),odontologoBuscado.getApellido());

    }

    @Test
    public void eliminarOdontologo(){
        //DADO
        BD.crearTablas();
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());
        //CUANDO
        odontologoService.eliminar(2);
        Odontologo odontologoBuscado= odontologoService.buscar(2);
        //ENTONCES
        Assertions.assertNull(odontologoBuscado);
    }

    @Test
    public void actualizarOdontologo(){
        //DADO
        BD.crearTablas();
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());
        Odontologo odontologoAActualizar= new Odontologo(1,"Apu","Nahasapeemapetilon","54321");

        Assertions.assertEquals("Doctor",odontologoService.buscar(1).getNombre());
        //CUANDO
        odontologoService.actualizar(odontologoAActualizar);

        Odontologo odontologoBuscado= odontologoService.buscar(1);

        //ENTONCES
        Assertions.assertEquals("Apu",odontologoBuscado.getNombre());

    }

    @Test
    public void buscarPorMatricula(){
        //DADO
        BD.crearTablas();
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());
        //CUANDO
        Odontologo odontologo= odontologoService.buscarGenerico("12345");
        System.out.println("datos encontrados: "+odontologo.toString());
        //ENTONCES
        Assertions.assertNotNull(odontologo);
        Assertions.assertEquals("Doctor",odontologo.getNombre());
    }


    @Test
    public void buscarTodosLosPacientes(){
        //DADO
        BD.crearTablas();
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());
        Odontologo odontologAGuardar= new Odontologo("Apu","Nahasapeemapetilon","12345");
        odontologoService.guardar(odontologAGuardar);

        //CUANDO
        List<Odontologo> odontologos= odontologoService.buscarTodos();
        odontologos.forEach(odontologo -> System.out.println(odontologo.toString()));
        //ENTONCES
        Assertions.assertEquals(2, odontologos.size());
    }

}
