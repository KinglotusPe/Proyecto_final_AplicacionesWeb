package com.pontificia.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad (Modelo): representa la tabla "pago".
 */
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debe seleccionar un cliente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotNull(message = "La fecha de pago es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "Debe seleccionar el metodo de pago")
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;

    @Column(name = "proxima_fecha_pago")
    private LocalDate proximaFechaPago;

    public Pago() {
    }

    public Pago(Long id, Cliente cliente, BigDecimal monto, LocalDate fecha, MetodoPago metodoPago, LocalDate proximaFechaPago) {
        this.id = id;
        this.cliente = cliente;
        this.monto = monto;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.proximaFechaPago = proximaFechaPago;
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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDate getProximaFechaPago() {
        return proximaFechaPago;
    }

    public void setProximaFechaPago(LocalDate proximaFechaPago) {
        this.proximaFechaPago = proximaFechaPago;
    }
}
