package com.pontificia.gym.repository;

import com.pontificia.gym.entity.SeguimientoFisico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoFisicoRepository extends JpaRepository<SeguimientoFisico, Long> {
    List<SeguimientoFisico> findByClienteIdOrderByFechaRegistroDesc(Long clienteId);
    List<SeguimientoFisico> findAllByOrderByFechaRegistroDesc();
}
