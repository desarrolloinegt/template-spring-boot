package com.ine.development.models.dto;

import com.ine.development.common.interfaces.OnUpdate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Representa una solicitud para deshabilitar o eliminar un recurso.
 * Utiliza validaciones para garantizar que los valores sean válidos y estén dentro del rango permitido.
 *
 * @param isActive indica si el recurso está activo (1) o inactivo (0). Obligatorio para actualizaciones.
 * @param isDelete indica si el recurso está marcado para eliminación (1) o no (0). Obligatorio para actualizaciones.
 */
public record DisableRequest(
        @NotNull(groups = OnUpdate.class)
        @Min(value = 0, groups = OnUpdate.class)
        @Max(value = 1, groups = OnUpdate.class)
        Integer isActive,

        @NotNull(groups = OnUpdate.class)
        @Min(value = 0, groups = OnUpdate.class)
        @Max(value = 1, groups = OnUpdate.class)
        Integer isDelete
) {
}
