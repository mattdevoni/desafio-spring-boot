package com.gestiontareas.prueba.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto para crear una nueva tarea")
public class CrearTareaDTO {
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Schema(description = "Título de la tarea", example = "Gestionar proveedores", required = true)
    private String titulo;
    
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    @Schema(description = "Descripción detallada de la tarea", example = "Contactar nuevos proveedores para mejorar costos")
    private String descripcion;
    
    @FutureOrPresent(message = "La fecha de vencimiento debe ser actual o futura")
    @Schema(description = "Fecha de vencimiento de la tarea", example = "2025-04-15T18:00:00", required = true)
    private LocalDateTime fechaVencimiento;
    
    @NotNull(message = "El estado de la tarea es obligatorio")
    @Schema(description = "ID del estado de la tarea", example = "1", required = true)
    private Long estadoId;
    
    @Schema(description = "Prioridad de la tarea (1: Alta, 2: Media, 3: Baja)", example = "2")
    private Integer prioridad;
}
