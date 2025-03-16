package com.gestiontareas.prueba.service;

import com.gestiontareas.prueba.dto.ActualizarTareaDTO;
import com.gestiontareas.prueba.dto.CrearTareaDTO;
import com.gestiontareas.prueba.dto.TareaDTO;
import com.gestiontareas.prueba.model.EstadoTarea;
import com.gestiontareas.prueba.model.Tarea;
import com.gestiontareas.prueba.model.Usuario;
import com.gestiontareas.prueba.repository.EstadoTareaRepository;
import com.gestiontareas.prueba.repository.TareaRepository;
import com.gestiontareas.prueba.repository.UsuarioRepository;
import com.gestiontareas.prueba.service.impl.TareaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TareaServiceImplTest {

    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EstadoTareaRepository estadoTareaRepository;

    @InjectMocks
    private TareaServiceImpl tareaService;

    private Usuario usuario;
    private EstadoTarea estadoTarea;
    private Tarea tarea;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        usuario = new Usuario(
            1L,
            "usuario1",
            "password123",
            "Nombre",
            "Apellido",
            "usuario1@ejemplo.com"
        );

        estadoTarea = new EstadoTarea(1L, "Pendiente", "Tarea pendiente de realizar");

        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDateTime fechaVencimiento = LocalDateTime.now().plusDays(10);
        
        tarea = new Tarea(
            1L,
            "Preparar presentacion trimestral",
            "Elaborar presentacion con resultados financieros del trimestre",
            fechaCreacion,
            fechaVencimiento,
            estadoTarea,
            usuario,
            3
        );
    }

    @Test
    void getAllTareas_DebeRetornarListaDeTareas() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(tareaRepository.findByUsuario(usuario)).thenReturn(Arrays.asList(tarea));

        // Act
        List<TareaDTO> result = tareaService.getAllTareas(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Preparar presentacion trimestral", result.get(0).getTitulo());
        assertEquals("Pendiente", result.get(0).getEstadoNombre());
        assertEquals(1L, result.get(0).getUsuarioId());
        assertEquals("Nombre", result.get(0).getUsuarioNombre());
        verify(tareaRepository, times(1)).findByUsuario(usuario);
    }

    @Test
    void getTareaById_CuandoTareaExiste_DebeRetornarTarea() {
        // Arrange
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));

        // Act
        TareaDTO result = tareaService.getTareaById(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Preparar presentacion trimestral", result.getTitulo());
        assertEquals("Pendiente", result.getEstadoNombre());
        assertEquals(1L, result.getUsuarioId());
        assertEquals("Nombre", result.getUsuarioNombre());
    }

    @Test
    void getTareaById_CuandoTareaNoExiste_DebeLanzarExcepcion() {
        // Arrange
        when(tareaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            tareaService.getTareaById(99L, 1L);
        });
    }

    @Test
    void getTareaById_CuandoUsuarioNoEsPropietario_DebeLanzarExcepcion() {
        // Arrange
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            tareaService.getTareaById(1L, 2L);
        });
    }

    @Test
    void createTarea_DebeCrearYRetornarNuevaTarea() {
        // Arrange
        CrearTareaDTO nuevaTareaDTO = CrearTareaDTO.builder()
                .titulo("Nueva tarea")
                .descripcion("Descripcion de la nueva tarea")
                .fechaVencimiento(LocalDateTime.now().plusDays(5))
                .prioridad(2)
                .estadoId(1L)
                .build();

        Tarea nuevaTarea = new Tarea(
            2L,
            "Nueva tarea",
            "Descripcion de la nueva tarea",
            LocalDateTime.now(),
            nuevaTareaDTO.getFechaVencimiento(),
            estadoTarea,
            usuario,
            2
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(estadoTareaRepository.findById(1L)).thenReturn(Optional.of(estadoTarea));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(nuevaTarea);

        // Act
        TareaDTO result = tareaService.createTarea(nuevaTareaDTO, 1L);

        // Assert
        assertNotNull(result);
        assertEquals("Nueva tarea", result.getTitulo());
        assertEquals("Descripcion de la nueva tarea", result.getDescripcion());
        assertEquals("Pendiente", result.getEstadoNombre());
        assertEquals(1L, result.getUsuarioId());
        assertEquals("Nombre", result.getUsuarioNombre());
        verify(tareaRepository, times(1)).save(any(Tarea.class));
    }

    @Test
    void updateTarea_CuandoTareaExiste_DebeActualizarYRetornarTarea() {
        // Arrange
        ActualizarTareaDTO tareaActualizadaDTO = ActualizarTareaDTO.builder()
                .titulo("Tarea actualizada")
                .descripcion("Descripcion actualizada")
                .fechaVencimiento(LocalDateTime.now().plusDays(15))
                .prioridad(1)
                .estadoId(1L)
                .build();

        Tarea tareaActualizada = new Tarea(
            1L,
            "Tarea actualizada",
            "Descripcion actualizada",
            tarea.getFechaCreacion(),
            tareaActualizadaDTO.getFechaVencimiento(),
            estadoTarea,
            usuario,
            1
        );

        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        when(estadoTareaRepository.findById(1L)).thenReturn(Optional.of(estadoTarea));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaActualizada);

        // Act
        TareaDTO result = tareaService.updateTarea(1L, tareaActualizadaDTO, 1L);

        // Assert
        assertNotNull(result);
        assertEquals("Tarea actualizada", result.getTitulo());
        assertEquals("Descripcion actualizada", result.getDescripcion());
        assertEquals("Pendiente", result.getEstadoNombre());
        assertEquals(1L, result.getUsuarioId());
        assertEquals("Nombre", result.getUsuarioNombre());
        verify(tareaRepository, times(1)).save(any(Tarea.class));
    }

    @Test
    void deleteTarea_CuandoTareaExiste_DebeEliminarTarea() {
        // Arrange
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        doNothing().when(tareaRepository).delete(tarea);

        // Act
        tareaService.deleteTarea(1L, 1L);

        // Assert
        verify(tareaRepository, times(1)).delete(tarea);
    }
}
