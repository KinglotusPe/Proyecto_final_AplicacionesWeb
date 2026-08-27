package com.pontificia.gym.controller;

import com.pontificia.gym.entity.Cliente;
import com.pontificia.gym.entity.Membresia;
import com.pontificia.gym.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/portal")
public class PortalClienteController {

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final MembresiaService membresiaService;
    private final PagoService pagoService;
    private final AsistenciaService asistenciaService;
    private final SeguimientoFisicoService seguimientoFisicoService;

    public PortalClienteController(UsuarioService usuarioService,
                                   ClienteService clienteService,
                                   MembresiaService membresiaService,
                                   PagoService pagoService,
                                   AsistenciaService asistenciaService,
                                   SeguimientoFisicoService seguimientoFisicoService) {
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
        this.membresiaService = membresiaService;
        this.pagoService = pagoService;
        this.asistenciaService = asistenciaService;
        this.seguimientoFisicoService = seguimientoFisicoService;
    }

    @GetMapping("/mi-cuenta")
    public String miCuenta(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();

        // Buscar el cliente por su usuario vinculado o por su DNI
        Cliente cliente = usuarioService.buscarPorUsername(username)
                .map(u -> u.getCliente() != null ? u.getCliente() : clienteService.buscarPorDni(username).orElse(null))
                .orElseGet(() -> clienteService.buscarPorDni(username).orElse(null));

        if (cliente == null) {
            // Si es un admin explorando el portal de socio, tomar el primer cliente de muestra
            List<Cliente> clientes = clienteService.listarTodos();
            if (!clientes.isEmpty()) {
                cliente = clientes.get(0);
            }
        }

        if (cliente != null) {
            model.addAttribute("cliente", cliente);

            // Membresías
            List<Membresia> membresias = membresiaService.listarPorCliente(cliente.getId());
            model.addAttribute("membresias", membresias);

            Membresia activa = membresias.stream()
                    .filter(m -> "ACTIVA".equals(m.getEstado().name()))
                    .findFirst()
                    .orElse(null);
            model.addAttribute("membresiaActiva", activa);

            if (activa != null && activa.getFechaVencimiento() != null) {
                long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), activa.getFechaVencimiento());
                model.addAttribute("diasRestantes", Math.max(0, diasRestantes));
            } else {
                model.addAttribute("diasRestantes", 0);
            }

            // Pagos
            model.addAttribute("pagos", pagoService.listarPorCliente(cliente.getId()));

            // Asistencias
            model.addAttribute("asistencias", asistenciaService.listarPorCliente(cliente.getId()));

            // Seguimientos Físicos / Evaluaciones
            model.addAttribute("seguimientos", seguimientoFisicoService.listarPorCliente(cliente.getId()));
        }

        return "portal/mi_cuenta";
    }
}
