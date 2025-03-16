package com.gestiontareas.prueba.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
@Schema(description = "Objeto para la solicitud de registro de usuario")
public class SignupRequest {
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre de usuario único para el registro", example = "usuario3", required = true)
    private final String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 40, message = "La contraseña debe tener entre 6 y 40 caracteres")
    @Schema(description = "Contraseña del usuario", example = "password123", required = true)
    private final String password;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Schema(description = "Nombre del usuario", example = "Juan", required = true)
    private final String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    @Schema(description = "Apellido del usuario", example = "Pérez", required = true)
    private final String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    @Email(message = "El formato del email no es válido")
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@ejemplo.com", required = true)
    private final String email;
    
    // Constructor vacío requerido para deserialización JSON
    public SignupRequest() {
        this.username = null;
        this.password = null;
        this.nombre = null;
        this.apellido = null;
        this.email = null;
    }
}
