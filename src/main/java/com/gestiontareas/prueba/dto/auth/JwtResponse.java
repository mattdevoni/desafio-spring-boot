package com.gestiontareas.prueba.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Schema(description = "Objeto de respuesta con el token JWT después de la autenticación")
public class JwtResponse {
    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMSIsImlhdCI6MTY3...", required = true)
    private final String token;
    
    @Schema(description = "Tipo de token", example = "Bearer", required = true)
    private final String type;
    
    @Schema(description = "ID del usuario autenticado", example = "1", required = true)
    private final Long id;
    
    @Schema(description = "Nombre de usuario", example = "usuario1", required = true)
    private final String username;
    
    @Schema(description = "Correo electrónico del usuario", example = "usuario1@ejemplo.com", required = true)
    private final String email;
    
    public JwtResponse(String token, Long id, String username, String email) {
        this.token = token;
        this.type = "Bearer";
        this.id = id;
        this.username = username;
        this.email = email;
    }
    
    // Constructor completo para casos especiales
    public JwtResponse(String token, String type, Long id, String username, String email) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
