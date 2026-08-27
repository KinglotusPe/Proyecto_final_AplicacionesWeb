package com.pontificia.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "seguimiento_fisico")
public class SeguimientoFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "entrenador_id")
    private Entrenador entrenador;

    @NotNull(message = "La fecha de registro es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @NotNull(message = "El peso es obligatorio")
    @Column(name = "peso_kg", nullable = false)
    private Double pesoKg;

    @Column(name = "altura_cm")
    private Double alturaCm;

    @Column(name = "porcentaje_grasa")
    private Double porcentajeGrasa;

    @Column(name = "masa_muscular")
    private Double masaMuscular;

    @Column(length = 100)
    private String objetivo;

    @Column(length = 255)
    private String observaciones;

    public SeguimientoFisico() {
    }

    public SeguimientoFisico(Long id, Cliente cliente, Entrenador entrenador, LocalDate fechaRegistro, Double pesoKg, Double alturaCm, Double porcentajeGrasa, Double masaMuscular, String objetivo, String observaciones) {
        this.id = id;
        this.cliente = cliente;
        this.entrenador = entrenador;
        this.fechaRegistro = fechaRegistro;
        this.pesoKg = pesoKg;
        this.alturaCm = alturaCm;
        this.porcentajeGrasa = porcentajeGrasa;
        this.masaMuscular = masaMuscular;
        this.objetivo = objetivo;
        this.observaciones = observaciones;
    }

    /**
     * Calcula el Índice de Masa Corporal (IMC) si la altura está disponible
     */
    public Double getImc() {
        if (pesoKg != null && alturaCm != null && alturaCm > 0) {
            double alturaM = alturaCm / 100.0;
            return Math.round((pesoKg / (alturaM * alturaM)) * 10.0) / 10.0;
        }
        return null;
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

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(Double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public Double getAlturaCm() {
        return alturaCm;
    }

    public void setAlturaCm(Double alturaCm) {
        this.alturaCm = alturaCm;
    }

    public Double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }

    public void setPorcentajeGrasa(Double porcentajeGrasa) {
        this.porcentajeGrasa = porcentajeGrasa;
    }

    public Double getMasaMuscular() {
        return masaMuscular;
    }

    public void setMasaMuscular(Double masaMuscular) {
        this.masaMuscular = masaMuscular;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
