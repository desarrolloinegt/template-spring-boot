package com.ine.development.repositories;

import com.ine.development.common.interfaces.BaseRepository;
import com.ine.development.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    boolean existsByName(String name);
    boolean existsByEmail(String email);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByIdAndStatus(Long id, Integer status);

}