package com.pontificia.gym.repository;

import com.pontificia.gym.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByClienteId(Long clienteId);

    List<Pago> findByFechaBetween(LocalDate desde, LocalDate hasta);

    // Consulta JPQL con funcion de agregacion (SUM), similar a lo visto en las diapositivas
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p " +
           "WHERE p.fecha BETWEEN :desde AND :hasta")
    BigDecimal totalRecaudadoEntre(@Param("desde") LocalDate desde,
                                    @Param("hasta") LocalDate hasta);
}
