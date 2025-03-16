package com.gestiontareas.prueba.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estados_tarea")
@Getter
@ToString(exclude = "tareas")
@NoArgsConstructor
public class EstadoTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column
    private String descripcion;

    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY)
    private final List<Tarea> tareas = new ArrayList<>();
    
    public EstadoTarea(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    /**
     * Constructor para pruebas que permite establecer el ID
     * @param id ID del estado de tarea
     * @param nombre Nombre del estado
     * @param descripcion Descripción del estado
     */
    public EstadoTarea(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // Métodos para gestionar la relación bidireccional
    public void addTarea(Tarea tarea) {
        tareas.add(tarea);
        tarea.setEstado(this);
    }
    
    public void removeTarea(Tarea tarea) {
        tareas.remove(tarea);
        tarea.setEstado(null);
    }
}
