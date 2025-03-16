package com.gestiontareas.prueba.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Schema(description = "Objeto para la solicitud de inicio de sesión")
public class LoginRequest {
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Schema(description = "Nombre de usuario para iniciar sesión", example = "usuario1", required = true)
    private final String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "password123", required = true)
    private final String password;
    
    // Constructor requerido para deserialización JSON
    public LoginRequest() {
        this.username = null;
        this.password = null;
    }
    
    // Constructor completo
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
