package com.ine.development.models.dto;

import com.ine.development.common.interfaces.OnCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Representa una solicitud de usuario con los datos necesarios para su creación.
 * Utiliza validaciones para garantizar la integridad de los datos.
 *
 * @param name el nombre de usuario, obligatorio y con una longitud entre 3 y 30 caracteres.
 * @param email el correo electrónico del usuario, obligatorio, con una longitud entre 10 y 50 caracteres, y en formato válido.
 * @param password la contraseña del usuario, obligatoria y con una longitud mínima de 3 caracteres.
 * @param phone el número de teléfono del usuario, obligatorio.
 */
public record UserDto(
        @NotBlank(groups = OnCreate.class, message = "El campo nombre es obligatorio")
        @Size(min = 3, max = 30)
        String name,

        @NotBlank(groups = OnCreate.class, message = "El campo email es obligatorio")
        @Size(min = 10, max = 50) @Email
        String email,

        @NotBlank(groups = OnCreate.class, message = "El campo password es obligatorio")
        @Size(min = 3, groups = OnCreate.class, message = "El campo password debe tener al menos 3 caracteres")
        String password,

        @NotBlank(groups = OnCreate.class, message = "El campo de telefono es obligatorio")
        String phone

) {}