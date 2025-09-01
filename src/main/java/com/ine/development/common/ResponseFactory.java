package com.ine.development.common;

import com.ine.development.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Clase de utilidad para crear respuestas HTTP estandarizadas utilizando la clase ApiResponse.
 * Proporciona métodos para generar respuestas exitosas y fallidas con diferentes estados HTTP.
 */
public final class ResponseFactory {

    private ResponseFactory() {}

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponse.ok(message, data));
    }

    public static ResponseEntity<ApiResponse<Void>> created(String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(message, null));
    }

    public static ResponseEntity<ApiResponse<Void>> fail(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.fail(message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> createdWithBody(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(message, data));
    }
}