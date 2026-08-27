package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.EstadoMembresia;
import com.pontificia.gym.entity.Membresia;
import com.pontificia.gym.repository.MembresiaRepository;
import com.pontificia.gym.service.MembresiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembresiaServiceImpl implements MembresiaService {

    private final MembresiaRepository membresiaRepository;

    @Override
    public List<Membresia> listarTodos() {
        actualizarEstados();
        return membresiaRepository.findAll();
    }

    @Override
    public Membresia buscarPorId(Long id) {
        return membresiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membresia no encontrada con id " + id));
    }

    @Override
    public List<Membresia> listarPorCliente(Long clienteId) {
        return membresiaRepository.findByClienteId(clienteId);
    }

    @Override
    public Membresia guardar(Membresia membresia) {
        // Regla de negocio: si la fecha de vencimiento ya paso, se guarda como VENCIDA
        if (membresia.getFechaVencimiento() != null
                && membresia.getFechaVencimiento().isBefore(LocalDate.now())) {
            membresia.setEstado(EstadoMembresia.VENCIDA);
        } else {
            membresia.setEstado(EstadoMembresia.ACTIVA);
        }
        return membresiaRepository.save(membresia);
    }

    @Override
    public void eliminar(Long id) {
        membresiaRepository.deleteById(id);
    }

    @Override
    public long contarActivas() {
        actualizarEstados();
        return membresiaRepository.countByEstado(EstadoMembresia.ACTIVA);
    }

    @Override
    public long contarVencidas() {
        actualizarEstados();
        return membresiaRepository.countByEstado(EstadoMembresia.VENCIDA);
    }

    @Override
    public List<Membresia> listarPorVencerEnDias(int dias) {
        LocalDate hoy = LocalDate.now();
        return membresiaRepository.findMembresiasPorVencer(hoy, hoy.plusDays(dias));
    }

    @Override
    public void actualizarEstados() {
        LocalDate hoy = LocalDate.now();
        List<Membresia> activas = membresiaRepository.findByEstado(EstadoMembresia.ACTIVA);
        for (Membresia m : activas) {
            if (m.getFechaVencimiento().isBefore(hoy)) {
                m.setEstado(EstadoMembresia.VENCIDA);
                membresiaRepository.save(m);
            }
        }
    }
}
