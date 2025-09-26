package com.payrollsystem.services;

import com.payrollsystem.models.Empleado;
import com.payrollsystem.dao.EmpleadoDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class NotificationService {
    private EmpleadoDAO empleadoDAO;
    
    public NotificationService() {
        this.empleadoDAO = new EmpleadoDAO();
    }
    
    public List<String> obtenerNotificaciones() {
        List<String> notificaciones = new ArrayList<>();
        
        try {
            // Cumpleaños del mes
            List<Empleado> cumpleanos = empleadoDAO.obtenerCumpleaniosMes(LocalDate.now().getMonthValue());
            if (!cumpleanos.isEmpty()) {
                notificaciones.add("🎂 " + cumpleanos.size() + " empleados cumplen años este mes:");
                for (Empleado emp : cumpleanos) {
                    notificaciones.add("  - " + emp.getNombreCompleto() + 
                        " (" + emp.getFechaNacimiento().getDayOfMonth() + "/" + 
                        emp.getFechaNacimiento().getMonthValue() + ")");
                }
            }
            
            // Aniversarios de trabajo
            List<Empleado> aniversarios = obtenerAniversariosLaboral();
            if (!aniversarios.isEmpty()) {
                notificaciones.add("🏆 Aniversarios laborales este mes:");
                for (Empleado emp : aniversarios) {
                    int anos = LocalDate.now().getYear() - emp.getFechaIngreso().getYear();
                    notificaciones.add("  - " + emp.getNombreCompleto() + " (" + anos + " años)");
                }
            }
            
            // Contratos por vencer (ejemplo)
            notificaciones.add("📋 Sistema actualizado correctamente");
            
        } catch (Exception e) {
            notificaciones.add("⚠️ Error cargando notificaciones");
        }
        
        return notificaciones;
    }
    
    private List<Empleado> obtenerAniversariosLaboral() throws Exception {
        List<Empleado> todos = empleadoDAO.obtenerTodos();
        List<Empleado> aniversarios = new ArrayList<>();
        
        int mesActual = LocalDate.now().getMonthValue();
        
        for (Empleado emp : todos) {
            if (emp.getFechaIngreso().getMonthValue() == mesActual) {
                aniversarios.add(emp);
            }
        }
        
        return aniversarios;
    }
}