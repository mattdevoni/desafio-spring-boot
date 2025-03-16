package com.gestiontareas.prueba.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@Schema(description = "Objeto que representa una tarea")
public class TareaDTO {
    
    @Schema(description = "Identificador único de la tarea", example = "6")
    private final Long id;
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Schema(description = "Título de la tarea", example = "Gestionar proveedores", required = true)
    private final String titulo;
    
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    @Schema(description = "Descripción detallada de la tarea", example = "Contactar nuevos proveedores para mejorar costos")
    private final String descripcion;
    
    @Schema(description = "Fecha de creación de la tarea", example = "2025-03-15T10:30:00")
    private final LocalDateTime fechaCreacion;
    
    @FutureOrPresent(message = "La fecha de vencimiento debe ser actual o futura")
    @Schema(description = "Fecha de vencimiento de la tarea", example = "2025-03-25T18:00:00", required = true)
    private final LocalDateTime fechaVencimiento;
    
    @NotNull(message = "El estado de la tarea es obligatorio")
    @Schema(description = "ID del estado de la tarea", example = "1", required = true)
    private final Long estadoId;
    
    @Schema(description = "Nombre del estado de la tarea", example = "Pendiente")
    private final String estadoNombre;
    
    @Schema(description = "ID del usuario propietario de la tarea", example = "1")
    private final Long usuarioId;
    
    @Schema(description = "Nombre del usuario propietario de la tarea", example = "Juan Pérez")
    private final String usuarioNombre;
    
    @Schema(description = "Prioridad de la tarea (1: Alta, 2: Media, 3: Baja)", example = "1")
    private final Integer prioridad;
    
    // Constructor vacío requerido para deserialización JSON
    public TareaDTO() {
        this.id = null;
        this.titulo = null;
        this.descripcion = null;
        this.fechaCreacion = null;
        this.fechaVencimiento = null;
        this.estadoId = null;
        this.estadoNombre = null;
        this.usuarioId = null;
        this.usuarioNombre = null;
        this.prioridad = null;
    }
    
    // Constructor para crear una nueva tarea (sin id ni fechaCreacion)
    public TareaDTO(String titulo, String descripcion, LocalDateTime fechaVencimiento,
                   Long estadoId, String estadoNombre, Long usuarioId, String usuarioNombre,
                   Integer prioridad) {
        this.id = null;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = null;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoId = estadoId;
        this.estadoNombre = estadoNombre;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.prioridad = prioridad;
    }
    
    // Constructor completo para convertir desde entidad
    public TareaDTO(Long id, String titulo, String descripcion, LocalDateTime fechaCreacion,
                  LocalDateTime fechaVencimiento, Long estadoId, String estadoNombre,
                  Long usuarioId, String usuarioNombre, Integer prioridad) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoId = estadoId;
        this.estadoNombre = estadoNombre;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.prioridad = prioridad;
    }
}
