package com.pontificia.gym.service;

import com.pontificia.gym.entity.Membresia;

import java.util.List;

public interface MembresiaService {
    List<Membresia> listarTodos();
    Membresia buscarPorId(Long id);
    List<Membresia> listarPorCliente(Long clienteId);
    Membresia guardar(Membresia membresia);
    void eliminar(Long id);

    long contarActivas();
    long contarVencidas();
    List<Membresia> listarPorVencerEnDias(int dias);

    /** Recorre todas las membresias activas y actualiza su estado segun la fecha actual. */
    void actualizarEstados();
}
