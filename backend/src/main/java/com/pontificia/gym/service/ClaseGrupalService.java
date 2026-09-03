package com.pontificia.gym.service;

import com.pontificia.gym.entity.ClaseGrupal;
import com.pontificia.gym.entity.Disciplina;
import com.pontificia.gym.entity.ReservaClase;

import java.util.List;
import java.util.Optional;

public interface ClaseGrupalService {
    List<ClaseGrupal> listarTodas();
    List<ClaseGrupal> listarProximas();
    Optional<ClaseGrupal> buscarPorId(Long id);
    ClaseGrupal guardar(ClaseGrupal clase);
    void eliminar(Long id);

    List<Disciplina> listarDisciplinas();
    Disciplina guardarDisciplina(Disciplina disciplina);

    ReservaClase reservarCupo(Long claseId, Long clienteId);
    void cancelarReserva(Long reservaId);
    List<ReservaClase> listarReservasPorCliente(Long clienteId);
    boolean estaInscrito(Long claseId, Long clienteId);

    void inicializarDatosPorDefecto();
}
