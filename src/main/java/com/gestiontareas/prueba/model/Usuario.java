package com.gestiontareas.prueba.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@ToString(exclude = "tareas")
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Tarea> tareas = new ArrayList<>();
    
    public Usuario(String username, String password, String nombre, String apellido, String email) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }
    
    /**
     * Constructor para pruebas que permite establecer el ID
     * @param id ID del usuario
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param nombre Nombre
     * @param apellido Apellido
     * @param email Email
     */
    public Usuario(Long id, String username, String password, String nombre, String apellido, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }
    
    // Métodos para gestionar la relación bidireccional
    public void addTarea(Tarea tarea) {
        tareas.add(tarea);
        tarea.setUsuario(this);
    }
    
    public void removeTarea(Tarea tarea) {
        tareas.remove(tarea);
        tarea.setUsuario(null);
    }
}
