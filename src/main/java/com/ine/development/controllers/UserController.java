package com.ine.development.controllers;

import com.ine.development.common.ResponseFactory;
import com.ine.development.common.dto.ApiResponse;
import com.ine.development.common.interfaces.OnCreate;
import com.ine.development.common.interfaces.OnUpdate;
import com.ine.development.models.User;
import com.ine.development.models.dto.UserDto;
import com.ine.development.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Operaciones sobre usuarios")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userServices;

    public UserController(UserService userServices) {
        this.userServices = userServices;
    }

    @Operation(summary = "Listar usuarios", description = "Obtiene una lista de todos los usuarios. Se puede filtrar por estado (activo/inactivo).")
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> findAllUsers(
            @Parameter(description = "Estados a filtrar (0=Inactivo, 1=Activo). Ej: ?status=1,0")
            @RequestParam(name = "status", required = false) List<Integer> status
    ) {
        List<User> data = (status == null || status.isEmpty())
                ? userServices.findAll()
                : userServices.findAll(status.toArray(Integer[]::new));

        return ResponseFactory.ok("Usuarios obtenidos correctamente.", data);
    }

    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario por su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> findById(
            @Parameter(description = "Filtrar por el id del usuario")
            @PathVariable Long id){
        User data = userServices.findById(id);
        return ResponseFactory.ok("Usuario obtenido correctamente.", data);
    }

    @Operation(summary = "Crear un usuario", description = "Crea un nuevo usuario.")
    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(@Validated(OnCreate.class) @RequestBody UserDto req){
        User saved = userServices.create(req);
        return ResponseFactory.createdWithBody("Usuario creado correctamente.", saved);
    }

    @Operation(summary = "Actualizar un usuario", description = "Actualiza un usuario existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> update(
            @Parameter(description = "Actualiza por el id del usuario")
            @PathVariable Long id,
            @Validated(OnUpdate.class)
            @RequestBody UserDto req){
        User updated = userServices.update(id, req);
        return ResponseFactory.ok("Usuario actualizado correctamente.", updated);
    }

    @Operation(summary = "Desactivar un usuario", description = "Desactiva un usuario existente.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateStatus(
            @Parameter(description = "Deshabilita por el id del usuario")
            @PathVariable Long id){
        userServices.disable(id);
        return ResponseFactory.ok("Usuario deshabilitado correctamente.", null);
    }
}
