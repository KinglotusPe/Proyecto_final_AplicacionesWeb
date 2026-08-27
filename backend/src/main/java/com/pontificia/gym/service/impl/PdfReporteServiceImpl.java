package com.pontificia.gym.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.pontificia.gym.entity.Pago;
import com.pontificia.gym.service.PdfReporteService;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfReporteServiceImpl implements PdfReporteService {

    @Override
    public ByteArrayInputStream generarBoletaPagoPdf(Pago pago) {
        Document document = new Document(PageSize.A5, 30, 30, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Encabezado de la Empresa
            Paragraph header = new Paragraph("BRUTAL FITNESS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(220, 53, 69)));
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph subheader = new Paragraph("SISTEMA DE GESTIÓN INTEGRAL PARA GIMNASIOS\nJirón José Santos Chocano - Jesús Nazareno, Ayacucho\nRUC: 20608945123 | Teléfono: 987-654-321", 
                    FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY));
            subheader.setAlignment(Element.ALIGN_CENTER);
            subheader.setSpacingAfter(15);
            document.add(subheader);

            // 2. Título del Recibo
            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setWidthPercentage(100);
            PdfPCell titleCell = new PdfPCell(new Phrase("COMPROBANTE DE PAGO N° REC-" + String.format("%06d", pago.getId()), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE)));
            titleCell.setBackgroundColor(new Color(17, 24, 39));
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setPadding(6);
            titleTable.addCell(titleCell);
            titleTable.setSpacingAfter(15);
            document.add(titleTable);

            // 3. Datos del Cliente y Emisión
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1.2f, 2.8f});

            addInfoRow(infoTable, "Fecha de Emisión:", pago.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            addInfoRow(infoTable, "Cliente / Socio:", pago.getCliente().getNombreCompleto());
            addInfoRow(infoTable, "DNI del Cliente:", pago.getCliente().getDni());
            if (pago.getCliente().getTelefono() != null) {
                addInfoRow(infoTable, "Teléfono:", pago.getCliente().getTelefono());
            }
            addInfoRow(infoTable, "Método de Pago:", pago.getMetodoPago().name());
            if (pago.getProximaFechaPago() != null) {
                addInfoRow(infoTable, "Próximo Vencimiento:", pago.getProximaFechaPago().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            infoTable.setSpacingAfter(15);
            document.add(infoTable);

            // 4. Detalle del Cobro
            PdfPTable tableDetalle = new PdfPTable(3);
            tableDetalle.setWidthPercentage(100);
            tableDetalle.setWidths(new float[]{3.5f, 1.5f, 1.5f});

            // Cabeceras
            addHeaderCell(tableDetalle, "Descripción / Concepto");
            addHeaderCell(tableDetalle, "Cant.");
            addHeaderCell(tableDetalle, "Total");

            // Fila de concepto
            addBodyCell(tableDetalle, "Cuota de Membresía / Acceso a Gimnasio", Element.ALIGN_LEFT);
            addBodyCell(tableDetalle, "1", Element.ALIGN_CENTER);
            addBodyCell(tableDetalle, "S/ " + String.format("%.2f", pago.getMonto()), Element.ALIGN_RIGHT);

            tableDetalle.setSpacingAfter(20);
            document.add(tableDetalle);

            // 5. Total a Pagar Destacado
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[]{3.5f, 2.5f});

            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setBorder(Rectangle.NO_BORDER);
            totalTable.addCell(emptyCell);

            PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL PAGADO: S/ " + String.format("%.2f", pago.getMonto()), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(220, 53, 69))));
            totalCell.setBackgroundColor(new Color(245, 245, 245));
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalCell.setPadding(8);
            totalTable.addCell(totalCell);
            totalTable.setSpacingAfter(25);
            document.add(totalTable);

            // 6. Mensaje de Pie
            Paragraph footer = new Paragraph("¡Gracias por entrenar con nosotros en BRUTAL FITNESS!\nConserva este comprobante para cualquier consulta o trámite en recepción.", 
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Color.DARK_GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.DARK_GRAY)));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPadding(3);
        table.addCell(cellLabel);

        PdfPCell cellVal = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK)));
        cellVal.setBorder(Rectangle.NO_BORDER);
        cellVal.setPadding(3);
        table.addCell(cellVal);
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE)));
        cell.setBackgroundColor(new Color(30, 41, 59));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK)));
        cell.setHorizontalAlignment(align);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
