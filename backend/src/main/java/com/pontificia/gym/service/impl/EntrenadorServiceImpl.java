package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.Entrenador;
import com.pontificia.gym.repository.EntrenadorRepository;
import com.pontificia.gym.service.EntrenadorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EntrenadorServiceImpl implements EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;

    public EntrenadorServiceImpl(EntrenadorRepository entrenadorRepository) {
        this.entrenadorRepository = entrenadorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entrenador> listarTodos() {
        return entrenadorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entrenador> listarActivos() {
        return entrenadorRepository.findByEstado("ACTIVO");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Entrenador> buscarPorId(Long id) {
        return entrenadorRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Entrenador> buscarPorDni(String dni) {
        return entrenadorRepository.findByDni(dni);
    }

    @Override
    public Entrenador guardar(Entrenador entrenador) {
        return entrenadorRepository.save(entrenador);
    }

    @Override
    public void eliminar(Long id) {
        entrenadorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarActivos() {
        return entrenadorRepository.countByEstado("ACTIVO");
    }

    @Override
    @Transactional(readOnly = true)
    public long contarTotal() {
        return entrenadorRepository.count();
    }
}
