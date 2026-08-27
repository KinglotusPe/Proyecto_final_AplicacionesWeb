package com.pontificia.gym.controller;

import com.pontificia.gym.entity.Asistencia;
import com.pontificia.gym.entity.Cliente;
import com.pontificia.gym.entity.Membresia;
import com.pontificia.gym.service.AsistenciaService;
import com.pontificia.gym.service.ClienteService;
import com.pontificia.gym.service.MembresiaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/asistencias/control-acceso")
public class ControlAccesoController {

    private final ClienteService clienteService;
    private final MembresiaService membresiaService;
    private final AsistenciaService asistenciaService;

    public ControlAccesoController(ClienteService clienteService,
                                   MembresiaService membresiaService,
                                   AsistenciaService asistenciaService) {
        this.clienteService = clienteService;
        this.membresiaService = membresiaService;
        this.asistenciaService = asistenciaService;
    }

    @GetMapping
    public String vistaControlAcceso(Model model) {
        model.addAttribute("asistenciasHoy", asistenciaService.listarHoy());
        model.addAttribute("totalHoy", asistenciaService.contarAsistenciasHoy());
        return "asistencias/control_acceso";
    }

    @PostMapping("/verificar")
    public String verificarAcceso(@RequestParam("dni") String dni, RedirectAttributes redirectAttributes) {
        if (dni == null || dni.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("resultado", "VACIO");
            return "redirect:/asistencias/control-acceso";
        }

        String dniLimpio = dni.trim();
        Optional<Cliente> optCliente = clienteService.buscarPorDni(dniLimpio);

        if (optCliente.isEmpty()) {
            redirectAttributes.addFlashAttribute("resultado", "NO_REGISTRADO");
            redirectAttributes.addFlashAttribute("dniBuscado", dniLimpio);
            return "redirect:/asistencias/control-acceso";
        }

        Cliente cliente = optCliente.get();
        List<Membresia> membresias = membresiaService.listarPorCliente(cliente.getId());
        Membresia activa = membresias.stream()
                .filter(m -> "ACTIVA".equals(m.getEstado().name()))
                .findFirst()
                .orElse(null);

        if (activa != null && activa.getFechaVencimiento() != null && !activa.getFechaVencimiento().isBefore(LocalDate.now())) {
            // Membresía ACTIVA y VIGENTE: Registrar asistencia
            Asistencia nueva = new Asistencia();
            nueva.setCliente(cliente);
            nueva.setFechaHora(LocalDateTime.now());
            asistenciaService.guardar(nueva);

            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), activa.getFechaVencimiento());

            redirectAttributes.addFlashAttribute("resultado", "PERMITIDO");
            redirectAttributes.addFlashAttribute("cliente", cliente);
            redirectAttributes.addFlashAttribute("membresia", activa);
            redirectAttributes.addFlashAttribute("diasRestantes", diasRestantes);
        } else {
            // Membresía VENCIDA o SIN MEMBRESÍA
            redirectAttributes.addFlashAttribute("resultado", "DENEGADO");
            redirectAttributes.addFlashAttribute("cliente", cliente);
            redirectAttributes.addFlashAttribute("ultimaMembresia", membresias.isEmpty() ? null : membresias.get(0));
        }

        return "redirect:/asistencias/control-acceso";
    }
}
