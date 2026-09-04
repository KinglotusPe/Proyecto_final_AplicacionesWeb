package com.pontificia.gym.controller;

import com.pontificia.gym.entity.Ejercicio;
import com.pontificia.gym.service.EjercicioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("ejercicio", new Ejercicio());
        return "ejercicios/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Ejercicio ejercicio = ejercicioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ejercicio no encontrado: " + id));
        model.addAttribute("ejercicio", ejercicio);
        return "ejercicios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("ejercicio") Ejercicio ejercicio,
                          BindingResult result,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "ejercicios/form";
        }
        ejercicioService.guardar(ejercicio);
        flash.addFlashAttribute("mensaje", "Ejercicio guardado correctamente.");
        return "redirect:/ejercicios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        ejercicioService.eliminar(id);
        flash.addFlashAttribute("mensaje", "Ejercicio eliminado.");
        return "redirect:/ejercicios";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Ejercicio> obtenerDetalleApi(@PathVariable Long id) {
        return ejercicioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
