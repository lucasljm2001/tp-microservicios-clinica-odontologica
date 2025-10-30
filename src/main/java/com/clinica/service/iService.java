package com.clinica.service;

import com.clinica.exception.ResourceNotFoundException;

import java.util.List;

public interface iService<T> {
    T guardar(T t);
    T buscar(Long id) throws ResourceNotFoundException;
    void eliminar(Long id);
    void actualizar(T t);
    T buscarGenerico(String parametro) throws ResourceNotFoundException;
    List<T> buscarTodos();
}
