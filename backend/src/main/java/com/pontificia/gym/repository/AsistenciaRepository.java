package com.pontificia.gym.repository;

import com.pontificia.gym.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByClienteId(Long clienteId);

    long countByClienteId(Long clienteId);

    List<Asistencia> findByFechaHoraBetweenOrderByFechaHoraDesc(LocalDateTime desde, LocalDateTime hasta);

    long countByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);
}
