package com.pontificia.gym.repository;

import com.pontificia.gym.entity.ReservaClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaClaseRepository extends JpaRepository<ReservaClase, Long> {

    List<ReservaClase> findByClienteIdOrderByFechaReservaDesc(Long clienteId);

    List<ReservaClase> findByClaseIdOrderByFechaReservaAsc(Long claseId);

    Optional<ReservaClase> findByClaseIdAndClienteId(Long claseId, Long clienteId);

    boolean existsByClaseIdAndClienteIdAndEstadoNot(Long claseId, Long clienteId, String estado);

    long countByClaseIdAndEstadoNot(Long claseId, String estado);
}
