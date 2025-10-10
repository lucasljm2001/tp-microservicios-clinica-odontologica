package service;

import java.util.List;

public interface iService<T> {
    T guardar(T t);
    T buscar(Integer id);
    void eliminar(Integer id);
    void actualizar(T t);
    T buscarGenerico(String parametro);
    List<T> buscarTodos();
}
