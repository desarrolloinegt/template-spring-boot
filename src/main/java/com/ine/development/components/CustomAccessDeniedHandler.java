package com.ine.development.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ine.development.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Método sobrescrito que se invoca cuando un usuario autenticado intenta acceder
     * a un recurso para el cual no tiene los permisos necesarios. Configura la respuesta
     * HTTP con un estado 403 (Prohibido) y un cuerpo JSON que describe el error.
     *
     * @param request                el objeto `HttpServletRequest` de la solicitud.
     * @param response               el objeto `HttpServletResponse` de la respuesta.
     * @param accessDeniedException  la excepción que indica que el acceso fue denegado.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        try {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            var body = ApiResponse.fail("Acceso denegado: permisos insuficientes.");
            mapper.writeValue(response.getWriter(), body);
        } catch (Exception ignored) {}
    }
}
