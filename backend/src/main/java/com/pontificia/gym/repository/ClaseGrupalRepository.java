package com.pontificia.gym.repository;

import com.pontificia.gym.entity.ClaseGrupal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClaseGrupalRepository extends JpaRepository<ClaseGrupal, Long> {

    List<ClaseGrupal> findByFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(LocalDate fecha);

    List<ClaseGrupal> findByDisciplinaIdOrderByFechaAscHoraInicioAsc(Long disciplinaId);

    List<ClaseGrupal> findByEntrenadorIdOrderByFechaAscHoraInicioAsc(Long entrenadorId);

    @Query("SELECT c FROM ClaseGrupal c ORDER BY c.fecha ASC, c.horaInicio ASC")
    List<ClaseGrupal> listarTodasOrdenadas();
}
