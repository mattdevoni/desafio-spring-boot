package com.gestiontareas.prueba.service.impl;

import com.gestiontareas.prueba.dto.EstadoTareaDTO;
import com.gestiontareas.prueba.model.EstadoTarea;
import com.gestiontareas.prueba.repository.EstadoTareaRepository;
import com.gestiontareas.prueba.service.EstadoTareaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoTareaServiceImpl implements EstadoTareaService {

    private final EstadoTareaRepository estadoTareaRepository;

    public EstadoTareaServiceImpl(EstadoTareaRepository estadoTareaRepository) {
        this.estadoTareaRepository = estadoTareaRepository;
    }

    @Override
    public List<EstadoTareaDTO> getAllEstados() {
        List<EstadoTarea> estados = estadoTareaRepository.findAll();
        return estados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EstadoTareaDTO getEstadoById(Long id) {
        EstadoTarea estadoTarea = estadoTareaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado de tarea no encontrado"));
        return convertToDTO(estadoTarea);
    }

    /**
     * Convierte una entidad EstadoTarea a un DTO.
     */
    private EstadoTareaDTO convertToDTO(EstadoTarea estadoTarea) {
        return new EstadoTareaDTO(
                estadoTarea.getId(),
                estadoTarea.getNombre(),
                estadoTarea.getDescripcion()
        );
    }
}
