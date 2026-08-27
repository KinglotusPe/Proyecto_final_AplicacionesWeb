package com.pontificia.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad (Modelo): representa la tabla "asistencia".
 * Cada registro es una entrada del cliente al gimnasio.
 */
@Entity
@Table(name = "asistencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debe seleccionar un cliente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @PrePersist
    public void prePersist() {
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }
}
