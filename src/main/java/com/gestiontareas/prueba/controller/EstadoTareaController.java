package com.gestiontareas.prueba.controller;

import com.gestiontareas.prueba.dto.EstadoTareaDTO;
import com.gestiontareas.prueba.service.EstadoTareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de estados de tarea.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/estados")
@Tag(name = "Estados de Tarea", description = "API para la gestión de estados de tarea")
@SecurityRequirement(name = "bearerAuth")
public class EstadoTareaController {

    private final EstadoTareaService estadoTareaService;

    public EstadoTareaController(EstadoTareaService estadoTareaService) {
        this.estadoTareaService = estadoTareaService;
    }

    /**
     * Obtiene todos los estados de tarea disponibles.
     */
    @GetMapping
    @Operation(summary = "Listar estados de tarea", description = "Obtiene todos los estados de tarea disponibles en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de estados obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EstadoTareaDTO.class)))),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<EstadoTareaDTO>> getAllEstados() {
        List<EstadoTareaDTO> estados = estadoTareaService.getAllEstados();
        return ResponseEntity.ok(estados);
    }
    
    /**
     * Obtiene un estado de tarea por su ID.
     * 
     * @param id El ID del estado de tarea a buscar
     * @return ResponseEntity con el estado de tarea encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado por ID", description = "Obtiene un estado de tarea específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado encontrado",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EstadoTareaDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "404", description = "Estado de tarea no encontrado")
    })
    public ResponseEntity<EstadoTareaDTO> getEstadoById(
            @Parameter(description = "ID del estado de tarea a buscar", required = true)
            @PathVariable Long id) {
        EstadoTareaDTO estado = estadoTareaService.getEstadoById(id);
        return ResponseEntity.ok(estado);
    }
}
