package com.pontificia.gym.controller;

import com.pontificia.gym.entity.Entrenador;
import com.pontificia.gym.service.EntrenadorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/entrenadores")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    public EntrenadorController(EntrenadorService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("entrenadores", entrenadorService.listarTodos());
        return "entrenadores/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("entrenador", new Entrenador());
        return "entrenadores/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return entrenadorService.buscarPorId(id)
                .map(e -> {
                    model.addAttribute("entrenador", e);
                    return "entrenadores/form";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "Entrenador no encontrado");
                    return "redirect:/entrenadores";
                });
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("entrenador") Entrenador entrenador,
                          BindingResult result,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "entrenadores/form";
        }
        entrenadorService.guardar(entrenador);
        flash.addFlashAttribute("success", "Entrenador guardado exitosamente");
        return "redirect:/entrenadores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            entrenadorService.eliminar(id);
            flash.addFlashAttribute("success", "Entrenador eliminado exitosamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se puede eliminar el entrenador (tiene registros asociados)");
        }
        return "redirect:/entrenadores";
    }
}
