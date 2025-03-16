package com.gestiontareas.prueba.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
@Getter
@NoArgsConstructor
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaVencimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoTarea estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column
    private Integer prioridad;

    public Tarea(String titulo, String descripcion, LocalDateTime fechaVencimiento, 
                EstadoTarea estado, Usuario usuario, Integer prioridad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.usuario = usuario;
        this.prioridad = prioridad;
        this.fechaCreacion = LocalDateTime.now();
    }

    /**
     * Constructor para pruebas que permite establecer el ID
     * @param id ID de la tarea
     * @param titulo Título de la tarea
     * @param descripcion Descripción de la tarea
     * @param fechaCreacion Fecha de creación
     * @param fechaVencimiento Fecha de vencimiento
     * @param estado Estado de la tarea
     * @param usuario Usuario asignado
     * @param prioridad Prioridad de la tarea
     */
    public Tarea(Long id, String titulo, String descripcion, LocalDateTime fechaCreacion,
                LocalDateTime fechaVencimiento, EstadoTarea estado, Usuario usuario, Integer prioridad) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.usuario = usuario;
        this.prioridad = prioridad;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
    
    // Métodos para mantener la relación bidireccional
    // Estos métodos son necesarios para que las relaciones funcionen correctamente
    void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }
}
