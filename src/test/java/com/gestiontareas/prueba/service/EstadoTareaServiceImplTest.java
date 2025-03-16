package com.gestiontareas.prueba.service;

import com.gestiontareas.prueba.dto.EstadoTareaDTO;
import com.gestiontareas.prueba.model.EstadoTarea;
import com.gestiontareas.prueba.repository.EstadoTareaRepository;
import com.gestiontareas.prueba.service.impl.EstadoTareaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstadoTareaServiceImplTest {

    @Mock
    private EstadoTareaRepository estadoTareaRepository;

    @InjectMocks
    private EstadoTareaServiceImpl estadoTareaService;

    private EstadoTarea estadoPendiente;
    private EstadoTarea estadoEnProgreso;
    private EstadoTareaDTO estadoPendienteDTO;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        estadoPendiente = new EstadoTarea(1L, "Pendiente", "Tarea que aun no ha sido iniciada");
        estadoEnProgreso = new EstadoTarea(2L, "En Progreso", "Tarea que esta siendo trabajada actualmente");

        // Usar el constructor con parámetros en lugar de setters
        estadoPendienteDTO = new EstadoTareaDTO(1L, "Pendiente", "Tarea que aun no ha sido iniciada");
    }

    @Test
    void getAllEstados_DebeRetornarListaDeEstados() {
        // Arrange
        when(estadoTareaRepository.findAll()).thenReturn(Arrays.asList(estadoPendiente, estadoEnProgreso));

        // Act
        List<EstadoTareaDTO> result = estadoTareaService.getAllEstados();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pendiente", result.get(0).getNombre());
        assertEquals("En Progreso", result.get(1).getNombre());
        verify(estadoTareaRepository, times(1)).findAll();
    }

    @Test
    void getEstadoById_CuandoEstadoExiste_DebeRetornarEstado() {
        // Arrange
        when(estadoTareaRepository.findById(1L)).thenReturn(Optional.of(estadoPendiente));

        // Act
        EstadoTareaDTO result = estadoTareaService.getEstadoById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(estadoPendienteDTO.getId(), result.getId());
        assertEquals(estadoPendienteDTO.getNombre(), result.getNombre());
        assertEquals(estadoPendienteDTO.getDescripcion(), result.getDescripcion());
        verify(estadoTareaRepository, times(1)).findById(1L);
    }

    @Test
    void getEstadoById_CuandoEstadoNoExiste_DebeLanzarExcepcion() {
        // Arrange
        when(estadoTareaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            estadoTareaService.getEstadoById(99L);
        });
        verify(estadoTareaRepository, times(1)).findById(99L);
    }
}
