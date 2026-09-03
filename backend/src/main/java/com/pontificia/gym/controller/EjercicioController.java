package com.pontificia.gym.controller;

import com.pontificia.gym.entity.Ejercicio;
import com.pontificia.gym.service.EjercicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ejercicios")
public class EjercicioController {

    private final EjercicioService ejercicioService;

    public EjercicioController(EjercicioService ejercicioService) {
        this.ejercicioService = ejercicioService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String grupo,
                         @RequestParam(required = false) String q,
                         Model model) {
        ejercicioService.inicializarDatasetEjercicios();

        List<Ejercicio> ejercicios;
        if (q != null && !q.trim().isEmpty()) {
            ejercicios = ejercicioService.buscarPorTexto(q.trim());
        } else if (grupo != null && !grupo.equalsIgnoreCase("TODOS")) {
            ejercicios = ejercicioService.buscarPorGrupoMuscular(grupo);
        } else {
            ejercicios = ejercicioService.listarTodos();
        }

        model.addAttribute("ejercicios", ejercicios);
        model.addAttribute("grupoSeleccionado", grupo != null ? grupo : "TODOS");
        model.addAttribute("query", q != null ? q : "");
        return "ejercicios/list";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Ejercicio> obtenerDetalleApi(@PathVariable Long id) {
        return ejercicioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
