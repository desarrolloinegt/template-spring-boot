package com.ine.development.common.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.List;

/**
 * Interfaz base para repositorios JPA personalizados.
 * Proporciona métodos genéricos para manejar entidades con campos `isActive` e `isDelete`.
 *
 * @param <T>  el tipo de la entidad.
 * @param <ID> el tipo del identificador de la entidad.
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    List<T> findByStatus(Integer status);

    List<T> findByStatusIn(Collection<Integer> statuses);

    default List<T> findAllActive() {
        return findByStatus(1);
    }

    default List<T> findAllInactive() {
        return findByStatus(0);
    }
}