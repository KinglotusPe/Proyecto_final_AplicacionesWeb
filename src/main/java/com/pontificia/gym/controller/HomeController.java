package com.pontificia.gym.controller;

import com.pontificia.gym.service.AsistenciaService;
import com.pontificia.gym.service.ClienteService;
import com.pontificia.gym.service.MembresiaService;
import com.pontificia.gym.service.PagoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller: recibe las peticiones del navegador y llama al Service.
 * Muestra el dashboard con los indicadores principales (como en la propuesta del gimnasio).
 */
@Controller
public class HomeController {

    private final ClienteService clienteService;
    private final MembresiaService membresiaService;
    private final PagoService pagoService;
    private final AsistenciaService asistenciaService;

    public HomeController(ClienteService clienteService,
                          MembresiaService membresiaService,
                          PagoService pagoService,
                          AsistenciaService asistenciaService) {
        this.clienteService = clienteService;
        this.membresiaService = membresiaService;
        this.pagoService = pagoService;
        this.asistenciaService = asistenciaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("totalClientes", clienteService.contarTotal());
        model.addAttribute("membresiasActivas", membresiaService.contarActivas());
        model.addAttribute("membresiasVencidas", membresiaService.contarVencidas());
        model.addAttribute("membresiasPorVencer", membresiaService.listarPorVencerEnDias(7).size());
        model.addAttribute("pagosDelMes", pagoService.totalRecaudadoDelMes());
        model.addAttribute("asistenciasHoy", asistenciaService.contarAsistenciasDeHoy());
        return "index";
    }
}
