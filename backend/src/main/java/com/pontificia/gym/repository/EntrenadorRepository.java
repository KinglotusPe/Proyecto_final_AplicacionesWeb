package com.pontificia.gym.repository;

import com.pontificia.gym.entity.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
    Optional<Entrenador> findByDni(String dni);
    List<Entrenador> findByEstado(String estado);
    long countByEstado(String estado);
}
