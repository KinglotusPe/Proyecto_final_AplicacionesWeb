package com.pontificia.gym.service;

import com.pontificia.gym.entity.Asistencia;

import java.util.List;

public interface AsistenciaService {
    List<Asistencia> listarTodos();
    List<Asistencia> listarDeHoy();
    List<Asistencia> listarHoy();
    List<Asistencia> listarPorCliente(Long clienteId);
    Asistencia registrarEntrada(Long clienteId);
    Asistencia guardar(Asistencia asistencia);
    void eliminar(Long id);
    long contarAsistenciasDeHoy();
    long contarAsistenciasHoy();
}
