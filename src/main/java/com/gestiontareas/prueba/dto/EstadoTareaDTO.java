package com.gestiontareas.prueba.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Schema(description = "Objeto que representa un estado de tarea")
public class EstadoTareaDTO {
    
    @Schema(description = "Identificador único del estado de tarea", example = "1")
    private final Long id;
    
    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre del estado debe tener entre 2 y 50 caracteres")
    @Schema(description = "Nombre del estado de tarea", example = "Pendiente", required = true)
    private final String nombre;
    
    @Size(max = 200, message = "La descripción no puede exceder los 200 caracteres")
    @Schema(description = "Descripción del estado de tarea", example = "Tareas que aún no han sido iniciadas")
    private final String descripcion;
    
    // Constructor para crear un nuevo estado (sin id)
    public EstadoTareaDTO(String nombre, String descripcion) {
        this.id = null;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // Constructor completo para convertir desde entidad
    public EstadoTareaDTO(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}
