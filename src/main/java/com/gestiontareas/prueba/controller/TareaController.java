package com.gestiontareas.prueba.controller;

import com.gestiontareas.prueba.dto.TareaDTO;
import com.gestiontareas.prueba.dto.CrearTareaDTO;
import com.gestiontareas.prueba.dto.ActualizarTareaDTO;
import com.gestiontareas.prueba.security.services.UserDetailsImpl;
import com.gestiontareas.prueba.service.TareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tareas")
@Tag(name = "Tareas", description = "API para la gestión de tareas")
@SecurityRequirement(name = "bearerAuth")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping
    @Operation(summary = "Listar tareas", description = "Obtiene todas las tareas del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tareas obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TareaDTO.class)))),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<TareaDTO>> getAllTareas() {
        Long userId = getCurrentUserId();
        List<TareaDTO> tareas = tareaService.getAllTareas(userId);
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tarea por ID", description = "Obtiene una tarea específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarea encontrada",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TareaDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - La tarea no pertenece al usuario"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<TareaDTO> getTareaById(
            @Parameter(description = "ID de la tarea a buscar", required = true)
            @PathVariable Long id) {
        Long userId = getCurrentUserId();
        TareaDTO tarea = tareaService.getTareaById(id, userId);
        return ResponseEntity.ok(tarea);
    }

    @PostMapping
    @Operation(summary = "Crear tarea", description = "Crea una nueva tarea para el usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarea creada exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TareaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de tarea inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "404", description = "Estado de tarea no encontrado")
    })
    public ResponseEntity<TareaDTO> createTarea(
            @Parameter(description = "Datos de la tarea a crear", required = true)
            @Valid @RequestBody CrearTareaDTO crearTareaDTO) {
        Long userId = getCurrentUserId();
        TareaDTO createdTarea = tareaService.createTarea(crearTareaDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTarea);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tarea", description = "Actualiza una tarea existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TareaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de tarea inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - La tarea no pertenece al usuario"),
        @ApiResponse(responseCode = "404", description = "Tarea o estado de tarea no encontrado")
    })
    public ResponseEntity<TareaDTO> updateTarea(
            @Parameter(description = "ID de la tarea a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados de la tarea", required = true)
            @Valid @RequestBody ActualizarTareaDTO actualizarTareaDTO) {
        Long userId = getCurrentUserId();
        TareaDTO updatedTarea = tareaService.updateTarea(id, actualizarTareaDTO, userId);
        return ResponseEntity.ok(updatedTarea);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tarea", description = "Elimina una tarea existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarea eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - La tarea no pertenece al usuario"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<Void> deleteTarea(
            @Parameter(description = "ID de la tarea a eliminar", required = true)
            @PathVariable Long id) {
        Long userId = getCurrentUserId();
        tareaService.deleteTarea(id, userId);
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
}
