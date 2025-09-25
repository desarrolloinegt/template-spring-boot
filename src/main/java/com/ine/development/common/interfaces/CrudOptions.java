package com.ine.development.common.interfaces;

import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD personalizadas.
 *
 * @param <E>  el tipo de la entidad.
 * @param <ID> el tipo del identificador de la entidad.
 * @param <C>  el tipo del payload para la creación.
 * @param <U>  el tipo del payload para la actualización.
 */
public interface CrudOptions<E, ID, C, U> {
    E create(C payload);
    E update(ID id, U payload);
    void disable(ID id);
    List<E> findAll(Integer... status);
    E findById(ID id);
}
