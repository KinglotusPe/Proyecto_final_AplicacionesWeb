package com.pontificia.gym.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CajaTransaccionDto {

    private String tipo; // "SUSCRIPCION" o "TIENDA"
    private String comprobante;
    private LocalDateTime fechaHora;
    private String cliente;
    private String concepto;
    private String atendidoPor;
    private String metodoPago;
    private BigDecimal monto;

    public CajaTransaccionDto() {
    }

    public CajaTransaccionDto(String tipo, String comprobante, LocalDateTime fechaHora, String cliente, String concepto, String atendidoPor, String metodoPago, BigDecimal monto) {
        this.tipo = tipo;
        this.comprobante = comprobante;
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.concepto = concepto;
        this.atendidoPor = atendidoPor;
        this.metodoPago = metodoPago;
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getAtendidoPor() {
        return atendidoPor;
    }

    public void setAtendidoPor(String atendidoPor) {
        this.atendidoPor = atendidoPor;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
