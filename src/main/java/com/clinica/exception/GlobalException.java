package com.clinica.exception;

import com.clinica.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ExceptionDTO> tratamientoRNFE(ResourceNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler({ResourceExistingException.class})
    public ResponseEntity<ExceptionDTO> resourceExisting(ResourceExistingException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler({TurnoComprometidoException.class})
    public ResponseEntity<ExceptionDTO> turnoComprometidoException(TurnoComprometidoException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler({FechaInvalidaException.class})
    public ResponseEntity<ExceptionDTO> fechaInvalidaException(FechaInvalidaException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}

