package com.pontificia.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad (Modelo): representa la tabla "cliente".
 * Solo contiene datos, sin logica de negocio (segun el patron MVC visto en clase).
 */
@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
