package com.pontificia.gym.controller;

import com.pontificia.gym.entity.Asistencia;
import com.pontificia.gym.entity.Cliente;
import com.pontificia.gym.entity.Membresia;
import com.pontificia.gym.service.AsistenciaService;
import com.pontificia.gym.service.ClienteService;
import com.pontificia.gym.service.MembresiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

        // Verificar si ya registró ingreso hoy para control de seguridad / doble pase
        List<Asistencia> hoy = asistenciaService.listarHoy();
        Optional<Asistencia> ingresoPrevio = hoy.stream()
                .filter(a -> a.getCliente().getId().equals(cliente.getId()))
                .findFirst();

        boolean reingresoHoy = ingresoPrevio.isPresent();
        String horaPrimerIngreso = reingresoHoy ? ingresoPrevio.get().getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : null;

        if (activa != null && activa.getFechaVencimiento() != null && !activa.getFechaVencimiento().isBefore(LocalDate.now())) {
            // Membresía ACTIVA y VIGENTE: Registrar asistencia
            Asistencia nueva = new Asistencia();
            nueva.setCliente(cliente);
            nueva.setFechaHora(LocalDateTime.now());
            asistenciaService.guardar(nueva);

            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), activa.getFechaVencimiento());

            redirectAttributes.addFlashAttribute("resultado", reingresoHoy ? "REINGRESO_ADVERTENCIA" : "PERMITIDO");
            redirectAttributes.addFlashAttribute("cliente", cliente);
            redirectAttributes.addFlashAttribute("membresia", activa);
            redirectAttributes.addFlashAttribute("diasRestantes", diasRestantes);
            redirectAttributes.addFlashAttribute("reingresoHoy", reingresoHoy);
            redirectAttributes.addFlashAttribute("horaPrimerIngreso", horaPrimerIngreso);
        } else {
            // Membresía VENCIDA o SIN MEMBRESÍA
            redirectAttributes.addFlashAttribute("resultado", "DENEGADO");
            redirectAttributes.addFlashAttribute("cliente", cliente);
            redirectAttributes.addFlashAttribute("ultimaMembresia", membresias.isEmpty() ? null : membresias.get(0));
            redirectAttributes.addFlashAttribute("reingresoHoy", reingresoHoy);
            redirectAttributes.addFlashAttribute("horaPrimerIngreso", horaPrimerIngreso);
        }

        return "redirect:/asistencias/control-acceso";
    }

    @PostMapping("/api/verificar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verificarAccesoApi(@RequestBody Map<String, String> payload) {
        String dni = payload.get("dni");
        Map<String, Object> resp = new HashMap<>();

        if (dni == null || dni.trim().isEmpty()) {
            resp.put("status", "VACIO");
            return ResponseEntity.badRequest().body(resp);
        }

        String dniLimpio = dni.trim();
        Optional<Cliente> optCliente = clienteService.buscarPorDni(dniLimpio);

        if (optCliente.isEmpty()) {
            resp.put("status", "NO_REGISTRADO");
            resp.put("dni", dniLimpio);
            return ResponseEntity.ok(resp);
        }

        Cliente cliente = optCliente.get();
        List<Membresia> membresias = membresiaService.listarPorCliente(cliente.getId());
        Membresia activa = membresias.stream()
                .filter(m -> "ACTIVA".equals(m.getEstado().name()))
                .findFirst()
                .orElse(null);

        List<Asistencia> hoy = asistenciaService.listarHoy();
        Optional<Asistencia> ingresoPrevio = hoy.stream()
                .filter(a -> a.getCliente().getId().equals(cliente.getId()))
                .findFirst();

        boolean reingresoHoy = ingresoPrevio.isPresent();
        String horaPrimerIngreso = reingresoHoy ? ingresoPrevio.get().getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : null;

        resp.put("clienteId", cliente.getId());
        resp.put("nombreCompleto", cliente.getNombreCompleto());
        resp.put("dni", cliente.getDni());
        resp.put("fotoUrl", cliente.getFotoUrlOrDefault());
        resp.put("reingresoHoy", reingresoHoy);
        resp.put("horaPrimerIngreso", horaPrimerIngreso);

        if (activa != null && activa.getFechaVencimiento() != null && !activa.getFechaVencimiento().isBefore(LocalDate.now())) {
            Asistencia nueva = new Asistencia();
            nueva.setCliente(cliente);
            nueva.setFechaHora(LocalDateTime.now());
            asistenciaService.guardar(nueva);

            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), activa.getFechaVencimiento());
            resp.put("status", reingresoHoy ? "REINGRESO_ADVERTENCIA" : "PERMITIDO");
            resp.put("plan", activa.getTipo());
            resp.put("vencimiento", activa.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            resp.put("diasRestantes", diasRestantes);
        } else {
            resp.put("status", "DENEGADO");
            resp.put("vencimiento", activa != null && activa.getFechaVencimiento() != null ? activa.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "SIN PLAN");
        }

        return ResponseEntity.ok(resp);
    }
}
