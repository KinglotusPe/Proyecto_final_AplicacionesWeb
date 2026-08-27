package com.pontificia.gym.repository;

import com.pontificia.gym.entity.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    List<Rutina> findByClienteId(Long clienteId);
    List<Rutina> findByEntrenadorId(Long entrenadorId);
}
