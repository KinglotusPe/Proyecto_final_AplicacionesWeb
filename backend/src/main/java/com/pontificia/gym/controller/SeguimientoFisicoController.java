package com.pontificia.gym.controller;

import com.pontificia.gym.entity.SeguimientoFisico;
import com.pontificia.gym.service.ClienteService;
import com.pontificia.gym.service.EntrenadorService;
import com.pontificia.gym.service.SeguimientoFisicoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/seguimientos")
public class SeguimientoFisicoController {

    private final SeguimientoFisicoService seguimientoFisicoService;
    private final ClienteService clienteService;
    private final EntrenadorService entrenadorService;

    public SeguimientoFisicoController(SeguimientoFisicoService seguimientoFisicoService,
                                       ClienteService clienteService,
                                       EntrenadorService entrenadorService) {
        this.seguimientoFisicoService = seguimientoFisicoService;
        this.clienteService = clienteService;
        this.entrenadorService = entrenadorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("seguimientos", seguimientoFisicoService.listarTodos());
        return "seguimientos/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        SeguimientoFisico seguimiento = new SeguimientoFisico();
        seguimiento.setFechaRegistro(LocalDate.now());
        model.addAttribute("seguimiento", seguimiento);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("entrenadores", entrenadorService.listarActivos());
        return "seguimientos/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return seguimientoFisicoService.buscarPorId(id)
                .map(s -> {
                    model.addAttribute("seguimiento", s);
                    model.addAttribute("clientes", clienteService.listarTodos());
                    model.addAttribute("entrenadores", entrenadorService.listarActivos());
                    return "seguimientos/form";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "Ficha de seguimiento no encontrada");
                    return "redirect:/seguimientos";
                });
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("seguimiento") SeguimientoFisico seguimiento,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("entrenadores", entrenadorService.listarActivos());
            return "seguimientos/form";
        }
        seguimientoFisicoService.guardar(seguimiento);
        flash.addFlashAttribute("success", "Ficha de seguimiento físico guardada exitosamente");
        return "redirect:/seguimientos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            seguimientoFisicoService.eliminar(id);
            flash.addFlashAttribute("success", "Registro eliminado exitosamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el registro");
        }
        return "redirect:/seguimientos";
    }
}
