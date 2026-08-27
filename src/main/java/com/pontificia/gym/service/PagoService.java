package com.pontificia.gym.service;

import com.pontificia.gym.entity.Pago;

import java.math.BigDecimal;
import java.util.List;

public interface PagoService {
    List<Pago> listarTodos();
    Pago buscarPorId(Long id);
    List<Pago> listarPorCliente(Long clienteId);
    Pago guardar(Pago pago);
    void eliminar(Long id);
    BigDecimal totalRecaudadoDelMes();
}
