package com.ine.development.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ine.development.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Componente personalizado que implementa `AuthenticationEntryPoint` para manejar
 * intentos de acceso no autenticados en la aplicación.
 *
 * Este componente se encarga de devolver una respuesta JSON con un mensaje de error
 * cuando un usuario no autenticado intenta acceder a un recurso protegido.
 */
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Método sobrescrito que se invoca cuando un usuario no autenticado intenta acceder
     * a un recurso protegido. Configura la respuesta HTTP con un estado 401 (No autorizado)
     * y un cuerpo JSON que describe el error.
     *
     * @param request       el objeto `HttpServletRequest` de la solicitud.
     * @param response      el objeto `HttpServletResponse` de la respuesta.
     * @param authException la excepción que indica que la autenticación falló.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        try {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            var body = ApiResponse.fail("Acceso denegado, usuario no autenticado.");
            mapper.writeValue(response.getWriter(), body);
        } catch (Exception ignored) {}
    }
}