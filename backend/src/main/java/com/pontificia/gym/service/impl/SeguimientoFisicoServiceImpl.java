package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.SeguimientoFisico;
import com.pontificia.gym.repository.SeguimientoFisicoRepository;
import com.pontificia.gym.service.SeguimientoFisicoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SeguimientoFisicoServiceImpl implements SeguimientoFisicoService {

    private final SeguimientoFisicoRepository seguimientoFisicoRepository;

    public SeguimientoFisicoServiceImpl(SeguimientoFisicoRepository seguimientoFisicoRepository) {
        this.seguimientoFisicoRepository = seguimientoFisicoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeguimientoFisico> listarTodos() {
        return seguimientoFisicoRepository.findAllByOrderByFechaRegistroDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeguimientoFisico> listarPorCliente(Long clienteId) {
        return seguimientoFisicoRepository.findByClienteIdOrderByFechaRegistroDesc(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SeguimientoFisico> buscarPorId(Long id) {
        return seguimientoFisicoRepository.findById(id);
    }

    @Override
    public SeguimientoFisico guardar(SeguimientoFisico seguimientoFisico) {
        if (seguimientoFisico.getFechaRegistro() == null) {
            seguimientoFisico.setFechaRegistro(LocalDate.now());
        }
        return seguimientoFisicoRepository.save(seguimientoFisico);
    }

    @Override
    public void eliminar(Long id) {
        seguimientoFisicoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarTotal() {
        return seguimientoFisicoRepository.count();
    }
}
