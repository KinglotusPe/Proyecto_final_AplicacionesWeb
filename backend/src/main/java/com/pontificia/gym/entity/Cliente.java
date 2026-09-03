package com.pontificia.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * Entidad (Modelo): representa la tabla "cliente".
 * Solo contiene datos, sin logica de negocio (segun el patron MVC visto en clase).
 */
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El DNI es obligatorio")
    @Column(nullable = false, unique = true, length = 15)
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false, length = 100)
    private String apellidos;

    @Positive(message = "La edad debe ser un numero positivo")
    private Integer edad;

    @Column(length = 20)
    private String telefono;

    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    public Cliente() {
    }

    public Cliente(Long id, String dni, String nombres, String apellidos, Integer edad, String telefono, LocalDate fechaInscripcion) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.telefono = telefono;
        this.fechaInscripcion = fechaInscripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getFotoUrlOrDefault() {
        if (fotoUrl != null && !fotoUrl.trim().isEmpty()) {
            return fotoUrl;
        }
        return "https://ui-avatars.com/api/?name=" + (nombres != null ? nombres.replace(" ", "+") : "Socio") + "+" + (apellidos != null ? apellidos.replace(" ", "+") : "") + "&background=dc3545&color=fff&size=200&bold=true";
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
