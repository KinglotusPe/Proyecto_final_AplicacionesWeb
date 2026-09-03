package com.pontificia.gym.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva_clase", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"clase_id", "cliente_id"})
})
public class ReservaClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clase_id", nullable = false)
    private ClaseGrupal clase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Column(nullable = false, length = 30)
    private String estado = "CONFIRMADA"; // CONFIRMADA, ASISTIO, CANCELADA

    public ReservaClase() {
    }

    public ReservaClase(Long id, ClaseGrupal clase, Cliente cliente, LocalDateTime fechaReserva, String estado) {
        this.id = id;
        this.clase = clase;
        this.cliente = cliente;
        this.fechaReserva = fechaReserva;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClaseGrupal getClase() {
        return clase;
    }

    public void setClase(ClaseGrupal clase) {
        this.clase = clase;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
