package com.payrollsystem.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.payrollsystem.models.Empleado;
import com.payrollsystem.models.Nomina;
import com.payrollsystem.dao.EmpleadoDAO;
import com.payrollsystem.dao.NominaDAO;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportService {
    
    private EmpleadoDAO empleadoDAO;
    private NominaDAO nominaDAO;
    
    public ReportService() {
        this.empleadoDAO = new EmpleadoDAO();
        this.nominaDAO = new NominaDAO();
    }
    
    public void generarReporteNominaPDF(int mes, int anio, String rutaArchivo) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
        
        document.open();
        
        // Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("REPORTE DE NÓMINA - " + mes + "/" + anio, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Información de generación
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Paragraph info = new Paragraph("Generado: " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), infoFont);
        info.setAlignment(Element.ALIGN_RIGHT);
        info.setSpacingAfter(20);
        document.add(info);
        
        // Tabla de nóminas
        List<Nomina> nominas = nominaDAO.obtenerPorPeriodo(mes, anio);
        
        if (nominas.isEmpty()) {
            document.add(new Paragraph("No se encontraron nóminas para el período especificado."));
        } else {
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 3, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 2});
            
            // Encabezados
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            addTableHeader(table, headerFont, "Empleado");
            addTableHeader(table, headerFont, "Salario Base");
            addTableHeader(table, headerFont, "Bonificaciones");
            addTableHeader(table, headerFont, "ISSS");
            addTableHeader(table, headerFont, "AFP");
            addTableHeader(table, headerFont, "ISR");
            addTableHeader(table, headerFont, "Otros Desc.");
            addTableHeader(table, headerFont, "Salario Neto");
            
            // Datos
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            BigDecimal totalSalarios = BigDecimal.ZERO;
            BigDecimal totalDescuentos = BigDecimal.ZERO;
            BigDecimal totalNeto = BigDecimal.ZERO;
            
            for (Nomina nomina : nominas) {
                addTableCell(table, dataFont, nomina.getEmpleadoNombre());
                addTableCell(table, dataFont, "$" + nomina.getSalarioBase().toString());
                addTableCell(table, dataFont, "$" + nomina.getBonificaciones().toString());
                addTableCell(table, dataFont, "$" + nomina.getDescuentoIsss().toString());
                addTableCell(table, dataFont, "$" + nomina.getDescuentoAfp().toString());
                addTableCell(table, dataFont, "$" + nomina.getDescuentoIsr().toString());
                addTableCell(table, dataFont, "$" + nomina.getOtrosDescuentos().toString());
                addTableCell(table, dataFont, "$" + nomina.getSalarioNeto().toString());
                
                totalSalarios = totalSalarios.add(nomina.getSalarioBase()).add(nomina.getBonificaciones());
                totalDescuentos = totalDescuentos.add(nomina.getTotalDescuentos());
                totalNeto = totalNeto.add(nomina.getSalarioNeto());
            }
            
            // Fila de totales
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            addTableCell(table, totalFont, "TOTALES:");
            addTableCell(table, totalFont, "$" + totalSalarios.toString());
            addTableCell(table, totalFont, "");
            addTableCell(table, totalFont, "");
            addTableCell(table, totalFont, "");
            addTableCell(table, totalFont, "");
            addTableCell(table, totalFont, "$" + totalDescuentos.toString());
            addTableCell(table, totalFont, "$" + totalNeto.toString());
            
            document.add(table);
            
            // Resumen
            document.add(new Paragraph("\n"));
            Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            document.add(new Paragraph("RESUMEN:", summaryFont));
            document.add(new Paragraph("Total empleados: " + nominas.size()));
            document.add(new Paragraph("Total salarios brutos: $" + totalSalarios));
            document.add(new Paragraph("Total descuentos: $" + totalDescuentos));
            document.add(new Paragraph("Total salarios netos: $" + totalNeto));
        }
        
        document.close();
    }
    
    public void generarReporteEmpleadosExcel(String rutaArchivo) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Empleados");
        
        // Crear estilo para encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Crear fila de encabezados
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Código", "Nombre", "Apellido", "Email", "Teléfono", 
                           "Departamento", "Salario Base", "Estado", "Fecha Ingreso"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Agregar datos de empleados
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        int rowNum = 1;
        
        for (Empleado empleado : empleados) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(empleado.getCodigo());
            row.createCell(1).setCellValue(empleado.getNombre());
            row.createCell(2).setCellValue(empleado.getApellido());
            row.createCell(3).setCellValue(empleado.getEmail());
            row.createCell(4).setCellValue(empleado.getTelefono());
            row.createCell(5).setCellValue(empleado.getDepartamentoNombre());
            row.createCell(6).setCellValue(empleado.getSalarioBase().doubleValue());
            row.createCell(7).setCellValue(empleado.getEstado());
            row.createCell(8).setCellValue(empleado.getFechaIngreso().toString());
        }
        
        // Ajustar ancho de columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Guardar archivo
        try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
            workbook.write(fileOut);
        }
        
        workbook.close();
    }
    
    private void addTableHeader(PdfPTable table, Font font, String text) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(text, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setPadding(5);
        table.addCell(header);
    }
    
    private void addTableCell(PdfPTable table, Font font, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(3);
        cell.setBorderWidth(1);
        table.addCell(cell);
    }
}