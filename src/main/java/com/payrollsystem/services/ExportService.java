package com.payrollsystem.services;

import com.opencsv.CSVWriter;
import com.payrollsystem.models.Empleado;
import com.payrollsystem.models.Nomina;
import com.payrollsystem.dao.EmpleadoDAO;
import com.payrollsystem.dao.NominaDAO;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportService {
    
    private EmpleadoDAO empleadoDAO;
    private NominaDAO nominaDAO;
    
    public ExportService() {
        this.empleadoDAO = new EmpleadoDAO();
        this.nominaDAO = new NominaDAO();
    }
    
    public void exportarEmpleadosCSV(String rutaArchivo) throws Exception {
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(rutaArchivo))) {
            // Escribir encabezados
            String[] headers = {"Codigo", "Nombre", "Apellido", "Email", "Telefono", 
                              "Direccion", "FechaIngreso", "FechaNacimiento", "DepartamentoId", 
                              "SalarioBase", "TipoContrato", "NumeroCuenta", "Banco", "Estado"};
            writer.writeNext(headers);
            
            // Escribir datos
            for (Empleado empleado : empleados) {
                String[] datos = {
                    empleado.getCodigo(),
                    empleado.getNombre(),
                    empleado.getApellido(),
                    empleado.getEmail(),
                    empleado.getTelefono(),
                    empleado.getDireccion(),
                    empleado.getFechaIngreso().toString(),
                    empleado.getFechaNacimiento() != null ? empleado.getFechaNacimiento().toString() : "",
                    String.valueOf(empleado.getDepartamentoId()),
                    empleado.getSalarioBase().toString(),
                    empleado.getTipoContrato(),
                    empleado.getNumeroCuenta(),
                    empleado.getBanco(),
                    empleado.getEstado()
                };
                writer.writeNext(datos);
            }
        }
    }
    
    public void exportarNominasCSV(int mes, int anio, String rutaArchivo) throws Exception {
        List<Nomina> nominas = nominaDAO.obtenerPorPeriodo(mes, anio);
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(rutaArchivo))) {
            // Escribir encabezados
            String[] headers = {"EmpleadoNombre", "PeriodoMes", "PeriodoAnio", "SalarioBase", 
                              "Bonificaciones", "HorasExtra", "DescuentoISSS", "DescuentoAFP", 
                              "DescuentoISR", "OtrosDescuentos", "SalarioNeto", "Estado"};
            writer.writeNext(headers);
            
            // Escribir datos
            for (Nomina nomina : nominas) {
                String[] datos = {
                    nomina.getEmpleadoNombre(),
                    String.valueOf(nomina.getPeriodoMes()),
                    String.valueOf(nomina.getPeriodoAnio()),
                    nomina.getSalarioBase().toString(),
                    nomina.getBonificaciones().toString(),
                    nomina.getHorasExtra().toString(),
                    nomina.getDescuentoIsss().toString(),
                    nomina.getDescuentoAfp().toString(),
                    nomina.getDescuentoIsr().toString(),
                    nomina.getOtrosDescuentos().toString(),
                    nomina.getSalarioNeto().toString(),
                    nomina.getEstado()
                };
                writer.writeNext(datos);
            }
        }
    }
}