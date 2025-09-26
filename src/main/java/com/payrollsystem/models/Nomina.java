package com.payrollsystem.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Nomina {
    private int id;
    private int empleadoId;
    private String empleadoNombre;
    private int periodoMes;
    private int periodoAnio;
    private BigDecimal salarioBase;
    private BigDecimal bonificaciones;
    private BigDecimal horasExtra;
    private BigDecimal descuentoIsss;
    private BigDecimal descuentoAfp;
    private BigDecimal descuentoIsr;
    private BigDecimal otrosDescuentos;
    private BigDecimal salarioNeto;
    private LocalDateTime fechaCalculo;
    private String estado;
    
    // Constructores
    public Nomina() {
        this.bonificaciones = BigDecimal.ZERO;
        this.horasExtra = BigDecimal.ZERO;
        this.descuentoIsss = BigDecimal.ZERO;
        this.descuentoAfp = BigDecimal.ZERO;
        this.descuentoIsr = BigDecimal.ZERO;
        this.otrosDescuentos = BigDecimal.ZERO;
        this.salarioNeto = BigDecimal.ZERO;
        this.fechaCalculo = LocalDateTime.now();
        this.estado = "CALCULADA";
    }
    
    public Nomina(int empleadoId, int periodoMes, int periodoAnio, BigDecimal salarioBase) {
        this();
        this.empleadoId = empleadoId;
        this.periodoMes = periodoMes;
        this.periodoAnio = periodoAnio;
        this.salarioBase = salarioBase;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }
    
    public String getEmpleadoNombre() { return empleadoNombre; }
    public void setEmpleadoNombre(String empleadoNombre) { this.empleadoNombre = empleadoNombre; }
    
    public int getPeriodoMes() { return periodoMes; }
    public void setPeriodoMes(int periodoMes) { this.periodoMes = periodoMes; }
    
    public int getPeriodoAnio() { return periodoAnio; }
    public void setPeriodoAnio(int periodoAnio) { this.periodoAnio = periodoAnio; }
    
    public BigDecimal getSalarioBase() { return salarioBase; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase; }
    
    public BigDecimal getBonificaciones() { return bonificaciones; }
    public void setBonificaciones(BigDecimal bonificaciones) { this.bonificaciones = bonificaciones; }
    
    public BigDecimal getHorasExtra() { return horasExtra; }
    public void setHorasExtra(BigDecimal horasExtra) { this.horasExtra = horasExtra; }
    
    public BigDecimal getDescuentoIsss() { return descuentoIsss; }
    public void setDescuentoIsss(BigDecimal descuentoIsss) { this.descuentoIsss = descuentoIsss; }
    
    public BigDecimal getDescuentoAfp() { return descuentoAfp; }
    public void setDescuentoAfp(BigDecimal descuentoAfp) { this.descuentoAfp = descuentoAfp; }
    
    public BigDecimal getDescuentoIsr() { return descuentoIsr; }
    public void setDescuentoIsr(BigDecimal descuentoIsr) { this.descuentoIsr = descuentoIsr; }
    
    public BigDecimal getOtrosDescuentos() { return otrosDescuentos; }
    public void setOtrosDescuentos(BigDecimal otrosDescuentos) { this.otrosDescuentos = otrosDescuentos; }
    
    public BigDecimal getSalarioNeto() { return salarioNeto; }
    public void setSalarioNeto(BigDecimal salarioNeto) { this.salarioNeto = salarioNeto; }
    
    public LocalDateTime getFechaCalculo() { return fechaCalculo; }
    public void setFechaCalculo(LocalDateTime fechaCalculo) { this.fechaCalculo = fechaCalculo; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    // Métodos de utilidad
    public BigDecimal getTotalDescuentos() {
        return descuentoIsss.add(descuentoAfp).add(descuentoIsr).add(otrosDescuentos);
    }
    
    public BigDecimal getSalarioBruto() {
        return salarioBase.add(bonificaciones).add(horasExtra);
    }
    
    public String getPeriodoTexto() {
        return String.format("%02d/%d", periodoMes, periodoAnio);
    }
}
