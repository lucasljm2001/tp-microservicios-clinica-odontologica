package com.clinica.dao;

import com.clinica.model.Odontologo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDAOH2 implements iDao<Odontologo> {

    public static final String SQL_INSERT=" INSERT INTO ODONTOLOGOS(NOMBRE, APELLIDO, MATRICULA) VALUES(?,?,?)";

    public static final String SQL_SELECT_ONE=" SELECT * FROM ODONTOLOGOS WHERE ID=?";

    public static final String SQL_DELETE=" DELETE FROM ODONTOLOGOS WHERE ID=?";

    public static final String SQL_UPDATE=" UPDATE ODONTOLOGOS SET NOMBRE=?, APELLIDO=?, MATRICULA=? WHERE ID=?";

    public static final String SQL_SELECT_BY_MATRICULA=" SELECT * FROM ODONTOLOGOS WHERE MATRICULA=?";

    public static final String SQL_SELECT_ALL=" SELECT * FROM ODONTOLOGOS";

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        Connection connection=null;
        try{
            connection=BD.getConnection();

            PreparedStatement ps_insert= connection.prepareStatement(SQL_INSERT);
            ps_insert.setString(1,odontologo.getNombre());
            ps_insert.setString(2,odontologo.getApellido());
            ps_insert.setString(3,odontologo.getMatricula());

            ps_insert.execute();
            System.out.println("odontologo guardado con exito");
        }catch (Exception e){
            System.out.println("Error buscando el odontologo: "+e.getMessage());
        }
        return odontologo;

    }

    @Override
    public Odontologo buscar(Integer id) {
        Connection connection=null;
        Odontologo odontologo= null;
        try{
            connection=BD.getConnection();
            PreparedStatement ps_select_one= connection.prepareStatement(SQL_SELECT_ONE);
            ps_select_one.setInt(1,id);
            ResultSet rs= ps_select_one.executeQuery();
            while(rs.next()){
                odontologo= new Odontologo(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getString(4));
            }
        }catch (Exception e){
            System.out.println("Error buscando el odontologo: "+e.getMessage());
        }
        System.out.println("odontologo encontrado");
        return odontologo;

    }

    @Override
    public void eliminar(Integer id) {
        Connection connection=null;
        try{
            connection=BD.getConnection();

            PreparedStatement ps_insert= connection.prepareStatement(SQL_DELETE);
            ps_insert.setInt(1,id);

            ps_insert.execute();
            System.out.println("odontologo eliminado con exito");
        }catch (Exception e){
            System.out.println("Error eliminando el odontologo: "+e.getMessage());
        }

    }

    @Override
    public void actualizar(Odontologo odontologo) {
        Connection connection=null;
        try{
            connection=BD.getConnection();

            PreparedStatement ps_insert= connection.prepareStatement(SQL_UPDATE);
            ps_insert.setString(1,odontologo.getNombre());
            ps_insert.setString(2,odontologo.getApellido());
            ps_insert.setString(3,odontologo.getMatricula());
            ps_insert.setInt(4,odontologo.getId());

            ps_insert.execute();
            System.out.println("odontologo actualizado con exito");
        }catch (Exception e){
            System.out.println("Error actualizando el odontologo: "+e.getMessage());
        }
    }

    @Override
    public Odontologo buscarGenerico(String parametro) {
        Connection connection=null;
        Odontologo odontologo= null;
        try{
            connection=BD.getConnection();
            PreparedStatement ps_select_one= connection.prepareStatement(SQL_SELECT_BY_MATRICULA);
            ps_select_one.setString(1,parametro);
            ResultSet rs= ps_select_one.executeQuery();
            while(rs.next()){
                odontologo= new Odontologo(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4));
            }
        }catch (Exception e){
            System.out.println("Error buscando el odontologo: "+e.getMessage());
        }
        System.out.println("odontologo encontrado");
        return odontologo;
    }

    @Override
    public List<Odontologo> buscarTodos() {
        Connection connection=null;
        Odontologo odontologo= null;
        List<Odontologo> odontologos= new ArrayList<>();
        try{
            connection=BD.getConnection();
            PreparedStatement ps_select_one= connection.prepareStatement(SQL_SELECT_ALL);
            ResultSet rs= ps_select_one.executeQuery();
            while(rs.next()){
                odontologo= new Odontologo(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4));
                odontologos.add(odontologo);
            }
        }catch (Exception e){
            System.out.println("Error buscando el odontologo: "+e.getMessage());
        }
        System.out.println("odontologo encontrado");
        return odontologos;

    }
}
