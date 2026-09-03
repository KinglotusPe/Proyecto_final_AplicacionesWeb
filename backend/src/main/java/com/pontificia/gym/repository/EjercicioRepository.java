package com.pontificia.gym.repository;

import com.pontificia.gym.entity.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    List<Ejercicio> findByGrupoMuscularIgnoreCase(String grupoMuscular);

    List<Ejercicio> findByEquipamientoIgnoreCase(String equipamiento);

    List<Ejercicio> findByNombreContainingIgnoreCaseOrGrupoMuscularContainingIgnoreCase(String nombre, String grupo);

    boolean existsByNombre(String nombre);
}
