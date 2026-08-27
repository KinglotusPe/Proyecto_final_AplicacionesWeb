package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.Asistencia;
import com.pontificia.gym.entity.Cliente;
import com.pontificia.gym.repository.AsistenciaRepository;
import com.pontificia.gym.repository.ClienteRepository;
import com.pontificia.gym.service.AsistenciaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final ClienteRepository clienteRepository;

    public AsistenciaServiceImpl(AsistenciaRepository asistenciaRepository,
                                  ClienteRepository clienteRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> listarTodos() {
        return asistenciaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> listarDeHoy() {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(LocalTime.MAX);
        return asistenciaRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> listarHoy() {
        return listarDeHoy();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> listarPorCliente(Long clienteId) {
        return asistenciaRepository.findByClienteIdOrderByFechaHoraDesc(clienteId);
    }

    @Override
    public Asistencia registrarEntrada(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clienteId));

        Asistencia asistencia = new Asistencia();
        asistencia.setCliente(cliente);
        asistencia.setFechaHora(LocalDateTime.now());
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia guardar(Asistencia asistencia) {
        if (asistencia.getFechaHora() == null) {
            asistencia.setFechaHora(LocalDateTime.now());
        }
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarAsistenciasDeHoy() {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(LocalTime.MAX);
        return asistenciaRepository.countByFechaHoraBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarAsistenciasHoy() {
        return contarAsistenciasDeHoy();
    }
}
