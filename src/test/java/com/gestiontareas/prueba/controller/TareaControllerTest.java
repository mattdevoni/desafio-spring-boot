package com.gestiontareas.prueba.controller;

import com.gestiontareas.prueba.dto.TareaDTO;
import com.gestiontareas.prueba.dto.CrearTareaDTO;
import com.gestiontareas.prueba.dto.ActualizarTareaDTO;
import com.gestiontareas.prueba.security.services.UserDetailsImpl;
import com.gestiontareas.prueba.service.TareaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TareaControllerTest {

    @Mock
    private TareaService tareaService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TareaController tareaController;

    private UserDetailsImpl userDetails;
    private TareaDTO tareaDTO;

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

        // Usar constructor en lugar de setters
        LocalDateTime ahora = LocalDateTime.now();
        tareaDTO = new TareaDTO(
                1L,
                "Preparar presentacion trimestral",
                "Elaborar presentacion con resultados financieros del trimestre",
                ahora,
                ahora.plusDays(10),
                1L,
                "Pendiente",
                1L,
                "usuario1",
                3
        );

        // Configurar SecurityContextHolder
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    void getAllTareas_DebeRetornarListaDeTareas() {
        // Arrange
        List<TareaDTO> tareas = Arrays.asList(tareaDTO);
        when(tareaService.getAllTareas(1L)).thenReturn(tareas);

        // Act
        ResponseEntity<List<TareaDTO>> response = tareaController.getAllTareas();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Preparar presentacion trimestral", response.getBody().get(0).getTitulo());
        verify(tareaService, times(1)).getAllTareas(1L);
    }

    @Test
    void getTareaById_CuandoTareaExiste_DebeRetornarTarea() {
        // Arrange
        when(tareaService.getTareaById(1L, 1L)).thenReturn(tareaDTO);

        // Act
        ResponseEntity<TareaDTO> response = tareaController.getTareaById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Preparar presentacion trimestral", response.getBody().getTitulo());
        verify(tareaService, times(1)).getTareaById(1L, 1L);
    }

    @Test
    void createTarea_DebeCrearYRetornarNuevaTarea() {
        // Arrange
        LocalDateTime fechaVencimiento = LocalDateTime.now().plusDays(5);
        CrearTareaDTO crearTareaDTO = CrearTareaDTO.builder()
                .titulo("Nueva tarea")
                .descripcion("Descripcion de la nueva tarea")
                .fechaVencimiento(fechaVencimiento)
                .estadoId(1L)
                .prioridad(2)
                .build();

        TareaDTO nuevaTareaDTO = TareaDTO.builder()
                .id(1L)
                .titulo("Nueva tarea")
                .descripcion("Descripcion de la nueva tarea")
                .fechaCreacion(LocalDateTime.now())
                .fechaVencimiento(fechaVencimiento)
                .estadoId(1L)
                .estadoNombre("Pendiente")
                .usuarioId(1L)
                .usuarioNombre("usuario1")
                .prioridad(2)
                .build();

        when(tareaService.createTarea(crearTareaDTO, 1L)).thenReturn(nuevaTareaDTO);

        // Act
        ResponseEntity<TareaDTO> response = tareaController.createTarea(crearTareaDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Nueva tarea", response.getBody().getTitulo());
        verify(tareaService, times(1)).createTarea(crearTareaDTO, 1L);
    }

    @Test
    void updateTarea_CuandoTareaExiste_DebeActualizarYRetornarTarea() {
        // Arrange
        LocalDateTime fechaVencimiento = LocalDateTime.now().plusDays(15);
        ActualizarTareaDTO actualizarTareaDTO = ActualizarTareaDTO.builder()
                .titulo("Tarea actualizada")
                .descripcion("Descripcion actualizada")
                .fechaVencimiento(fechaVencimiento)
                .estadoId(1L)
                .prioridad(1)
                .build();

        TareaDTO tareaActualizadaDTO = TareaDTO.builder()
                .id(1L)
                .titulo("Tarea actualizada")
                .descripcion("Descripcion actualizada")
                .fechaCreacion(LocalDateTime.now())
                .fechaVencimiento(fechaVencimiento)
                .estadoId(1L)
                .estadoNombre("Pendiente")
                .usuarioId(1L)
                .usuarioNombre("usuario1")
                .prioridad(1)
                .build();

        when(tareaService.updateTarea(1L, actualizarTareaDTO, 1L)).thenReturn(tareaActualizadaDTO);

        // Act
        ResponseEntity<TareaDTO> response = tareaController.updateTarea(1L, actualizarTareaDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tarea actualizada", response.getBody().getTitulo());
        verify(tareaService, times(1)).updateTarea(1L, actualizarTareaDTO, 1L);
    }

    @Test
    void deleteTarea_CuandoTareaExiste_DebeRetornarNoContent() {
        // Arrange
        doNothing().when(tareaService).deleteTarea(1L, 1L);

        // Act
        ResponseEntity<Void> response = tareaController.deleteTarea(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tareaService, times(1)).deleteTarea(1L, 1L);
    }
}
