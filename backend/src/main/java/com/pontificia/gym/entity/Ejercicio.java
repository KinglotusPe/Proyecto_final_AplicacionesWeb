package com.pontificia.gym.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ejercicio")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(name = "grupo_muscular", nullable = false, length = 50)
    private String grupoMuscular;

    @Column(length = 60)
    private String equipamiento;

    @Column(length = 30)
    private String nivel;

    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "gif_url", length = 500)
    private String gifUrl;

    @Column(length = 50)
    private String categoria;

    public Ejercicio() {
    }

    public Ejercicio(Long id, String nombre, String grupoMuscular, String equipamiento, String nivel, String instrucciones, String imagenUrl, String gifUrl, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.equipamiento = equipamiento;
        this.nivel = nivel;
        this.instrucciones = instrucciones;
        this.imagenUrl = imagenUrl;
        this.gifUrl = gifUrl;
        this.categoria = categoria;
    }

    public static EjercicioBuilder builder() {
        return new EjercicioBuilder();
    }

    public static class EjercicioBuilder {
        private Long id;
        private String nombre;
        private String grupoMuscular;
        private String equipamiento;
        private String nivel;
        private String instrucciones;
        private String imagenUrl;
        private String gifUrl;
        private String categoria;

        public EjercicioBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EjercicioBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public EjercicioBuilder grupoMuscular(String grupoMuscular) {
            this.grupoMuscular = grupoMuscular;
            return this;
        }

        public EjercicioBuilder equipamiento(String equipamiento) {
            this.equipamiento = equipamiento;
            return this;
        }

        public EjercicioBuilder nivel(String nivel) {
            this.nivel = nivel;
            return this;
        }

        public EjercicioBuilder instrucciones(String instrucciones) {
            this.instrucciones = instrucciones;
            return this;
        }

        public EjercicioBuilder imagenUrl(String imagenUrl) {
            this.imagenUrl = imagenUrl;
            return this;
        }

        public EjercicioBuilder gifUrl(String gifUrl) {
            this.gifUrl = gifUrl;
            return this;
        }

        public EjercicioBuilder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public Ejercicio build() {
            return new Ejercicio(id, nombre, grupoMuscular, equipamiento, nivel, instrucciones, imagenUrl, gifUrl, categoria);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(String equipamiento) {
        this.equipamiento = equipamiento;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
