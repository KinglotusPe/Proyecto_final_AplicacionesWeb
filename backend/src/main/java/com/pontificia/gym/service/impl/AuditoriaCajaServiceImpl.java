package com.pontificia.gym.service.impl;

import com.pontificia.gym.dto.CajaResumenDto;
import com.pontificia.gym.dto.CajaTransaccionDto;
import com.pontificia.gym.entity.Pago;
import com.pontificia.gym.entity.Venta;
import com.pontificia.gym.entity.VentaDetalle;
import com.pontificia.gym.repository.PagoRepository;
import com.pontificia.gym.repository.VentaRepository;
import com.pontificia.gym.service.AuditoriaCajaService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuditoriaCajaServiceImpl implements AuditoriaCajaService {

    private final VentaRepository ventaRepository;
    private final PagoRepository pagoRepository;

    public AuditoriaCajaServiceImpl(VentaRepository ventaRepository, PagoRepository pagoRepository) {
        this.ventaRepository = ventaRepository;
        this.pagoRepository = pagoRepository;
    }

    @Override
    public CajaResumenDto generarResumenCaja(LocalDate fechaInicio, LocalDate fechaFin, String usernameCajero) {
        if (fechaInicio == null) {
            fechaInicio = LocalDate.now();
        }
        if (fechaFin == null) {
            fechaFin = LocalDate.now();
        }

        LocalDateTime startDt = fechaInicio.atStartOfDay();
        LocalDateTime endDt = fechaFin.atTime(23, 59, 59);

        CajaResumenDto resumen = new CajaResumenDto();
        resumen.setFechaInicio(fechaInicio);
        resumen.setFechaFin(fechaFin);
        resumen.setCajeroFiltro(usernameCajero);

        List<CajaTransaccionDto> listaTransacciones = new ArrayList<>();

        // 1. Obtener Ventas de Tienda
        List<Venta> ventas = ventaRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(startDt, endDt);
        for (Venta v : ventas) {
            String cajero = v.getUsuario() != null ? v.getUsuario().getUsername() : "recepcion";

            // Si hay filtro de cajero y no coincide, omitir
            if (usernameCajero != null && !usernameCajero.trim().isEmpty() && !cajero.equalsIgnoreCase(usernameCajero.trim())) {
                continue;
            }

            String clienteNombre = v.getCliente() != null ? v.getCliente().getNombreCompleto() : "Público General";
            String concepto = v.getDetalles().stream()
                    .map(d -> d.getCantidad() + "x " + d.getProducto().getNombre())
                    .collect(Collectors.joining(", "));

            if (concepto.isEmpty()) {
                concepto = "Venta Mostrador";
            }

            CajaTransaccionDto dto = new CajaTransaccionDto(
                    "TIENDA",
                    v.getCodigoComprobante(),
                    v.getFechaHora(),
                    clienteNombre,
                    concepto,
                    v.getUsuario() != null ? v.getUsuario().getNombre() : "Recepción",
                    v.getMetodoPago(),
                    v.getTotal()
            );

            listaTransacciones.add(dto);
            resumen.setTotalTienda(resumen.getTotalTienda().add(v.getTotal()));
            acumularPorMetodo(resumen, v.getMetodoPago(), v.getTotal());
        }

        // 2. Obtener Pagos de Membresías (Cobros de Suscripciones)
        List<Pago> pagos = pagoRepository.findByFechaBetween(fechaInicio, fechaFin);
        for (Pago p : pagos) {
            String clienteNombre = p.getCliente() != null ? p.getCliente().getNombreCompleto() : "Socio Gimnasio";
            String metodo = p.getMetodoPago() != null ? p.getMetodoPago().name() : "EFECTIVO";

            CajaTransaccionDto dto = new CajaTransaccionDto(
                    "SUSCRIPCION",
                    "REC-" + String.format("%06d", p.getId()),
                    p.getFecha().atTime(12, 0, 0),
                    clienteNombre,
                    "Cuota Membresía Deportiva",
                    "Caja / Recepción",
                    metodo,
                    p.getMonto()
            );

            listaTransacciones.add(dto);
            resumen.setTotalSuscripciones(resumen.getTotalSuscripciones().add(p.getMonto()));
            acumularPorMetodo(resumen, metodo, p.getMonto());
        }

        // Ordenar todas las transacciones por fecha descendente
        listaTransacciones.sort(Comparator.comparing(CajaTransaccionDto::getFechaHora).reversed());

        resumen.setTotalGeneral(resumen.getTotalSuscripciones().add(resumen.getTotalTienda()));
        resumen.setCantidadTransacciones(listaTransacciones.size());
        resumen.setTransacciones(listaTransacciones);

        return resumen;
    }

    private void acumularPorMetodo(CajaResumenDto resumen, String metodo, BigDecimal monto) {
        if (metodo == null || monto == null) return;
        String m = metodo.toUpperCase();
        if (m.contains("EFECTIVO")) {
            resumen.setTotalEfectivo(resumen.getTotalEfectivo().add(monto));
        } else if (m.contains("YAPE") || m.contains("PLIN")) {
            resumen.setTotalYapePlin(resumen.getTotalYapePlin().add(monto));
        } else if (m.contains("TARJETA")) {
            resumen.setTotalTarjeta(resumen.getTotalTarjeta().add(monto));
        } else {
            resumen.setTotalTransferencia(resumen.getTotalTransferencia().add(monto));
        }
    }

    @Override
    public ByteArrayInputStream generarExcelReporte(CajaResumenDto resumen) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Arqueo de Caja");

            // Estilos
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);

            // Título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BRUTAL FITNESS - REPORTE DE AUDITORÍA Y ARQUEO DE CAJA");
            titleCell.setCellStyle(titleStyle);

            Row subtitleRow = sheet.createRow(1);
            subtitleRow.createCell(0).setCellValue("Periodo: " + resumen.getFechaInicio() + " al " + resumen.getFechaFin() + " | Total Recaudado: S/ " + resumen.getTotalGeneral());

            // Cabeceras de tabla
            String[] columns = {"#", "Tipo", "N° Comprobante", "Fecha y Hora", "Cliente / Socio", "Concepto / Detalle", "Atendido Por", "Método de Pago", "Monto (S/)"};
            Row rowHeader = sheet.createRow(3);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = rowHeader.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            int rowIndex = 4;
            int itemNum = 1;

            for (CajaTransaccionDto t : resumen.getTransacciones()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(itemNum++);
                row.createCell(1).setCellValue(t.getTipo().equals("TIENDA") ? "🛍️ TIENDA" : "🎟️ SUSCRIPCIÓN");
                row.createCell(2).setCellValue(t.getComprobante());
                row.createCell(3).setCellValue(t.getFechaHora().format(dtf));
                row.createCell(4).setCellValue(t.getCliente());
                row.createCell(5).setCellValue(t.getConcepto());
                row.createCell(6).setCellValue(t.getAtendidoPor());
                row.createCell(7).setCellValue(t.getMetodoPago());
                row.createCell(8).setCellValue(t.getMonto().doubleValue());
            }

            // Fila de Total
            Row totalRow = sheet.createRow(rowIndex + 1);
            Cell lblTotal = totalRow.createCell(7);
            lblTotal.setCellValue("TOTAL GENERAL:");
            Cell valTotal = totalRow.createCell(8);
            valTotal.setCellValue(resumen.getTotalGeneral().doubleValue());

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel: " + e.getMessage(), e);
        }
    }
}
