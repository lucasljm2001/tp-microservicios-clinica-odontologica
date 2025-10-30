package com.clinica.dto;



public class ExceptionDTO {
    private String message;
    private int status;

    public ExceptionDTO(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ExceptionDTO() {
    }


    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
