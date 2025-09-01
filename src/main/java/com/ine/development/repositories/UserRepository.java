package com.ine.development.repositories;

import com.ine.development.common.interfaces.BaseRepository;
import com.ine.development.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameAndUserIdNot(String username, Long userId);
    boolean existsByEmailAndUserIdNot(String email, Long userId);

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByUserIdAndIsDelete(Long id, Integer isDeleted);

}