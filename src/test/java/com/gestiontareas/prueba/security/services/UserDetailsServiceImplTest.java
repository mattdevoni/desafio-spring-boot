package com.gestiontareas.prueba.security.services;

import com.gestiontareas.prueba.model.Usuario;
import com.gestiontareas.prueba.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Configurar datos de prueba
        usuario = new Usuario(1L, "usuario1", "password123", "Usuario", "Uno", "usuario1@example.com");
    }

    @Test
    void loadUserByUsername_CuandoUsuarioExiste_DebeRetornarUserDetails() {
        // Arrange
        when(usuarioRepository.findByUsername("usuario1")).thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("usuario1");

        // Assert
        assertNotNull(userDetails);
        assertEquals("usuario1", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails instanceof UserDetailsImpl);
        
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        assertEquals(1L, userDetailsImpl.getId());
        assertEquals("usuario1@example.com", userDetailsImpl.getEmail());
        assertEquals("Usuario", userDetailsImpl.getNombre());
        assertEquals("Uno", userDetailsImpl.getApellido());
        
        verify(usuarioRepository, times(1)).findByUsername("usuario1");
    }

    @Test
    void loadUserByUsername_CuandoUsuarioNoExiste_DebeLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByUsername("usuarioInexistente")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("usuarioInexistente");
        });
        
        String expectedMessage = "Usuario no encontrado con username: usuarioInexistente";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
        verify(usuarioRepository, times(1)).findByUsername("usuarioInexistente");
    }
}
