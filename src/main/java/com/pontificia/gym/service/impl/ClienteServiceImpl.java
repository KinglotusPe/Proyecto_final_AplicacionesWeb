package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.Cliente;
import com.pontificia.gym.repository.ClienteRepository;
import com.pontificia.gym.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service: contiene la logica de negocio.
 * El Controller nunca llama directo al Repository, siempre pasa por el Service.
 */
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id " + id));
    }

    @Override
    public List<Cliente> buscarPorNombreOApellido(String texto) {
        if (texto == null || texto.isBlank()) {
            return clienteRepository.findAll();
        }
        return clienteRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(texto, texto);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public long contarTotal() {
        return clienteRepository.count();
    }
}
