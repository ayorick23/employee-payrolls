package com.payrollsystem.dao;

import com.payrollsystem.models.Nomina;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class NominaDAO {
    
    public void crear(Nomina nomina) throws SQLException {
        String sql = """
            INSERT INTO nominas (empleado_id, periodo_mes, periodo_anio, salario_base, 
                               bonificaciones, horas_extra, descuento_isss, descuento_afp, 
                               descuento_isr, otros_descuentos, salario_neto, estado) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, nomina.getEmpleadoId());
            stmt.setInt(2, nomina.getPeriodoMes());
            stmt.setInt(3, nomina.getPeriodoAnio());
            stmt.setBigDecimal(4, nomina.getSalarioBase());
            stmt.setBigDecimal(5, nomina.getBonificaciones());
            stmt.setBigDecimal(6, nomina.getHorasExtra());
            stmt.setBigDecimal(7, nomina.getDescuentoIsss());
            stmt.setBigDecimal(8, nomina.getDescuentoAfp());
            stmt.setBigDecimal(9, nomina.getDescuentoIsr());
            stmt.setBigDecimal(10, nomina.getOtrosDescuentos());
            stmt.setBigDecimal(11, nomina.getSalarioNeto());
            stmt.setString(12, nomina.getEstado());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    nomina.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public List<Nomina> obtenerPorEmpleado(int empleadoId) throws SQLException {
        String sql = """
            SELECT n.*, e.nombre, e.apellido 
            FROM nominas n 
            JOIN empleados e ON n.empleado_id = e.id 
            WHERE n.empleado_id = ? 
            ORDER BY n.periodo_anio DESC, n.periodo_mes DESC
            """;
        
        List<Nomina> nominas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, empleadoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    nominas.add(mapearNomina(rs));
                }
            }
        }
        
        return nominas;
    }
    
    public List<Nomina> obtenerPorPeriodo(int mes, int anio) throws SQLException {
        String sql = """
            SELECT n.*, e.nombre, e.apellido 
            FROM nominas n 
            JOIN empleados e ON n.empleado_id = e.id 
            WHERE n.periodo_mes = ? AND n.periodo_anio = ? 
            ORDER BY e.apellido, e.nombre
            """;
        
        List<Nomina> nominas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mes);
            stmt.setInt(2, anio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    nominas.add(mapearNomina(rs));
                }
            }
        }
        
        return nominas;
    }
    
    public Nomina obtenerPorEmpleadoPeriodo(int empleadoId, int mes, int anio) throws SQLException {
        String sql = """
            SELECT n.*, e.nombre, e.apellido 
            FROM nominas n 
            JOIN empleados e ON n.empleado_id = e.id 
            WHERE n.empleado_id = ? AND n.periodo_mes = ? AND n.periodo_anio = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, empleadoId);
            stmt.setInt(2, mes);
            stmt.setInt(3, anio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearNomina(rs);
                }
            }
        }
        
        return null;
    }
    
    public void actualizar(Nomina nomina) throws SQLException {
        String sql = """
            UPDATE nominas SET 
                salario_base = ?, bonificaciones = ?, horas_extra = ?, 
                descuento_isss = ?, descuento_afp = ?, descuento_isr = ?, 
                otros_descuentos = ?, salario_neto = ?, estado = ? 
            WHERE id = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, nomina.getSalarioBase());
            stmt.setBigDecimal(2, nomina.getBonificaciones());
            stmt.setBigDecimal(3, nomina.getHorasExtra());
            stmt.setBigDecimal(4, nomina.getDescuentoIsss());
            stmt.setBigDecimal(5, nomina.getDescuentoAfp());
            stmt.setBigDecimal(6, nomina.getDescuentoIsr());
            stmt.setBigDecimal(7, nomina.getOtrosDescuentos());
            stmt.setBigDecimal(8, nomina.getSalarioNeto());
            stmt.setString(9, nomina.getEstado());
            stmt.setInt(10, nomina.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM nominas WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public List<Nomina> obtenerTodas() throws SQLException {
        String sql = """
            SELECT n.*, e.nombre, e.apellido 
            FROM nominas n 
            JOIN empleados e ON n.empleado_id = e.id 
            ORDER BY n.periodo_anio DESC, n.periodo_mes DESC, e.apellido, e.nombre
            """;
        
        List<Nomina> nominas = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                nominas.add(mapearNomina(rs));
            }
        }
        
        return nominas;
    }
    
    private Nomina mapearNomina(ResultSet rs) throws SQLException {
        Nomina nomina = new Nomina();
        nomina.setId(rs.getInt("id"));
        nomina.setEmpleadoId(rs.getInt("empleado_id"));
        nomina.setEmpleadoNombre(rs.getString("nombre") + " " + rs.getString("apellido"));
        nomina.setPeriodoMes(rs.getInt("periodo_mes"));
        nomina.setPeriodoAnio(rs.getInt("periodo_anio"));
        nomina.setSalarioBase(rs.getBigDecimal("salario_base"));
        nomina.setBonificaciones(rs.getBigDecimal("bonificaciones"));
        nomina.setHorasExtra(rs.getBigDecimal("horas_extra"));
        nomina.setDescuentoIsss(rs.getBigDecimal("descuento_isss"));
        nomina.setDescuentoAfp(rs.getBigDecimal("descuento_afp"));
        nomina.setDescuentoIsr(rs.getBigDecimal("descuento_isr"));
        nomina.setOtrosDescuentos(rs.getBigDecimal("otros_descuentos"));
        nomina.setSalarioNeto(rs.getBigDecimal("salario_neto"));
        nomina.setFechaCalculo(rs.getTimestamp("fecha_calculo").toLocalDateTime());
        nomina.setEstado(rs.getString("estado"));
        
        return nomina;
    }
    
    // Métodos adicionales para reportes
    public BigDecimal obtenerTotalNominasPorPeriodo(int mes, int anio) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(salario_neto), 0) as total 
            FROM nominas 
            WHERE periodo_mes = ? AND periodo_anio = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mes);
            stmt.setInt(2, anio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        }
        
        return BigDecimal.ZERO;
    }
    
    public int contarNominasPendientes() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM nominas WHERE estado = 'CALCULADA'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        
        return 0;
    }
}