package com.pontificia.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad (Modelo): representa la tabla "membresia".
 * Relacion muchos-a-uno con Cliente (un cliente puede tener varias membresias en su historial).
 */
@Entity
@Table(name = "membresia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debe seleccionar un cliente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Debe seleccionar el tipo de membresia")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMembresia tipo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoMembresia estado = EstadoMembresia.ACTIVA;
}
