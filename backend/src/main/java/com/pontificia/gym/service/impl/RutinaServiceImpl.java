package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.Rutina;
import com.pontificia.gym.repository.RutinaRepository;
import com.pontificia.gym.service.RutinaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RutinaServiceImpl implements RutinaService {

    private final RutinaRepository rutinaRepository;

    public RutinaServiceImpl(RutinaRepository rutinaRepository) {
        this.rutinaRepository = rutinaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rutina> listarTodas() {
        return rutinaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rutina> listarPorCliente(Long clienteId) {
        return rutinaRepository.findByClienteId(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rutina> listarPorEntrenador(Long entrenadorId) {
        return rutinaRepository.findByEntrenadorId(entrenadorId);
    }

    @Override
    @Transactional(readOnly = true)
    public Rutina buscarPorId(Long id) {
        return rutinaRepository.findById(id).orElse(null);
    }

    @Override
    public Rutina guardar(Rutina rutina) {
        return rutinaRepository.save(rutina);
    }

    @Override
    public void eliminar(Long id) {
        rutinaRepository.deleteById(id);
    }
}
