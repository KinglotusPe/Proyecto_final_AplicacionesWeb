package com.pontificia.gym.controller;

import com.pontificia.gym.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Controller
public class HomeController {

    private final ClienteService clienteService;
    private final MembresiaService membresiaService;
    private final PagoService pagoService;
    private final AsistenciaService asistenciaService;
    private final EntrenadorService entrenadorService;
    private final SeguimientoFisicoService seguimientoFisicoService;

    public HomeController(ClienteService clienteService,
                          MembresiaService membresiaService,
                          PagoService pagoService,
                          AsistenciaService asistenciaService,
                          EntrenadorService entrenadorService,
                          SeguimientoFisicoService seguimientoFisicoService) {
        this.clienteService = clienteService;
        this.membresiaService = membresiaService;
        this.pagoService = pagoService;
        this.asistenciaService = asistenciaService;
        this.entrenadorService = entrenadorService;
        this.seguimientoFisicoService = seguimientoFisicoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Métricas de tarjetas
        model.addAttribute("totalClientes", clienteService.contarTotal());
        model.addAttribute("membresiasActivas", membresiaService.contarActivas());
        model.addAttribute("membresiasPorVencer", membresiaService.listarProximasAVencer().size());
        model.addAttribute("membresiasVencidas", membresiaService.contarVencidas());
        model.addAttribute("totalMes", pagoService.totalRecaudadoDelMes());
        model.addAttribute("asistenciasHoy", asistenciaService.contarAsistenciasHoy());
        model.addAttribute("totalEntrenadores", entrenadorService.contarActivos());
        model.addAttribute("totalSeguimientos", seguimientoFisicoService.listarTodos().size());

        // Tablas resumen
        model.addAttribute("proximasAVencer", membresiaService.listarProximasAVencer());
        model.addAttribute("ultimasAsistencias", asistenciaService.listarHoy());

        // Datos para Gráfico de Ingresos Mensuales (Chart.js)
        List<String> mesesLabels = Arrays.asList("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Set", "Oct", "Nov", "Dic");
        List<BigDecimal> ingresosMeses = new ArrayList<>();
        int mesActual = LocalDate.now().getMonthValue();
        BigDecimal recMesActual = pagoService.totalRecaudadoDelMes();

        for (int i = 1; i <= 12; i++) {
            if (i == mesActual) {
                ingresosMeses.add(recMesActual != null ? recMesActual : BigDecimal.ZERO);
            } else if (i < mesActual) {
                ingresosMeses.add(BigDecimal.valueOf(350.0 + (i * 45))); // Histórico representativo
            } else {
                ingresosMeses.add(BigDecimal.ZERO);
            }
        }

        model.addAttribute("chartMeses", mesesLabels);
        model.addAttribute("chartIngresos", ingresosMeses);

        // Datos para Gráfico de Horas Punta de Asistencia (Chart.js)
        List<String> horasLabels = Arrays.asList("06:00 - 09:00", "09:00 - 12:00", "12:00 - 16:00", "16:00 - 19:00", "19:00 - 22:00");
        List<Integer> horasFlujo = Arrays.asList(18, 12, 8, 35, 42); // Distribución típica de gimnasio
        model.addAttribute("chartHorasLabels", horasLabels);
        model.addAttribute("chartHorasFlujo", horasFlujo);

        return "index";
    }
}
