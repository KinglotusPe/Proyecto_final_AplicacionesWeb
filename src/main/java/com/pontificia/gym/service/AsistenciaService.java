package com.pontificia.gym.service;

import com.pontificia.gym.entity.Asistencia;

import java.util.List;

public interface AsistenciaService {
    List<Asistencia> listarTodos();
    List<Asistencia> listarDeHoy();
    List<Asistencia> listarPorCliente(Long clienteId);
    Asistencia registrarEntrada(Long clienteId);
    void eliminar(Long id);
    long contarAsistenciasDeHoy();
}
