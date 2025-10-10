package dao;

import model.Domicilio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

public class BD {
    private static final String SQL_DROP_CREATE_DOMCILIOS="DROP TABLE IF EXISTS DOMICILIOS; CREATE TABLE DOMICILIOS(ID INT AUTO_INCREMENT PRIMARY KEY, CALLE VARCHAR(100) NOT NULL, NUMERO INT NOT NULL, LOCALIDAD VARCHAR(100) NOT NULL, PROVINCIA VARCHAR(100) NOT NULL)";
    private static final String SQL_DROP_CREATE_PACIENTES="DROP TABLE IF EXISTS PACIENTES; CREATE TABLE PACIENTES(ID INT AUTO_INCREMENT PRIMARY KEY, NOMBRE VARCHAR(100) NOT NULL, APELLIDO VARCHAR(100) NOT NULL, NUMEROCONTACTO INT NOT NULL, FECHA DATE NOT NULL, DOMICILIO_ID INT NOT NULL, EMAIL VARCHAR(100) NOT NULL)";
    private static final String prueba=" INSERT INTO DOMICILIOS(CALLE, NUMERO, LOCALIDAD, PROVINCIA) VALUES('siempre viva','723','Springfield','USA'), ('Calle falsa','123', 'Sprinfield', 'USA');"+
    "INSERT INTO PACIENTES(NOMBRE, APELLIDO, NUMEROCONTACTO, FECHAINGRESO, DOMICILIO_ID, EMAIL) VALUES('Homero','Simpson','1111111111','2025-10-09','1','homer@disney.com'),('Marge','Simpson','11111','2025-08-08','2','marge@fox.com')" ;

public static void crearTablas(){
//primero deberia llamar a la conexion
    Connection connection= null;
    try{
        connection=getConnection();
        Statement statement= connection.createStatement();
        statement.execute(SQL_DROP_CREATE_DOMCILIOS);
        statement.execute(SQL_DROP_CREATE_PACIENTES);
        System.out.println("aca deberia mostrar");
        statement.execute(prueba);
        System.out.println("datos persistidos");
    }catch (Exception e){
        System.out.println("error: "+e.getMessage());
            }
}
    public static Connection getConnection() throws Exception{
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/clinicaFeliz","sa","sa");
    }
}
