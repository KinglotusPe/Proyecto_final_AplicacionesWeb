package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.EstadoMembresia;
import com.pontificia.gym.entity.Membresia;
import com.pontificia.gym.repository.MembresiaRepository;
import com.pontificia.gym.service.MembresiaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MembresiaServiceImpl implements MembresiaService {

    private final MembresiaRepository membresiaRepository;

    public MembresiaServiceImpl(MembresiaRepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Membresia> listarTodos() {
        return membresiaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Membresia buscarPorId(Long id) {
        return membresiaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Membresia> listarPorCliente(Long clienteId) {
        return membresiaRepository.findByClienteIdOrderByFechaInicioDesc(clienteId);
    }

    @Override
    public Membresia guardar(Membresia membresia) {
        // Regla de negocio: si no se asigno fecha de inicio, usar hoy
        if (membresia.getFechaInicio() == null) {
            membresia.setFechaInicio(LocalDate.now());
        }

        // Si no se asigno fecha de vencimiento manual, calcular segun el tipo
        if (membresia.getFechaVencimiento() == null && membresia.getTipo() != null) {
            membresia.setFechaVencimiento(membresia.getTipo().calcularVencimiento(membresia.getFechaInicio()));
        }

        // Actualizar estado segun la fecha de vencimiento
        if (membresia.getFechaVencimiento() != null) {
            if (membresia.getFechaVencimiento().isBefore(LocalDate.now())) {
                membresia.setEstado(EstadoMembresia.VENCIDA);
            } else {
                membresia.setEstado(EstadoMembresia.ACTIVA);
            }
        }

        return membresiaRepository.save(membresia);
    }

    @Override
    public void eliminar(Long id) {
        membresiaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarActivas() {
        return membresiaRepository.countByEstado(EstadoMembresia.ACTIVA);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarVencidas() {
        return membresiaRepository.countByEstado(EstadoMembresia.VENCIDA);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Membresia> listarPorVencerEnDias(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);
        return membresiaRepository.findMembresiasPorVencer(hoy, limite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Membresia> listarProximasAVencer() {
        return listarPorVencerEnDias(7);
    }

    @Override
    public void actualizarEstados() {
        List<Membresia> activas = membresiaRepository.findByEstado(EstadoMembresia.ACTIVA);
        LocalDate hoy = LocalDate.now();
        for (Membresia m : activas) {
            if (m.getFechaVencimiento() != null && m.getFechaVencimiento().isBefore(hoy)) {
                m.setEstado(EstadoMembresia.VENCIDA);
                membresiaRepository.save(m);
            }
        }
    }
}
