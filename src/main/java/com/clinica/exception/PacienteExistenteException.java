package com.clinica.exception;

public class PacienteExistenteException extends ResourceExistingException {
    public PacienteExistenteException(String message) {
        super(message);
    }
}
