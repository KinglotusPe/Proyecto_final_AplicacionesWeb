package com.pontificia.gym.controller;

import com.pontificia.gym.entity.Membresia;
import com.pontificia.gym.entity.TipoMembresia;
import com.pontificia.gym.service.ClienteService;
import com.pontificia.gym.service.MembresiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/membresias")
@RequiredArgsConstructor
public class MembresiaController {

    private final MembresiaService membresiaService;
    private final ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("membresias", membresiaService.listarTodos());
        return "membresias/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("membresia", new Membresia());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("tipos", TipoMembresia.values());
        return "membresias/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("membresia", membresiaService.buscarPorId(id));
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("tipos", TipoMembresia.values());
        return "membresias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("membresia") Membresia membresia,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("tipos", TipoMembresia.values());
            return "membresias/form";
        }
        membresiaService.guardar(membresia);
        redirectAttributes.addFlashAttribute("mensaje", "Membresia guardada correctamente");
        return "redirect:/membresias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        membresiaService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Membresia eliminada");
        return "redirect:/membresias";
    }
}
