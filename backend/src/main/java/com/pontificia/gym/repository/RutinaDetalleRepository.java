package com.pontificia.gym.repository;

import com.pontificia.gym.entity.RutinaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaDetalleRepository extends JpaRepository<RutinaDetalle, Long> {
    List<RutinaDetalle> findByRutinaId(Long rutinaId);
    void deleteByRutinaId(Long rutinaId);
}
