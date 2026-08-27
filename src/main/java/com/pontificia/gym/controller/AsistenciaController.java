package com.pontificia.gym.controller;

import com.pontificia.gym.service.AsistenciaService;
import com.pontificia.gym.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;
    private final ClienteService clienteService;

    public AsistenciaController(AsistenciaService asistenciaService, ClienteService clienteService) {
        this.asistenciaService = asistenciaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("asistencias", asistenciaService.listarTodos());
        model.addAttribute("clientes", clienteService.listarTodos());
        return "asistencias/list";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam Long clienteId, RedirectAttributes redirectAttributes) {
        asistenciaService.registrarEntrada(clienteId);
        redirectAttributes.addFlashAttribute("mensaje", "Asistencia registrada");
        return "redirect:/asistencias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        asistenciaService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Registro de asistencia eliminado");
        return "redirect:/asistencias";
    }
}
