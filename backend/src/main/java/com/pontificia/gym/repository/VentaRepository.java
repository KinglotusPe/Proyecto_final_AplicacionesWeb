package com.pontificia.gym.repository;

import com.pontificia.gym.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findAllByOrderByFechaHoraDesc();
    List<Venta> findByClienteIdOrderByFechaHoraDesc(Long clienteId);
    List<Venta> findByFechaHoraBetweenOrderByFechaHoraDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fechaHora BETWEEN :start AND :end")
    BigDecimal calcularTotalVentasEntre(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
