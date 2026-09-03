package com.pontificia.gym.controller;

import com.pontificia.gym.entity.Asistencia;
import com.pontificia.gym.entity.Cliente;
import com.pontificia.gym.entity.Entrenador;
import com.pontificia.gym.entity.Membresia;
import com.pontificia.gym.service.AsistenciaService;
import com.pontificia.gym.service.ClienteService;
import com.pontificia.gym.service.EntrenadorService;
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
    private final EntrenadorService entrenadorService;
    private final MembresiaService membresiaService;
    private final AsistenciaService asistenciaService;

    // Registro en memoria de marcajes laborales de entrenadores del día (Entrada / Salida)
    private static final Map<Long, LocalDateTime> marcajeEntradaEntrenadores = new HashMap<>();

    public ControlAccesoController(ClienteService clienteService,
                                   EntrenadorService entrenadorService,
                                   MembresiaService membresiaService,
                                   AsistenciaService asistenciaService) {
        this.clienteService = clienteService;
        this.entrenadorService = entrenadorService;
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

        // 1. Verificar si es Entrenador / Staff Deportivo
        Optional<Entrenador> optEntrenador = entrenadorService.buscarPorDni(dniLimpio);
        if (optEntrenador.isPresent()) {
            Entrenador coach = optEntrenador.get();
            LocalDateTime ahora = LocalDateTime.now();

            if (!marcajeEntradaEntrenadores.containsKey(coach.getId())) {
                // Primer marcaje: ENTRADA DE TURNO
                marcajeEntradaEntrenadores.put(coach.getId(), ahora);
                redirectAttributes.addFlashAttribute("resultado", "ENTRENADOR_ENTRADA");
                redirectAttributes.addFlashAttribute("entrenador", coach);
                redirectAttributes.addFlashAttribute("horaMarcaje", ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            } else {
                // Segundo marcaje: SALIDA DE TURNO
                LocalDateTime entrada = marcajeEntradaEntrenadores.remove(coach.getId());
                long minutos = ChronoUnit.MINUTES.between(entrada, ahora);
                long horas = minutos / 60;
                long minRestantes = minutos % 60;

                redirectAttributes.addFlashAttribute("resultado", "ENTRENADOR_SALIDA");
                redirectAttributes.addFlashAttribute("entrenador", coach);
                redirectAttributes.addFlashAttribute("horaEntrada", entrada.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                redirectAttributes.addFlashAttribute("horaSalida", ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                redirectAttributes.addFlashAttribute("tiempoTrabajado", horas + "h " + minRestantes + "m");
            }
            return "redirect:/asistencias/control-acceso";
        }

        // 2. Verificar si es Socio / Cliente
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

        // 1. Verificar si es Entrenador
        Optional<Entrenador> optEntrenador = entrenadorService.buscarPorDni(dniLimpio);
        if (optEntrenador.isPresent()) {
            Entrenador coach = optEntrenador.get();
            LocalDateTime ahora = LocalDateTime.now();

            resp.put("perfil", "ENTRENADOR");
            resp.put("nombreCompleto", coach.getNombreCompleto());
            resp.put("dni", coach.getDni());
            resp.put("especialidad", coach.getEspecialidad());
            resp.put("fotoUrl", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80");

            if (!marcajeEntradaEntrenadores.containsKey(coach.getId())) {
                marcajeEntradaEntrenadores.put(coach.getId(), ahora);
                resp.put("status", "ENTRENADOR_ENTRADA");
                resp.put("horaMarcaje", ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            } else {
                LocalDateTime entrada = marcajeEntradaEntrenadores.remove(coach.getId());
                long minutos = ChronoUnit.MINUTES.between(entrada, ahora);
                long horas = minutos / 60;
                long minRestantes = minutos % 60;

                resp.put("status", "ENTRENADOR_SALIDA");
                resp.put("horaEntrada", entrada.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                resp.put("horaSalida", ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                resp.put("tiempoTrabajado", horas + "h " + minRestantes + "m");
            }
            return ResponseEntity.ok(resp);
        }

        // 2. Verificar si es Socio
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

        resp.put("perfil", "SOCIO");
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
