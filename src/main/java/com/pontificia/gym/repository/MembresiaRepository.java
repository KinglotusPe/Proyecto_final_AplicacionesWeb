package com.pontificia.gym.repository;

import com.pontificia.gym.entity.EstadoMembresia;
import com.pontificia.gym.entity.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MembresiaRepository extends JpaRepository<Membresia, Long> {

    List<Membresia> findByClienteId(Long clienteId);

    List<Membresia> findByEstado(EstadoMembresia estado);

    long countByEstado(EstadoMembresia estado);

    // Consulta JPQL con @Query, tal como se vio en clase con Spring Data JPA
    @Query("SELECT m FROM Membresia m WHERE m.estado = 'ACTIVA' " +
           "AND m.fechaVencimiento BETWEEN :hoy AND :limite")
    List<Membresia> findMembresiasPorVencer(@Param("hoy") LocalDate hoy,
                                             @Param("limite") LocalDate limite);
}
