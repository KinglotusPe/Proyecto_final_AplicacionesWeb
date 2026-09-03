package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.*;
import com.pontificia.gym.repository.*;
import com.pontificia.gym.service.ClaseGrupalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClaseGrupalServiceImpl implements ClaseGrupalService {

    private final ClaseGrupalRepository claseGrupalRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ReservaClaseRepository reservaClaseRepository;
    private final ClienteRepository clienteRepository;
    private final EntrenadorRepository entrenadorRepository;

    public ClaseGrupalServiceImpl(ClaseGrupalRepository claseGrupalRepository,
                                  DisciplinaRepository disciplinaRepository,
                                  ReservaClaseRepository reservaClaseRepository,
                                  ClienteRepository clienteRepository,
                                  EntrenadorRepository entrenadorRepository) {
        this.claseGrupalRepository = claseGrupalRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.reservaClaseRepository = reservaClaseRepository;
        this.clienteRepository = clienteRepository;
        this.entrenadorRepository = entrenadorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaseGrupal> listarTodas() {
        return claseGrupalRepository.listarTodasOrdenadas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaseGrupal> listarProximas() {
        return claseGrupalRepository.findByFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClaseGrupal> buscarPorId(Long id) {
        return claseGrupalRepository.findById(id);
    }

    @Override
    public ClaseGrupal guardar(ClaseGrupal clase) {
        return claseGrupalRepository.save(clase);
    }

    @Override
    public void eliminar(Long id) {
        claseGrupalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disciplina> listarDisciplinas() {
        return disciplinaRepository.findAll();
    }

    @Override
    public Disciplina guardarDisciplina(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    @Override
    public ReservaClase reservarCupo(Long claseId, Long clienteId) {
        ClaseGrupal clase = claseGrupalRepository.findById(claseId)
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        if (clase.isLleno()) {
            throw new IllegalStateException("Lo sentimos, no quedan cupos disponibles para esta clase.");
        }

        Optional<ReservaClase> existente = reservaClaseRepository.findByClaseIdAndClienteId(claseId, clienteId);
        if (existente.isPresent()) {
            ReservaClase res = existente.get();
            if ("CONFIRMADA".equalsIgnoreCase(res.getEstado())) {
                throw new IllegalStateException("Ya tienes una reserva confirmada para esta clase.");
            } else {
                res.setEstado("CONFIRMADA");
                res.setFechaReserva(LocalDateTime.now());
                return reservaClaseRepository.save(res);
            }
        }

        ReservaClase reserva = new ReservaClase(null, clase, cliente, LocalDateTime.now(), "CONFIRMADA");
        return reservaClaseRepository.save(reserva);
    }

    @Override
    public void cancelarReserva(Long reservaId) {
        reservaClaseRepository.findById(reservaId).ifPresent(r -> {
            r.setEstado("CANCELADA");
            reservaClaseRepository.save(r);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaClase> listarReservasPorCliente(Long clienteId) {
        return reservaClaseRepository.findByClienteIdOrderByFechaReservaDesc(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estaInscrito(Long claseId, Long clienteId) {
        return reservaClaseRepository.existsByClaseIdAndClienteIdAndEstadoNot(claseId, clienteId, "CANCELADA");
    }

    @Override
    public void inicializarDatosPorDefecto() {
        if (disciplinaRepository.count() == 0) {
            disciplinaRepository.save(new Disciplina(null, "Spinning Indoor", "Cardio de alta intensidad en bicicleta fija", "Alta"));
            disciplinaRepository.save(new Disciplina(null, "Crossfit & WOD", "Entrenamiento funcional por intervalos de alta intensidad", "Muy Alta"));
            disciplinaRepository.save(new Disciplina(null, "Boxeo Funcional", "Golpeo de saco, agilidad, resistencia y coordinación", "Alta"));
            disciplinaRepository.save(new Disciplina(null, "Yoga & Stretching", "Elongación muscular, movilidad y respiración", "Baja"));
        }

        if (claseGrupalRepository.count() == 0) {
            List<Disciplina> disciplinas = disciplinaRepository.findAll();
            List<Entrenador> entrenadores = entrenadorRepository.findAll();

            if (!disciplinas.isEmpty() && !entrenadores.isEmpty()) {
                Entrenador prof = entrenadores.get(0);
                LocalDate hoy = LocalDate.now();

                ClaseGrupal c1 = new ClaseGrupal();
                c1.setDisciplina(disciplinas.get(0));
                c1.setEntrenador(prof);
                c1.setSalon("Sala 1 - Spinning");
                c1.setFecha(hoy.plusDays(1));
                c1.setHoraInicio(LocalTime.of(7, 0));
                c1.setHoraFin(LocalTime.of(8, 0));
                c1.setCupoMaximo(20);
                claseGrupalRepository.save(c1);

                ClaseGrupal c2 = new ClaseGrupal();
                c2.setDisciplina(disciplinas.size() > 1 ? disciplinas.get(1) : disciplinas.get(0));
                c2.setEntrenador(prof);
                c2.setSalon("Box Cross Principal");
                c2.setFecha(hoy.plusDays(1));
                c2.setHoraInicio(LocalTime.of(18, 30));
                c2.setHoraFin(LocalTime.of(19, 30));
                c2.setCupoMaximo(15);
                claseGrupalRepository.save(c2);

                ClaseGrupal c3 = new ClaseGrupal();
                c3.setDisciplina(disciplinas.size() > 2 ? disciplinas.get(2) : disciplinas.get(0));
                c3.setEntrenador(prof);
                c3.setSalon("Sala de Contacto");
                c3.setFecha(hoy.plusDays(2));
                c3.setHoraInicio(LocalTime.of(19, 0));
                c3.setHoraFin(LocalTime.of(20, 0));
                c3.setCupoMaximo(12);
                claseGrupalRepository.save(c3);
            }
        }
    }
}
