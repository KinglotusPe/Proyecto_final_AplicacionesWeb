package com.pontificia.gym.service;

import com.pontificia.gym.entity.Entrenador;

import java.util.List;
import java.util.Optional;

public interface EntrenadorService {
    List<Entrenador> listarTodos();
    List<Entrenador> listarActivos();
    Optional<Entrenador> buscarPorId(Long id);
    Optional<Entrenador> buscarPorDni(String dni);
    Entrenador guardar(Entrenador entrenador);
    void eliminar(Long id);
    long contarActivos();
    long contarTotal();
}
