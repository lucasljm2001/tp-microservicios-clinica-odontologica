package com.clinica;
import com.clinica.dao.BD;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClinicaApplication {
    public static void main(String[] args) {
        BD.crearTablas();
        SpringApplication.run(ClinicaApplication.class, args);
    }
}
