package com.pontificia.gym.controller;

import com.pontificia.gym.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        model.addAttribute("totalClientes", clienteService.contarTotal());
        model.addAttribute("membresiasActivas", membresiaService.contarActivas());
        model.addAttribute("membresiasPorVencer", membresiaService.listarPorVencerEnDias(7).size());
        model.addAttribute("membresiasVencidas", membresiaService.contarVencidas());
        model.addAttribute("pagosDelMes", pagoService.totalRecaudadoDelMes());
        model.addAttribute("asistenciasHoy", asistenciaService.contarAsistenciasDeHoy());
        model.addAttribute("totalEntrenadores", entrenadorService.contarActivos());
        model.addAttribute("totalSeguimientos", seguimientoFisicoService.contarTotal());
        return "index";
    }
}
