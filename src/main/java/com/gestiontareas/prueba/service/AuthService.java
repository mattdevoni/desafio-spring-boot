package com.gestiontareas.prueba.service;

import com.gestiontareas.prueba.dto.auth.JwtResponse;
import com.gestiontareas.prueba.dto.auth.LoginRequest;
import com.gestiontareas.prueba.dto.auth.SignupRequest;
import org.springframework.http.ResponseEntity;

/**
 * Interfaz que define las operaciones disponibles para la autenticación de usuarios.
 */
public interface AuthService {
    
    /**
     * Autentica a un usuario y genera un token JWT.
     * 
     * @param loginRequest Credenciales de inicio de sesión
     * @return Respuesta con el token JWT y datos del usuario
     */
    JwtResponse authenticateUser(LoginRequest loginRequest);
    
    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param signupRequest Datos del nuevo usuario
     * @return Mensaje de respuesta
     */
    ResponseEntity<?> registerUser(SignupRequest signupRequest);
}