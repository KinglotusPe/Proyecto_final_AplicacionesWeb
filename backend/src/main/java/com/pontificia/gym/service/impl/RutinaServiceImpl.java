package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.Ejercicio;
import com.pontificia.gym.entity.Rutina;
import com.pontificia.gym.entity.RutinaDetalle;
import com.pontificia.gym.repository.EjercicioRepository;
import com.pontificia.gym.repository.RutinaDetalleRepository;
import com.pontificia.gym.repository.RutinaRepository;
import com.pontificia.gym.service.RutinaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RutinaServiceImpl implements RutinaService {

    private final RutinaRepository rutinaRepository;
    private final RutinaDetalleRepository rutinaDetalleRepository;
    private final EjercicioRepository ejercicioRepository;

    public RutinaServiceImpl(RutinaRepository rutinaRepository,
                             RutinaDetalleRepository rutinaDetalleRepository,
                             EjercicioRepository ejercicioRepository) {
        this.rutinaRepository = rutinaRepository;
        this.rutinaDetalleRepository = rutinaDetalleRepository;
        this.ejercicioRepository = ejercicioRepository;
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
    public Rutina guardarConEjercicios(Rutina rutina, List<Long> ejercicioIds) {
        Rutina saved = rutinaRepository.save(rutina);

        if (ejercicioIds != null && !ejercicioIds.isEmpty()) {
            // Eliminar detalles previos si era edición
            rutinaDetalleRepository.deleteByRutinaId(saved.getId());

            for (Long ejId : ejercicioIds) {
                ejercicioRepository.findById(ejId).ifPresent(ej -> {
                    RutinaDetalle detalle = new RutinaDetalle(null, saved, ej, 4, 12, 0.0, 60);
                    rutinaDetalleRepository.save(detalle);
                });
            }
        }
        return saved;
    }

    @Override
    public void eliminar(Long id) {
        rutinaRepository.deleteById(id);
    }
}
