package com.payrollsystem.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMAT) : "";
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMAT) : "";
    }
    
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static int calcularAnios(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return 0;
        }
        return (int) ChronoUnit.YEARS.between(fechaInicio, fechaFin);
    }
    
    public static int calcularAniosDeServicio(LocalDate fechaIngreso) {
        return calcularAnios(fechaIngreso, LocalDate.now());
    }
    
    public static int calcularEdad(LocalDate fechaNacimiento) {
        return calcularAnios(fechaNacimiento, LocalDate.now());
    }
    
    public static boolean esMismoMes(LocalDate fecha1, LocalDate fecha2) {
        return fecha1 != null && fecha2 != null &&
               fecha1.getMonthValue() == fecha2.getMonthValue() &&
               fecha1.getYear() == fecha2.getYear();
    }
    
    public static String getNombreMes(int mes) {
        String[] meses = {
            "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        
        return (mes >= 1 && mes <= 12) ? meses[mes] : "";
    }
}