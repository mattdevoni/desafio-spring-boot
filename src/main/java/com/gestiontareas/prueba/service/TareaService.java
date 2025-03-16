package com.gestiontareas.prueba.service;

import com.gestiontareas.prueba.dto.TareaDTO;
import com.gestiontareas.prueba.dto.CrearTareaDTO;
import com.gestiontareas.prueba.dto.ActualizarTareaDTO;
import java.util.List;

/**
 * Interfaz que define las operaciones disponibles para la gestión de tareas.
 */
public interface TareaService {
    
    /**
     * Obtiene todas las tareas del usuario actual.
     * 
     * @param userId ID del usuario actual
     * @return Lista de tareas del usuario
     */
    List<TareaDTO> getAllTareas(Long userId);
    
    /**
     * Obtiene una tarea específica por su ID.
     * 
     * @param id ID de la tarea
     * @param userId ID del usuario actual
     * @return La tarea solicitada
     */
    TareaDTO getTareaById(Long id, Long userId);
    
    /**
     * Crea una nueva tarea.
     * 
     * @param crearTareaDTO Datos de la tarea a crear
     * @param userId ID del usuario que crea la tarea
     * @return La tarea creada
     */
    TareaDTO createTarea(CrearTareaDTO crearTareaDTO, Long userId);
    
    /**
     * Actualiza una tarea existente.
     * 
     * @param id ID de la tarea a actualizar
     * @param actualizarTareaDTO Nuevos datos de la tarea
     * @param userId ID del usuario que actualiza la tarea
     * @return La tarea actualizada
     */
    TareaDTO updateTarea(Long id, ActualizarTareaDTO actualizarTareaDTO, Long userId);
    
    /**
     * Elimina una tarea.
     * 
     * @param id ID de la tarea a eliminar
     * @param userId ID del usuario que elimina la tarea
     */
    void deleteTarea(Long id, Long userId);
}
