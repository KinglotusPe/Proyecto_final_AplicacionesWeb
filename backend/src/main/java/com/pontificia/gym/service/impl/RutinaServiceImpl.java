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
        List<Rutina> lista = rutinaRepository.findAll();
        lista.forEach(this::poblarDetallesSiVacio);
        return lista;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rutina> listarPorCliente(Long clienteId) {
        List<Rutina> lista = rutinaRepository.findByClienteId(clienteId);
        lista.forEach(this::poblarDetallesSiVacio);
        return lista;
    }

    private void poblarDetallesSiVacio(Rutina r) {
        if (r.getDetalles() == null || r.getDetalles().isEmpty()) {
            List<RutinaDetalle> enDb = rutinaDetalleRepository.findByRutinaId(r.getId());
            if (!enDb.isEmpty()) {
                r.setDetalles(enDb);
            } else {
                // Inferir automáticamente los mejores ejercicios según el día o grupo muscular
                String dia = (r.getDiaSemana() != null ? r.getDiaSemana() : "") + " " + (r.getNombre() != null ? r.getNombre() : "");
                String grupo = "Pecho";
                if (dia.toLowerCase().contains("espalda")) grupo = "Espalda";
                else if (dia.toLowerCase().contains("pierna")) grupo = "Piernas";
                else if (dia.toLowerCase().contains("hombro")) grupo = "Hombros";
                else if (dia.toLowerCase().contains("brazo") || dia.toLowerCase().contains("bíceps") || dia.toLowerCase().contains("tríceps")) grupo = "Brazos";
                else if (dia.toLowerCase().contains("core") || dia.toLowerCase().contains("abdomen")) grupo = "Core";

                List<Ejercicio> ejGrupo = ejercicioRepository.findByGrupoMuscularIgnoreCase(grupo);
                if (!ejGrupo.isEmpty()) {
                    for (int i = 0; i < Math.min(3, ejGrupo.size()); i++) {
                        RutinaDetalle d = new RutinaDetalle(null, r, ejGrupo.get(i), 4, 10, 0.0, 60);
                        r.getDetalles().add(d);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rutina> listarPorEntrenador(Long entrenadorId) {
        return rutinaRepository.findByEntrenadorId(entrenadorId);
    }

    @Override
    @Transactional(readOnly = true)
    public Rutina buscarPorId(Long id) {
        Rutina r = rutinaRepository.findById(id).orElse(null);
        if (r != null) {
            poblarDetallesSiVacio(r);
        }
        return r;
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
