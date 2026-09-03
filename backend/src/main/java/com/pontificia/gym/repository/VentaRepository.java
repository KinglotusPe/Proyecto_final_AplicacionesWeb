package com.pontificia.gym.repository;

import com.pontificia.gym.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findAllByOrderByFechaDesc();
    List<Venta> findByClienteIdOrderByFechaDesc(Long clienteId);
    List<Venta> findByFechaBetweenOrderByFechaDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fecha BETWEEN :start AND :end")
    java.math.BigDecimal calcularTotalVentasEntre(LocalDateTime start, LocalDateTime end);
}
