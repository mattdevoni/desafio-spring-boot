package com.gestiontareas.prueba.security.services;

import com.gestiontareas.prueba.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void testBuild() {
        // Arrange
        Usuario usuario = new Usuario(1L, "usuario1", "password123", "Usuario", "Uno", "usuario1@example.com");

        // Act
        UserDetailsImpl userDetails = UserDetailsImpl.build(usuario);

        // Assert
        assertNotNull(userDetails);
        assertEquals(1L, userDetails.getId());
        assertEquals("usuario1", userDetails.getUsername());
        assertEquals("usuario1@example.com", userDetails.getEmail());
        assertEquals("Usuario", userDetails.getNombre());
        assertEquals("Uno", userDetails.getApellido());
        assertEquals("password123", userDetails.getPassword());
        
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testUserAccountMethods() {
        // Arrange
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L, "usuario1", "usuario1@example.com", "Usuario", "Uno", "password123", authorities);

        // Act & Assert
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetailsImpl userDetails1 = new UserDetailsImpl(
                1L, "usuario1", "usuario1@example.com", "Usuario", "Uno", "password123", authorities);
        UserDetailsImpl userDetails2 = new UserDetailsImpl(
                1L, "usuario1_diferente", "otro@example.com", "Otro", "Usuario", "otraclave", authorities);
        UserDetailsImpl userDetails3 = new UserDetailsImpl(
                2L, "usuario1", "usuario1@example.com", "Usuario", "Uno", "password123", authorities);

        // Act & Assert - equals
        assertEquals(userDetails1, userDetails1); // Mismo objeto
        assertEquals(userDetails1, userDetails2); // Mismo ID
        assertNotEquals(userDetails1, userDetails3); // Diferente ID
        assertNotEquals(userDetails1, null); // Comparación con null
        assertNotEquals(userDetails1, new Object()); // Diferente tipo

        // Act & Assert - hashCode
        assertEquals(userDetails1.hashCode(), userDetails2.hashCode()); // Mismo ID, mismo hashCode
        assertNotEquals(userDetails1.hashCode(), userDetails3.hashCode()); // Diferente ID, diferente hashCode
    }
}
