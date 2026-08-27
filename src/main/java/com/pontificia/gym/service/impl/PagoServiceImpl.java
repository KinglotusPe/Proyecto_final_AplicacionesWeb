package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.Pago;
import com.pontificia.gym.repository.PagoRepository;
import com.pontificia.gym.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    @Override
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    @Override
    public Pago buscarPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado con id " + id));
    }

    @Override
    public List<Pago> listarPorCliente(Long clienteId) {
        return pagoRepository.findByClienteId(clienteId);
    }

    @Override
    public Pago guardar(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Override
    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }

    @Override
    public BigDecimal totalRecaudadoDelMes() {
        YearMonth mesActual = YearMonth.now();
        LocalDate desde = mesActual.atDay(1);
        LocalDate hasta = mesActual.atEndOfMonth();
        return pagoRepository.totalRecaudadoEntre(desde, hasta);
    }
}
