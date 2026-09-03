package com.pontificia.gym.controller;

import com.pontificia.gym.entity.Casillero;
import com.pontificia.gym.service.CasilleroService;
import com.pontificia.gym.service.ClienteService;
import com.pontificia.gym.service.EntrenadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/casilleros")
public class CasilleroController {

    private final CasilleroService casilleroService;
    private final ClienteService clienteService;
    private final EntrenadorService entrenadorService;

    public CasilleroController(CasilleroService casilleroService,
                               ClienteService clienteService,
                               EntrenadorService entrenadorService) {
        this.casilleroService = casilleroService;
        this.clienteService = clienteService;
        this.entrenadorService = entrenadorService;
    }

    @GetMapping
    public String vistaMapaCasilleros(Model model) {
        casilleroService.inicializarCasillerosPorDefecto();

        model.addAttribute("casilleros", casilleroService.listarTodos());
        model.addAttribute("totalLibres", casilleroService.contarDisponibles());
        model.addAttribute("totalOcupados", casilleroService.contarOcupados());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("entrenadores", entrenadorService.listarTodos());

        return "casilleros/mapa";
    }

    @PostMapping("/liberar/{id}")
    public String liberarCasillero(@PathVariable("id") Long id, RedirectAttributes flash) {
        casilleroService.liberarCasilleroPorId(id);
        flash.addFlashAttribute("success", "Casillero liberado correctamente.");
        return "redirect:/casilleros";
    }

    @PostMapping("/liberar-todos")
    public String liberarTodos(RedirectAttributes flash) {
        casilleroService.liberarTodos();
        flash.addFlashAttribute("success", "🧹 Todos los casilleros han sido liberados para el nuevo turno.");
        return "redirect:/casilleros";
    }

    @PostMapping("/mantenimiento/{id}")
    public String toggleMantenimiento(@PathVariable("id") Long id, RedirectAttributes flash) {
        casilleroService.buscarPorId(id).ifPresent(c -> {
            if ("MANTENIMIENTO".equals(c.getEstado())) {
                c.setEstado("DISPONIBLE");
                flash.addFlashAttribute("success", "Casillero habilitado nuevamente.");
            } else {
                c.setEstado("MANTENIMIENTO");
                flash.addFlashAttribute("warning", "Casillero puesto en mantenimiento.");
            }
            casilleroService.guardar(c);
        });
        return "redirect:/casilleros";
    }

    @PostMapping("/asignar-manual")
    public String asignarManual(@RequestParam("casilleroId") Long casilleroId,
                                @RequestParam("nombre") String nombre,
                                @RequestParam("dni") String dni,
                                @RequestParam(value = "tipo", defaultValue = "SOCIO") String tipo,
                                RedirectAttributes flash) {
        try {
            casilleroService.asignarCasilleroEspecifico(casilleroId, nombre, dni, tipo);
            flash.addFlashAttribute("success", "Casillero asignado manualmente a: " + nombre);
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al asignar casillero: " + e.getMessage());
        }
        return "redirect:/casilleros";
    }
}
