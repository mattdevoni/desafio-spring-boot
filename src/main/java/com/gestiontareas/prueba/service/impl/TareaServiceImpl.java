package com.gestiontareas.prueba.service.impl;

import com.gestiontareas.prueba.dto.TareaDTO;
import com.gestiontareas.prueba.dto.CrearTareaDTO;
import com.gestiontareas.prueba.dto.ActualizarTareaDTO;
import com.gestiontareas.prueba.model.EstadoTarea;
import com.gestiontareas.prueba.model.Tarea;
import com.gestiontareas.prueba.model.Usuario;
import com.gestiontareas.prueba.repository.EstadoTareaRepository;
import com.gestiontareas.prueba.repository.TareaRepository;
import com.gestiontareas.prueba.repository.UsuarioRepository;
import com.gestiontareas.prueba.service.TareaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoTareaRepository estadoTareaRepository;

    public TareaServiceImpl(TareaRepository tareaRepository, 
                           UsuarioRepository usuarioRepository, 
                           EstadoTareaRepository estadoTareaRepository) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoTareaRepository = estadoTareaRepository;
    }

    @Override
    public List<TareaDTO> getAllTareas(Long userId) {
        Usuario usuario = getUsuarioById(userId);
        List<Tarea> tareas = tareaRepository.findByUsuario(usuario);
        return tareas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TareaDTO getTareaById(Long id, Long userId) {
        Tarea tarea = getTareaAndValidateOwnership(id, userId);
        return convertToDTO(tarea);
    }

    @Override
    public TareaDTO createTarea(CrearTareaDTO crearTareaDTO, Long userId) {
        Usuario usuario = getUsuarioById(userId);
        EstadoTarea estadoTarea = getEstadoTareaById(crearTareaDTO.getEstadoId());

        Tarea tarea = new Tarea(
            crearTareaDTO.getTitulo(),
            crearTareaDTO.getDescripcion(),
            crearTareaDTO.getFechaVencimiento(),
            estadoTarea,
            usuario,
            crearTareaDTO.getPrioridad()
        );

        Tarea savedTarea = tareaRepository.save(tarea);
        return convertToDTO(savedTarea);
    }

    @Override
    public TareaDTO updateTarea(Long id, ActualizarTareaDTO actualizarTareaDTO, Long userId) {
        Tarea tareaExistente = getTareaAndValidateOwnership(id, userId);
        EstadoTarea estadoTarea = getEstadoTareaById(actualizarTareaDTO.getEstadoId());

        // Crear una nueva tarea con los datos actualizados y el mismo ID
        Tarea tareaNueva = new Tarea(
            tareaExistente.getId(),
            actualizarTareaDTO.getTitulo(),
            actualizarTareaDTO.getDescripcion(),
            tareaExistente.getFechaCreacion(),
            actualizarTareaDTO.getFechaVencimiento(),
            estadoTarea,
            tareaExistente.getUsuario(),
            actualizarTareaDTO.getPrioridad()
        );

        Tarea updatedTarea = tareaRepository.save(tareaNueva);
        return convertToDTO(updatedTarea);
    }

    @Override
    public void deleteTarea(Long id, Long userId) {
        Tarea tarea = getTareaAndValidateOwnership(id, userId);
        tareaRepository.delete(tarea);
    }

    private TareaDTO convertToDTO(Tarea tarea) {
        return new TareaDTO(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getFechaCreacion(),
                tarea.getFechaVencimiento(),
                tarea.getEstado().getId(),
                tarea.getEstado().getNombre(),
                tarea.getUsuario().getId(),
                tarea.getUsuario().getNombre(),
                tarea.getPrioridad()
        );
    }

    private Usuario getUsuarioById(Long userId) {
        return usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private EstadoTarea getEstadoTareaById(Long estadoId) {
        return estadoTareaRepository.findById(estadoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado de tarea no encontrado"));
    }

    private Tarea getTareaAndValidateOwnership(Long tareaId, Long userId) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada"));

        if (!tarea.getUsuario().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a esta tarea");
        }

        return tarea;
    }
}
