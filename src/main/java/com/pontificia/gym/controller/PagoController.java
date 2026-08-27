package com.pontificia.gym.controller;

import com.pontificia.gym.entity.MetodoPago;
import com.pontificia.gym.entity.Pago;
import com.pontificia.gym.service.ClienteService;
import com.pontificia.gym.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final ClienteService clienteService;

    public PagoController(PagoService pagoService, ClienteService clienteService) {
        this.pagoService = pagoService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pagos", pagoService.listarTodos());
        model.addAttribute("totalMes", pagoService.totalRecaudadoDelMes());
        return "pagos/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        Pago pago = new Pago();
        pago.setFecha(java.time.LocalDate.now());
        pago.setProximaFechaPago(java.time.LocalDate.now().plusMonths(1));
        model.addAttribute("pago", pago);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("metodos", MetodoPago.values());
        return "pagos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("pago") Pago pago,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("metodos", MetodoPago.values());
            return "pagos/form";
        }
        pagoService.guardar(pago);
        redirectAttributes.addFlashAttribute("mensaje", "Pago registrado correctamente");
        return "redirect:/pagos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pagoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Pago eliminado");
        return "redirect:/pagos";
    }
}
