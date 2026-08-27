package com.pontificia.gym.service;

import com.pontificia.gym.entity.SeguimientoFisico;

import java.util.List;
import java.util.Optional;

public interface SeguimientoFisicoService {
    List<SeguimientoFisico> listarTodos();
    List<SeguimientoFisico> listarPorCliente(Long clienteId);
    Optional<SeguimientoFisico> buscarPorId(Long id);
    SeguimientoFisico guardar(SeguimientoFisico seguimientoFisico);
    void eliminar(Long id);
    long contarTotal();
}
