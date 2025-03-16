package com.gestiontareas.prueba.controller;

import com.gestiontareas.prueba.dto.EstadoTareaDTO;
import com.gestiontareas.prueba.service.EstadoTareaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstadoTareaControllerTest {

    @Mock
    private EstadoTareaService estadoTareaService;

    @InjectMocks
    private EstadoTareaController estadoTareaController;

    private EstadoTareaDTO estadoPendienteDTO;
    private EstadoTareaDTO estadoEnProgresoDTO;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        estadoPendienteDTO = new EstadoTareaDTO(1L, "Pendiente", "Tarea que aun no ha sido iniciada");
        
        estadoEnProgresoDTO = new EstadoTareaDTO(2L, "En Progreso", "Tarea que esta siendo trabajada actualmente");
    }

    @Test
    void getAllEstados_DebeRetornarListaDeEstados() {
        // Arrange
        List<EstadoTareaDTO> estados = Arrays.asList(estadoPendienteDTO, estadoEnProgresoDTO);
        when(estadoTareaService.getAllEstados()).thenReturn(estados);

        // Act
        ResponseEntity<List<EstadoTareaDTO>> response = estadoTareaController.getAllEstados();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Pendiente", response.getBody().get(0).getNombre());
        assertEquals("En Progreso", response.getBody().get(1).getNombre());
        verify(estadoTareaService, times(1)).getAllEstados();
    }

    @Test
    void getEstadoById_CuandoEstadoExiste_DebeRetornarEstado() {
        // Arrange
        when(estadoTareaService.getEstadoById(1L)).thenReturn(estadoPendienteDTO);

        // Act
        ResponseEntity<EstadoTareaDTO> response = estadoTareaController.getEstadoById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Pendiente", response.getBody().getNombre());
        verify(estadoTareaService, times(1)).getEstadoById(1L);
    }
}
