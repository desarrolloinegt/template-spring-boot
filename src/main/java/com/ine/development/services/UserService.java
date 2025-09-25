package com.ine.development.services;

import com.ine.development.common.interfaces.CrudOptions;
import com.ine.development.common.interfaces.OnCreate;
import com.ine.development.common.interfaces.OnUpdate;
import com.ine.development.models.User;
import com.ine.development.models.dto.UserDto;
import com.ine.development.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

import static com.ine.development.common.Updaters.*;

/**
 * Servicio para la gestión de usuarios.
 * Implementa las operaciones CRUD definidas en la interfaz CrudOptions.
 */
@Service
@RequiredArgsConstructor
public class UserService implements CrudOptions<User, Long, UserDto, UserDto> {

    @PersistenceContext
    private EntityManager em;

    private final AuthService authService;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findByIdAndStatus(id, 1)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(Integer... status) {
        if (status == null || status.length == 0) {
            return userRepository.findAllActive();
        }
        if (status.length == 1) {
            Integer s = (status[0] == null) ? 1 : status[0];
            return userRepository.findByStatus(s);
        }
        List<Integer> listStatus = Arrays.stream(status)
                .map(s -> s == null ? 1 : s)
                .distinct()
                .toList();
        return userRepository.findByStatusIn(listStatus);
    }


    @Override
    @Transactional
    @Validated(OnCreate.class)
    public User create(@Valid UserDto user) {
        validateEmailAndUsername(null, user.email(), user.name());
        User newUser =  new User();
        applyPatch(newUser, user);
        User saved = userRepository.save(newUser);
        em.flush();
        em.refresh(saved);
        return saved;
    }

    @Override
    @Transactional
    @Validated(OnUpdate.class)
    public User update(Long id, @Valid UserDto req) {
        User u = findById(id);
        validateEmailAndUsername(id, req.email(), req.name());
        applyPatch(u, req);
        em.flush();
        em.refresh(u);
        return u;
    }

    @Override
    @Transactional
    @Validated(OnUpdate.class)
    public void disable(Long id) {
        User u = findById(id);
        u.setStatus(0);
        em.flush();
        em.refresh(u);
    }

    /**
     * Aplica los cambios de un DTO a una entidad User.
     *
     * @param user Entidad User a modificar.
     * @param req  DTO con los datos a aplicar.
     */
    private void applyPatch(User user, UserDto req) {
        setIfNonNull(req.name(), user::setName);
        setIfNonNull(req.email(), user::setEmail);
        setIfNonNull(req.phone(), user::setPhone);
        setIfNonNull(req.password(), password -> user.setPassword(authService.encodePassword(password)) );
    }

    /**
     * Valida que el correo y el nombre de usuario no estén en uso.
     *
     * @param id    ID del usuario (puede ser nulo para creación).
     * @param email Correo a validar.
     * @param name  Nombre de usuario a validar.
     * @throws IllegalArgumentException Si el correo o el nombre ya están en uso.
     */
    private void validateEmailAndUsername(@Nullable Long id, String email, String name) {
        String u = name == null ? null : name.trim();
        String e = email == null ? null : email.trim().toLowerCase();

        if (u != null && (id == null
                ? userRepository.existsByName(u)
                : userRepository.existsByNameAndIdNot(u, id))) {
            throw new IllegalArgumentException("El usuario que ingresó ya existe.");
        }

        if (e != null && (id == null
                ? userRepository.existsByEmail(e)
                : userRepository.existsByEmailAndIdNot(e, id))) {
            throw new IllegalArgumentException("El correo que ingresó ya existe.");
        }
    }

}