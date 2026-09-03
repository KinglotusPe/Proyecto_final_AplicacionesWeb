package com.pontificia.gym.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CajaResumenDto {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String cajeroFiltro;

    private BigDecimal totalGeneral = BigDecimal.ZERO;
    private BigDecimal totalSuscripciones = BigDecimal.ZERO;
    private BigDecimal totalTienda = BigDecimal.ZERO;

    private BigDecimal totalEfectivo = BigDecimal.ZERO;
    private BigDecimal totalYapePlin = BigDecimal.ZERO;
    private BigDecimal totalTarjeta = BigDecimal.ZERO;
    private BigDecimal totalTransferencia = BigDecimal.ZERO;

    private int cantidadTransacciones = 0;

    private List<CajaTransaccionDto> transacciones = new ArrayList<>();

    public CajaResumenDto() {
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCajeroFiltro() {
        return cajeroFiltro;
    }

    public void setCajeroFiltro(String cajeroFiltro) {
        this.cajeroFiltro = cajeroFiltro;
    }

    public BigDecimal getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(BigDecimal totalGeneral) {
        this.totalGeneral = totalGeneral;
    }

    public BigDecimal getTotalSuscripciones() {
        return totalSuscripciones;
    }

    public void setTotalSuscripciones(BigDecimal totalSuscripciones) {
        this.totalSuscripciones = totalSuscripciones;
    }

    public BigDecimal getTotalTienda() {
        return totalTienda;
    }

    public void setTotalTienda(BigDecimal totalTienda) {
        this.totalTienda = totalTienda;
    }

    public BigDecimal getTotalEfectivo() {
        return totalEfectivo;
    }

    public void setTotalEfectivo(BigDecimal totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public BigDecimal getTotalYapePlin() {
        return totalYapePlin;
    }

    public void setTotalYapePlin(BigDecimal totalYapePlin) {
        this.totalYapePlin = totalYapePlin;
    }

    public BigDecimal getTotalTarjeta() {
        return totalTarjeta;
    }

    public void setTotalTarjeta(BigDecimal totalTarjeta) {
        this.totalTarjeta = totalTarjeta;
    }

    public BigDecimal getTotalTransferencia() {
        return totalTransferencia;
    }

    public void setTotalTransferencia(BigDecimal totalTransferencia) {
        this.totalTransferencia = totalTransferencia;
    }

    public int getCantidadTransacciones() {
        return cantidadTransacciones;
    }

    public void setCantidadTransacciones(int cantidadTransacciones) {
        this.cantidadTransacciones = cantidadTransacciones;
    }

    public List<CajaTransaccionDto> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<CajaTransaccionDto> transacciones) {
        this.transacciones = transacciones;
    }
}
