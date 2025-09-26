package com.payrollsystem.services;

import com.payrollsystem.models.Empleado;
import com.payrollsystem.models.Nomina;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoNominaService {
    
    // Constantes de El Salvador
    private static final BigDecimal TASA_ISSS = new BigDecimal("0.0525"); // 5.25%
    private static final BigDecimal MAXIMO_ISSS = new BigDecimal("1000.00");
    private static final BigDecimal TASA_AFP = new BigDecimal("0.0775"); // 7.75%
    private static final BigDecimal TASA_ISR = new BigDecimal("0.30"); // 30%
    private static final BigDecimal MINIMO_ISR = new BigDecimal("472.00");
    
    public void calcularNomina(Nomina nomina) {
        if (nomina == null || nomina.getSalarioBase() == null) {
            throw new IllegalArgumentException("Datos de nómina inválidos");
        }
        
        // Calcular salario bruto
        BigDecimal salarioBruto = nomina.getSalarioBase()
            .add(nomina.getBonificaciones())
            .add(nomina.getHorasExtra());
        
        // Calcular ISSS
        BigDecimal descuentoIsss = calcularISS(salarioBruto);
        nomina.setDescuentoIsss(descuentoIsss);
        
        // Calcular AFP
        BigDecimal descuentoAfp = calcularAFP(salarioBruto);
        nomina.setDescuentoAfp(descuentoAfp);
        
        // Calcular ISR
        BigDecimal baseISR = salarioBruto.subtract(descuentoIsss).subtract(descuentoAfp);
        BigDecimal descuentoIsr = calcularISR(baseISR);
        nomina.setDescuentoIsr(descuentoIsr);
        
        // Calcular salario neto
        BigDecimal salarioNeto = salarioBruto
            .subtract(descuentoIsss)
            .subtract(descuentoAfp)
            .subtract(descuentoIsr)
            .subtract(nomina.getOtrosDescuentos());
        
        nomina.setSalarioNeto(salarioNeto);
    }
    
    public void calcularNomina(Nomina nomina, Empleado empleado) {
        calcularNomina(nomina);
    }
    
    private BigDecimal calcularISS(BigDecimal salarioBruto) {
        if (salarioBruto.compareTo(MAXIMO_ISSS) > 0) {
            return MAXIMO_ISSS.multiply(TASA_ISSS).setScale(2, RoundingMode.HALF_UP);
        }
        return salarioBruto.multiply(TASA_ISSS).setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calcularAFP(BigDecimal salarioBruto) {
        return salarioBruto.multiply(TASA_AFP).setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calcularISR(BigDecimal baseISR) {
        if (baseISR.compareTo(MINIMO_ISR) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal exceso = baseISR.subtract(MINIMO_ISR);
        return exceso.multiply(TASA_ISR).setScale(2, RoundingMode.HALF_UP);
    }
    
    // Métodos de utilidad
    public BigDecimal calcularHorasExtra(BigDecimal salarioBase, int horasExtra, double factorExtra) {
        BigDecimal salarioPorHora = salarioBase.divide(new BigDecimal("240"), 4, RoundingMode.HALF_UP); // 30 días * 8 horas
        BigDecimal valorHoraExtra = salarioPorHora.multiply(new BigDecimal(factorExtra));
        return valorHoraExtra.multiply(new BigDecimal(horasExtra)).setScale(2, RoundingMode.HALF_UP);
    }
    
    public double calcularPorcentajeDescuentos(Nomina nomina) {
        if (nomina.getSalarioBruto().compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        
        BigDecimal totalDescuentos = nomina.getTotalDescuentos();
        return totalDescuentos.divide(nomina.getSalarioBruto(), 4, RoundingMode.HALF_UP)
                             .multiply(new BigDecimal("100"))
                             .doubleValue();
    }
}