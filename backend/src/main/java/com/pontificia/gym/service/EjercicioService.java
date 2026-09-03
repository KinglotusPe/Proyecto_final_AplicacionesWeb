package com.pontificia.gym.service;

import com.pontificia.gym.entity.Ejercicio;

import java.util.List;
import java.util.Optional;

public interface EjercicioService {
    List<Ejercicio> listarTodos();
    Optional<Ejercicio> buscarPorId(Long id);
    List<Ejercicio> buscarPorGrupoMuscular(String grupo);
    List<Ejercicio> buscarPorTexto(String query);
    Ejercicio guardar(Ejercicio ejercicio);
    void eliminar(Long id);
    void inicializarDatasetEjercicios();
}
