package com.ine.development.services;

import com.ine.development.models.User;
import com.ine.development.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }


    @Transactional(readOnly = true)
    public User authenticate(String login, String rawPassword) {
        User user = findByLogin(login);
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BadCredentialsException("Credenciales inválidas.");
        }
        return user;
    }


    private User findByLogin(String login) {
        boolean isEmail = login.contains("@");
        return (isEmail
                ? userRepository.findByEmail(login)
                : userRepository.findByUsername(login))
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas."));
    }
}
