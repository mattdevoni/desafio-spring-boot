package com.gestiontareas.prueba.security.jwt;

import com.gestiontareas.prueba.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        userDetails = new UserDetailsImpl(
                1L,
                "usuario1",
                "usuario1@ejemplo.com",
                "Nombre",
                "Apellido",
                "password123",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Configurar propiedades del JwtUtils usando ReflectionTestUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "gestionTareasSecretKeyMuySeguraConAlMenos32Caracteres");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000); // 24 horas
    }

    @Test
    void generateJwtToken_DebeGenerarTokenValido() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void getUserNameFromJwtToken_DebeRetornarUsername() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        // Act
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals("usuario1", username);
    }

    @Test
    void validateJwtToken_ConTokenValido_DebeRetornarTrue() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_ConTokenInvalido_DebeRetornarFalse() {
        // Act
        boolean isValid = jwtUtils.validateJwtToken("tokenInvalido");

        // Assert
        assertFalse(isValid);
    }
}
