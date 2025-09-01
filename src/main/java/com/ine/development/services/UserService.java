package com.ine.development.services;

import com.ine.development.common.interfaces.CrudOptions;
import com.ine.development.common.interfaces.OnCreate;
import com.ine.development.common.interfaces.OnUpdate;
import com.ine.development.models.User;
import com.ine.development.models.dto.DisableRequest;
import com.ine.development.models.dto.UserRequest;
import com.ine.development.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements CrudOptions<User, Long, UserRequest, UserRequest, DisableRequest> {

    @PersistenceContext
    private EntityManager em;

    private final AuthService authService;
    private final UserRepository userRepository;

    public UserService(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findByUserIdAndIsDelete(id, 0)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(Integer... status) {
        if (status == null || status.length == 0) {
            return userRepository.findByIsActiveAndIsDelete(1, 0);
        }
        if (status.length == 1) {
            return userRepository.findByIsActiveAndIsDelete(status[0], 0);
        }
        return userRepository.findByIsActiveInAndIsDelete(Arrays.asList(status), 0);
    }


    @Override
    @Transactional
    @Validated(OnCreate.class)
    public User create(@Valid UserRequest user) {
        if (userRepository.existsByUsername(user.username())) {
            throw new IllegalArgumentException("El usuario que ingresó ya existe.");
        }
        if (userRepository.existsByEmail(user.email())) {
            throw new IllegalArgumentException("El correo que ingresó ya existe.");
        }

        User newUser =  new User();
        newUser.setUsername(user.username());
        newUser.setEmail(user.email());
        newUser.setPasswordHash(authService.encodePassword(user.password()));
        User saved = userRepository.save(newUser);
        em.flush();
        em.refresh(saved);
        return saved;
    }

    @Override
    @Transactional
    @Validated(OnUpdate.class)
    public User update(Long id, @Valid UserRequest req) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        if (req.username() != null && !req.username().equals(u.getUsername())) {
            if (userRepository.existsByUsernameAndUserIdNot(req.username(), id)) {
                throw new IllegalArgumentException("El usuario que ingresó ya existe.");
            }
            u.setUsername(req.username());
        }

        if (req.email() != null && !req.email().equals(u.getEmail())) {
            if (userRepository.existsByEmailAndUserIdNot(req.email(), id)) {
                throw new IllegalArgumentException("El correo que ingresó ya existe.");
            }
            u.setEmail(req.email());
        }

        if (req.password() != null && !req.password().isBlank()) {
            u.setPasswordHash(authService.encodePassword(req.password()));
        }
        em.flush();
        em.refresh(u);
        return u;
    }

    @Override
    @Transactional
    @Validated(OnUpdate.class)
    public User disable(Long id, @Valid DisableRequest req) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        if (req.isActive() != null) {
            u.setIsActive(req.isActive());
        }

        if (req.isDelete() != null) {
            u.setIsDelete(req.isDelete());
        }

        em.flush();
        em.refresh(u);
        return u;
    }

}