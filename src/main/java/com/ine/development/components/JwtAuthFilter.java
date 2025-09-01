package com.ine.development.components;

import com.ine.development.models.User;
import com.ine.development.repositories.UserRepository;
import com.ine.development.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro personalizado para la autenticación basada en JWT.
 * Este filtro se ejecuta una vez por solicitud y verifica la validez del token JWT
 * en el encabezado de autorización de la solicitud.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    /**
     * Método que implementa la lógica del filtro.
     * Verifica si la solicitud contiene un token JWT válido y, si es así, autentica al usuario.
     *
     * @param req   el objeto `HttpServletRequest` de la solicitud.
     * @param res   el objeto `HttpServletResponse` de la respuesta.
     * @param chain la cadena de filtros para continuar con el procesamiento.
     * @throws ServletException si ocurre un error durante el procesamiento del filtro.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = header.substring(7);
        try {
            String sub = jwtService.extractSubject(token);
            Long userId = Long.valueOf(sub);

            User u = userRepository.findById(userId).orElse(null);
            if (u != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var auth = new UsernamePasswordAuthenticationToken(
                        u.getUsername(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (io.jsonwebtoken.JwtException | NumberFormatException e) {
            SecurityContextHolder.clearContext();
            req.setAttribute("jwt_error", e.getMessage());
        }
        chain.doFilter(req, res);
    }
}
