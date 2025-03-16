package com.gestiontareas.prueba.controller;

import com.gestiontareas.prueba.dto.auth.JwtResponse;
import com.gestiontareas.prueba.dto.auth.LoginRequest;
import com.gestiontareas.prueba.dto.auth.SignupRequest;
import com.gestiontareas.prueba.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private JwtResponse jwtResponse;
    private Map<String, String> messageResponse;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        loginRequest = LoginRequest.builder()
                .username("usuario1")
                .password("password123")
                .build();

        signupRequest = SignupRequest.builder()
                .username("usuario3")
                .password("password123")
                .nombre("Juan")
                .apellido("Perez")
                .email("usuario3@ejemplo.com")
                .build();

        jwtResponse = JwtResponse.builder()
                .token("jwt-token")
                .type("Bearer")
                .id(1L)
                .username("usuario1")
                .email("usuario1@ejemplo.com")
                .build();

        messageResponse = new HashMap<>();
        messageResponse.put("message", "Usuario registrado exitosamente");
    }

    @Test
    void authenticateUser_DebeRetornarJwtResponse() {
        // Arrange
        when(authService.authenticateUser(loginRequest)).thenReturn(jwtResponse);

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtResponse, response.getBody());
        verify(authService, times(1)).authenticateUser(loginRequest);
    }

    @Test
    void registerUser_DebeRetornarMensajeExitoso() {
        // Arrange
        ResponseEntity<Map<String, String>> mockResponse = ResponseEntity.ok(messageResponse);
        doReturn(mockResponse).when(authService).registerUser(any(SignupRequest.class));

        // Act
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Usuario registrado exitosamente", responseBody.get("message"));
        
        verify(authService, times(1)).registerUser(signupRequest);
    }

    @Test
    void registerUser_CuandoUsuarioExiste_DebeRetornarError() {
        // Arrange
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Error: El nombre de usuario ya está en uso");
        
        ResponseEntity<Map<String, String>> mockResponse = ResponseEntity.badRequest().body(errorResponse);
        doReturn(mockResponse).when(authService).registerUser(any(SignupRequest.class));

        // Act
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Error: El nombre de usuario ya está en uso", responseBody.get("message"));
        
        verify(authService, times(1)).registerUser(signupRequest);
    }
}
