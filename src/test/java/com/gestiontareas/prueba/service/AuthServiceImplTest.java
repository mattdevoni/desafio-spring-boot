package com.gestiontareas.prueba.service;

import com.gestiontareas.prueba.dto.auth.JwtResponse;
import com.gestiontareas.prueba.dto.auth.LoginRequest;
import com.gestiontareas.prueba.dto.auth.SignupRequest;
import com.gestiontareas.prueba.model.Usuario;
import com.gestiontareas.prueba.repository.UsuarioRepository;
import com.gestiontareas.prueba.security.jwt.JwtUtils;
import com.gestiontareas.prueba.security.services.UserDetailsImpl;
import com.gestiontareas.prueba.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private Usuario usuario;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        loginRequest = LoginRequest.builder()
                .username("usuario1")
                .password("password123")
                .build();

        signupRequest = new SignupRequest(
            "usuario3",
            "password123",
            "Juan",
            "Perez",
            "usuario3@ejemplo.com"
        );

        usuario = new Usuario(
            1L,
            "usuario1",
            "encodedPassword",
            "Matias",
            "Alarcon",
            "usuario1@ejemplo.com"
        );

        userDetails = new UserDetailsImpl(
                1L,
                "usuario1",
                "usuario1@ejemplo.com",
                "Matias",
                "Alarcon",
                "encodedPassword",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void authenticateUser_DebeRetornarJwtResponse() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Act
        JwtResponse response = authService.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(1L, response.getId());
        assertEquals("usuario1", response.getUsername());
        assertEquals("usuario1@ejemplo.com", response.getEmail());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
    }

    @Test
    void registerUser_CuandoUsuarioNoExiste_DebeRegistrarUsuario() {
        // Arrange
        when(usuarioRepository.existsByUsername("usuario3")).thenReturn(false);
        when(usuarioRepository.existsByEmail("usuario3@ejemplo.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ResponseEntity<?> response = authService.registerUser(signupRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Usuario registrado exitosamente", responseBody.get("message"));
        
        verify(usuarioRepository, times(1)).existsByUsername("usuario3");
        verify(usuarioRepository, times(1)).existsByEmail("usuario3@ejemplo.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registerUser_CuandoUsernameExiste_DebeRetornarError() {
        // Arrange
        when(usuarioRepository.existsByUsername("usuario3")).thenReturn(true);

        // Act
        ResponseEntity<?> response = authService.registerUser(signupRequest);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Error: El nombre de usuario ya está en uso", responseBody.get("message"));
        
        verify(usuarioRepository, times(1)).existsByUsername("usuario3");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registerUser_CuandoEmailExiste_DebeRetornarError() {
        // Arrange
        when(usuarioRepository.existsByUsername("usuario3")).thenReturn(false);
        when(usuarioRepository.existsByEmail("usuario3@ejemplo.com")).thenReturn(true);

        // Act
        ResponseEntity<?> response = authService.registerUser(signupRequest);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Error: El email ya está en uso", responseBody.get("message"));
        
        verify(usuarioRepository, times(1)).existsByUsername("usuario3");
        verify(usuarioRepository, times(1)).existsByEmail("usuario3@ejemplo.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
