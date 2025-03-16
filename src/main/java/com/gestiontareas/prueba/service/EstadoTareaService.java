package com.gestiontareas.prueba.service;

import com.gestiontareas.prueba.dto.EstadoTareaDTO;
import java.util.List;

/**
 * Interfaz que define las operaciones disponibles para la gestión de estados de tarea.
 */
public interface EstadoTareaService {
    
    /**
     * Obtiene todos los estados de tarea disponibles.
     * 
     * @return Lista de estados de tarea
     */
    List<EstadoTareaDTO> getAllEstados();
    
    /**
     * Obtiene un estado de tarea por su ID.
     * 
     * @param id ID del estado de tarea
     * @return El estado de tarea solicitado
     */
    EstadoTareaDTO getEstadoById(Long id);
}
