package com.pontificia.gym.controller;

import com.pontificia.gym.entity.MetodoPago;
import com.pontificia.gym.entity.Pago;
import com.pontificia.gym.service.ClienteService;
import com.pontificia.gym.service.PagoService;
import com.pontificia.gym.service.PdfReporteService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final ClienteService clienteService;
    private final PdfReporteService pdfReporteService;

    public PagoController(PagoService pagoService, ClienteService clienteService, PdfReporteService pdfReporteService) {
        this.pagoService = pagoService;
        this.clienteService = clienteService;
        this.pdfReporteService = pdfReporteService;
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

    @GetMapping("/{id}/boleta-pdf")
    public ResponseEntity<InputStreamResource> descargarBoletaPdf(@PathVariable Long id) {
        Pago pago = pagoService.buscarPorId(id);
        if (pago == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayInputStream bis = pdfReporteService.generarBoletaPagoPdf(pago);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=boleta-pago-REC-" + pago.getId() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pagoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Pago eliminado");
        return "redirect:/pagos";
    }
}
