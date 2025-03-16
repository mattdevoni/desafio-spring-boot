package com.gestiontareas.prueba.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class UsuarioDTO {
    
    private final Long id;
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private final String username;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private final String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    private final String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private final String email;
    
    // Constructor vacío requerido para deserialización JSON
    public UsuarioDTO() {
        this.id = null;
        this.username = null;
        this.nombre = null;
        this.apellido = null;
        this.email = null;
    }
    
    // Constructor para crear un nuevo usuario (sin id)
    public UsuarioDTO(String username, String nombre, String apellido, String email) {
        this.id = null;
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }
    
    // Constructor completo para convertir desde entidad
    public UsuarioDTO(Long id, String username, String nombre, String apellido, String email) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }
}
