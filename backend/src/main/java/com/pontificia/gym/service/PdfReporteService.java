package com.pontificia.gym.service;

import com.pontificia.gym.entity.Pago;
import java.io.ByteArrayInputStream;

public interface PdfReporteService {
    ByteArrayInputStream generarBoletaPagoPdf(Pago pago);
}
