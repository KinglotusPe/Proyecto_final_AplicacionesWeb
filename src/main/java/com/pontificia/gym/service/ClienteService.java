package com.pontificia.gym.service;

import com.pontificia.gym.entity.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> listarTodos();
    Cliente buscarPorId(Long id);
    List<Cliente> buscarPorNombreOApellido(String texto);
    Cliente guardar(Cliente cliente);
    void eliminar(Long id);
    long contarTotal();
}
