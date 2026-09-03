package com.pontificia.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "rutina")
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debe asignar un cliente")
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "entrenador_id")
    private Entrenador entrenador;

    @NotBlank(message = "El nombre de la rutina es obligatorio")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El día o fase es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String diaSemana; // Ej: Lunes - Pecho y Tríceps

    @NotBlank(message = "El detalle de ejercicios es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String ejercicios; // Ej: Press Banca 4x10, Aperturas 3x12...

    @Size(max = 50)
    @Column(length = 50)
    private String nivel = "Intermedio"; // Principiante, Intermedio, Avanzado

    @Size(max = 255)
    @Column(length = 255)
    private String observaciones;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private java.util.List<RutinaDetalle> detalles = new java.util.ArrayList<>();

    public Rutina() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(String ejercicios) {
        this.ejercicios = ejercicios;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public java.util.List<RutinaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(java.util.List<RutinaDetalle> detalles) {
        this.detalles = detalles;
    }
}
