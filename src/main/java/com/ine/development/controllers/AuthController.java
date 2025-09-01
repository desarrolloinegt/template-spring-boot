package com.ine.development.controllers;

import com.ine.development.common.ResponseFactory;
import com.ine.development.common.dto.ApiResponse;
import com.ine.development.models.User;
import com.ine.development.models.dto.LoginRequest;
import com.ine.development.services.AuthService;
import com.ine.development.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Autenticación", description = "Operaciones de autenticación y autorización")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Autenticación", description = "Autentica un usuario y genera un token de autenticación.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody @Valid LoginRequest req) {
        User user = authService.authenticate(req.email(), req.password());

        String token = jwtService.generateToken(
                String.valueOf(user.getUserId()),
                Map.of("username", user.getUsername(), "email", user.getEmail())
        );

        Map<String, Object> payload = Map.of(
                "token", token,
                "user", user
        );
        return ResponseFactory.ok("Inicio de sesión exitoso.", payload);

    }
}
