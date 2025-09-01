package com.ine.development.common.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

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

    List<T> findByIsActiveAndIsDelete(Integer isActive, Integer isDeleted);
    List<T> findByIsActiveInAndIsDelete(List<Integer> statuses, Integer isDeleted);

    default List<T> findAllActive() {
        return findByIsActiveAndIsDelete(1, 0);
    }

    default List<T> findAllInactive() {
        return findByIsActiveAndIsDelete(0, 0);
    }
}