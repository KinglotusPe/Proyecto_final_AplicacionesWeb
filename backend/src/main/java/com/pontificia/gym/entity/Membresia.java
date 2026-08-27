package com.pontificia.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Entidad (Modelo): representa la tabla "membresia".
 * Relacion muchos-a-uno con Cliente (un cliente puede tener varias membresias en su historial).
 */
@Entity
@Table(name = "membresia")
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

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoMembresia estado = EstadoMembresia.ACTIVA;

    public Membresia() {
    }

    public Membresia(Long id, Cliente cliente, TipoMembresia tipo, LocalDate fechaInicio, LocalDate fechaVencimiento, EstadoMembresia estado) {
        this.id = id;
        this.cliente = cliente;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
    }

    /**
     * Calcula automaticamente la fecha de vencimiento segun el tipo de membresia
     */
    public LocalDate calcularVencimientoSegunTipo() {
        if (fechaInicio == null) {
            fechaInicio = LocalDate.now();
        }
        if (tipo == null) {
            return fechaInicio.plusMonths(1);
        }
        return switch (tipo) {
            case DIARIO -> fechaInicio.plusDays(1);
            case SEMANAL -> fechaInicio.plusDays(7);
            case MENSUAL -> fechaInicio.plusMonths(1);
            case TRIMESTRAL -> fechaInicio.plusMonths(3);
            case ANUAL -> fechaInicio.plusYears(1);
            case PERSONALIZADA -> fechaVencimiento != null ? fechaVencimiento : fechaInicio.plusMonths(1);
        };
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

    public TipoMembresia getTipo() {
        return tipo;
    }

    public void setTipo(TipoMembresia tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoMembresia getEstado() {
        return estado;
    }

    public void setEstado(EstadoMembresia estado) {
        this.estado = estado;
    }
}
