package com.pontificia.gym.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rutina_detalle")
public class RutinaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rutina_id", nullable = false)
    private Rutina rutina;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ejercicio_id", nullable = false)
    private Ejercicio ejercicio;

    @Column(nullable = false)
    private Integer series = 4;

    @Column(nullable = false)
    private Integer repeticiones = 12;

    @Column(name = "peso_sugerido_kg")
    private Double pesoSugeridoKg;

    @Column(name = "descanso_segundos")
    private Integer descansoSegundos = 60;

    public RutinaDetalle() {
    }

    public RutinaDetalle(Long id, Rutina rutina, Ejercicio ejercicio, Integer series, Integer repeticiones, Double pesoSugeridoKg, Integer descansoSegundos) {
        this.id = id;
        this.rutina = rutina;
        this.ejercicio = ejercicio;
        this.series = series;
        this.repeticiones = repeticiones;
        this.pesoSugeridoKg = pesoSugeridoKg;
        this.descansoSegundos = descansoSegundos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(Integer repeticiones) {
        this.repeticiones = repeticiones;
    }

    public Double getPesoSugeridoKg() {
        return pesoSugeridoKg;
    }

    public void setPesoSugeridoKg(Double pesoSugeridoKg) {
        this.pesoSugeridoKg = pesoSugeridoKg;
    }

    public Integer getDescansoSegundos() {
        return descansoSegundos;
    }

    public void setDescansoSegundos(Integer descansoSegundos) {
        this.descansoSegundos = descansoSegundos;
    }
}
