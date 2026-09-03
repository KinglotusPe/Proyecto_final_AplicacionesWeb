package com.pontificia.gym.service;

import com.pontificia.gym.dto.CajaResumenDto;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

public interface AuditoriaCajaService {
    CajaResumenDto generarResumenCaja(LocalDate fechaInicio, LocalDate fechaFin, String usernameCajero);
    ByteArrayInputStream generarExcelReporte(CajaResumenDto resumen);
}
