package com.ine.development.models.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Representa una solicitud de usuario con los datos necesarios para su creación.
 * Utiliza validaciones para garantizar la integridad de los datos.
 *
 * @param email el correo electrónico del usuario, obligatorio, con una longitud entre 10 y 50 caracteres, y en formato válido.
 * @param password la contraseña del usuario, obligatoria y con una longitud mínima de 3 caracteres.
 */
public record LoginRequest(
        @NotBlank(message = "El email es obligatorio") String email,
        @NotBlank(message = "El campo de contraseña es obligatorio") String password
) {
}
