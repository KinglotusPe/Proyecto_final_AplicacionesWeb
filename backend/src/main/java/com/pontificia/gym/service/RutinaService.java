package com.pontificia.gym.service;

import com.pontificia.gym.entity.Rutina;
import java.util.List;

public interface RutinaService {
    List<Rutina> listarTodas();
    List<Rutina> listarPorCliente(Long clienteId);
    List<Rutina> listarPorEntrenador(Long entrenadorId);
    Rutina buscarPorId(Long id);
    Rutina guardar(Rutina rutina);
    Rutina guardarConEjercicios(Rutina rutina, List<Long> ejercicioIds);
    void eliminar(Long id);
}
