package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.Pago;
import com.pontificia.gym.repository.PagoRepository;
import com.pontificia.gym.service.PagoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

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
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());
        BigDecimal total = pagoRepository.totalRecaudadoEntre(inicioMes, finMes);
        return total != null ? total : BigDecimal.ZERO;
    }
}
