package com.gestiontareas.prueba.repository;

import com.gestiontareas.prueba.model.EstadoTarea;
import com.gestiontareas.prueba.model.Tarea;
import com.gestiontareas.prueba.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByUsuario(Usuario usuario);
    List<Tarea> findByEstado(EstadoTarea estado);
    List<Tarea> findByUsuarioAndEstado(Usuario usuario, EstadoTarea estado);
    List<Tarea> findByFechaVencimientoBefore(LocalDateTime fecha);
    List<Tarea> findByUsuarioAndFechaVencimientoBefore(Usuario usuario, LocalDateTime fecha);
    List<Tarea> findByPrioridadGreaterThanEqual(Integer prioridad);
}
