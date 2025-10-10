package dao;

import model.Domicilio;
import model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAOH2 implements iDao<Paciente>{
    private static final String SQL_SELECT_ONE=" SELECT * FROM PACIENTES WHERE ID=?";

    private static final String SQL_INSERT=" INSERT INTO PACIENTES(NOMBRE, APELLIDO, NUMEROCONTACTO, FECHAINGRESO, DOMICILIO_ID, EMAIL) VALUES(?,?,?,?,?,?)";

    private static final String SQL_DELETE=" DELETE FROM PACIENTES WHERE ID=?";

    private static final String SQL_UPDATE=" UPDATE PACIENTES SET NOMBRE=?, APELLIDO=?, NUMEROCONTACTO=?, FECHAINGRESO=?, DOMICILIO_ID=?, EMAIL=? WHERE ID=?";

    private static final String SQL_SELECT_BY_NOMBRE=" SELECT * FROM PACIENTES WHERE NOMBRE=?";

    private static final String SQL_SELECT_ALL=" SELECT * FROM PACIENTES";


    @Override
    public Paciente guardar(Paciente paciente) {
        Connection connection=null;
        try{
            connection=BD.getConnection();

            PreparedStatement ps_insert= connection.prepareStatement(SQL_INSERT);
            ps_insert.setString(1,paciente.getNombre());
            ps_insert.setString(2,paciente.getApellido());
            ps_insert.setInt(3,paciente.getNumeroContacto());
            ps_insert.setDate(4,java.sql.Date.valueOf(paciente.getFechaIngreso()));
            ps_insert.setInt(5,paciente.getDomicilio().getId());
            ps_insert.setString(6,paciente.getEmail());

            ps_insert.execute();
            System.out.println("paciente guardado con exito");
        }catch (Exception e){
            System.out.println("Error buscando el paciente: "+e.getMessage());
        }
        return paciente;
    }

    @Override
    public Paciente buscar(Integer id) {
        Connection connection=null;
        Paciente paciente= null;
        Domicilio domicilio= null;
        try{
            connection=BD.getConnection();
            //statement mundo java a sql
            Statement statement= connection.createStatement();
            PreparedStatement ps_select_one= connection.prepareStatement(SQL_SELECT_ONE);
            ps_select_one.setInt(1,id);
            //ResultSet mundo bdd a java
            ResultSet rs= ps_select_one.executeQuery();
            DomicilioDAOH2 daoAux= new DomicilioDAOH2();
            while(rs.next()){
                domicilio=daoAux.buscar(rs.getInt(6));
                paciente= new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getDate(5).toLocalDate(),domicilio,rs.getString(7));
            }
        }catch (Exception e){
            System.out.println("Error buscando el paciente: "+e.getMessage());
        }
        System.out.println("paciente encontrado");
        return paciente;
    }

    @Override
    public void eliminar(Integer id) {
        Connection connection=null;
        try{
            connection=BD.getConnection();

            PreparedStatement ps_insert= connection.prepareStatement(SQL_DELETE);
            ps_insert.setInt(1,id);

            ps_insert.execute();
            System.out.println("paciente eliminado con exito");
        }catch (Exception e){
            System.out.println("Error eliminando el paciente: "+e.getMessage());
        }

    }

    @Override
    public void actualizar(Paciente paciente) {
        Connection connection=null;
        try{
            connection=BD.getConnection();

            PreparedStatement ps_insert= connection.prepareStatement(SQL_UPDATE);
            ps_insert.setString(1,paciente.getNombre());
            ps_insert.setString(2,paciente.getApellido());
            ps_insert.setInt(3,paciente.getNumeroContacto());
            ps_insert.setDate(4,java.sql.Date.valueOf(paciente.getFechaIngreso()));
            ps_insert.setInt(5,paciente.getDomicilio().getId());
            ps_insert.setString(6,paciente.getEmail());
            ps_insert.setInt(7, paciente.getId());

            ps_insert.execute();
            System.out.println("paciente actualizado con exito");
        }catch (Exception e){
            System.out.println("Error actualizando el paciente: "+e.getMessage());
        }


    }

    @Override
    public Paciente buscarGenerico(String parametro) {
        Connection connection=null;
        Paciente paciente= null;
        Domicilio domicilio= null;
        try{
            connection=BD.getConnection();
            PreparedStatement ps_select_one= connection.prepareStatement(SQL_SELECT_BY_NOMBRE);
            ps_select_one.setString(1,parametro);
            ResultSet rs= ps_select_one.executeQuery();
            DomicilioDAOH2 daoAux= new DomicilioDAOH2();
            while(rs.next()){
                domicilio=daoAux.buscar(rs.getInt(6));
                paciente= new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getDate(5).toLocalDate(),domicilio,rs.getString(7));
            }
        }catch (Exception e){
            System.out.println("Error buscando el paciente: "+e.getMessage());
        }
        System.out.println("paciente encontrado");
        return paciente;
    }

    @Override
    public List<Paciente> buscarTodos() {
        Connection connection=null;
        Paciente paciente= null;
        Domicilio domicilio= null;
        List<Paciente> pacientes= new ArrayList<>();
        try{
            connection=BD.getConnection();
            PreparedStatement ps_select_one= connection.prepareStatement(SQL_SELECT_ALL);
            ResultSet rs= ps_select_one.executeQuery();
            DomicilioDAOH2 daoAux= new DomicilioDAOH2();
            while(rs.next()){
                domicilio=daoAux.buscar(rs.getInt(6));
                paciente= new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getDate(5).toLocalDate(),domicilio,rs.getString(7));
                pacientes.add(paciente);
            }
        }catch (Exception e){
            System.out.println("Error buscando el paciente: "+e.getMessage());
        }
        System.out.println("paciente encontrado");
        return pacientes;

    }
}
