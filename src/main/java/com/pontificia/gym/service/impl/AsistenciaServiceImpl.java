package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.Asistencia;
import com.pontificia.gym.entity.Cliente;
import com.pontificia.gym.repository.AsistenciaRepository;
import com.pontificia.gym.service.AsistenciaService;
import com.pontificia.gym.service.ClienteService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Comparator;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final ClienteService clienteService;

    public AsistenciaServiceImpl(AsistenciaRepository asistenciaRepository, ClienteService clienteService) {
        this.asistenciaRepository = asistenciaRepository;
        this.clienteService = clienteService;
    }

    @Override
    public List<Asistencia> listarTodos() {
        List<Asistencia> lista = asistenciaRepository.findAll();
        lista.sort(Comparator.comparing(Asistencia::getFechaHora).reversed());
        return lista;
    }

    @Override
    public List<Asistencia> listarDeHoy() {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(23, 59, 59);
        return asistenciaRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(inicio, fin);
    }

    @Override
    public List<Asistencia> listarPorCliente(Long clienteId) {
        return asistenciaRepository.findByClienteId(clienteId);
    }

    @Override
    public Asistencia registrarEntrada(Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        Asistencia asistencia = new Asistencia();
        asistencia.setCliente(cliente);
        asistencia.setFechaHora(LocalDateTime.now());
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }

    @Override
    public long contarAsistenciasDeHoy() {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(23, 59, 59);
        return asistenciaRepository.countByFechaHoraBetween(inicio, fin);
    }
}
