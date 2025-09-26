package com.payrollsystem.dao;

import com.payrollsystem.models.Empleado;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class EmpleadoDAO {
    
    public void crear(Empleado empleado) throws SQLException {
        String sql = """
            INSERT INTO empleados (codigo, nombre, apellido, email, telefono, direccion, 
                                 fecha_ingreso, fecha_nacimiento, departamento_id, salario_base, 
                                 tipo_contrato, numero_cuenta, banco, estado) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, empleado.getCodigo());
            stmt.setString(2, empleado.getNombre());
            stmt.setString(3, empleado.getApellido());
            stmt.setString(4, empleado.getEmail());
            stmt.setString(5, empleado.getTelefono());
            stmt.setString(6, empleado.getDireccion());
            stmt.setDate(7, Date.valueOf(empleado.getFechaIngreso()));
            stmt.setDate(8, empleado.getFechaNacimiento() != null ? 
                        Date.valueOf(empleado.getFechaNacimiento()) : null);
            stmt.setInt(9, empleado.getDepartamentoId());
            stmt.setBigDecimal(10, empleado.getSalarioBase());
            stmt.setString(11, empleado.getTipoContrato());
            stmt.setString(12, empleado.getNumeroCuenta());
            stmt.setString(13, empleado.getBanco());
            stmt.setString(14, empleado.getEstado());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    empleado.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public List<Empleado> obtenerTodos() throws SQLException {
        String sql = """
            SELECT e.*, d.nombre as departamento_nombre 
            FROM empleados e 
            LEFT JOIN departamentos d ON e.departamento_id = d.id 
            ORDER BY e.nombre, e.apellido
            """;
        
        List<Empleado> empleados = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
        }
        
        return empleados;
    }
    
    public Empleado obtenerPorId(int id) throws SQLException {
        String sql = """
            SELECT e.*, d.nombre as departamento_nombre 
            FROM empleados e 
            LEFT JOIN departamentos d ON e.departamento_id = d.id 
            WHERE e.id = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        }
        return null;
    }
    
    public List<Empleado> buscar(String nombre, String departamento, BigDecimal salarioMin, BigDecimal salarioMax) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT e.*, d.nombre as departamento_nombre 
            FROM empleados e 
            LEFT JOIN departamentos d ON e.departamento_id = d.id 
            WHERE 1=1
            """);
        
        List<Object> params = new ArrayList<>();
        
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND (e.nombre LIKE ? OR e.apellido LIKE ? OR e.codigo LIKE ?)");
            String searchParam = "%" + nombre.trim() + "%";
            params.add(searchParam);
            params.add(searchParam);
            params.add(searchParam);
        }
        
        if (departamento != null && !departamento.trim().isEmpty()) {
            sql.append(" AND d.nombre LIKE ?");
            params.add("%" + departamento.trim() + "%");
        }
        
        if (salarioMin != null) {
            sql.append(" AND e.salario_base >= ?");
            params.add(salarioMin);
        }
        
        if (salarioMax != null) {
            sql.append(" AND e.salario_base <= ?");
            params.add(salarioMax);
        }
        
        sql.append(" ORDER BY e.nombre, e.apellido");
        
        List<Empleado> empleados = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    empleados.add(mapearEmpleado(rs));
                }
            }
        }
        
        return empleados;
    }
    
    public void actualizar(Empleado empleado) throws SQLException {
        String sql = """
            UPDATE empleados SET 
                codigo = ?, nombre = ?, apellido = ?, email = ?, telefono = ?, 
                direccion = ?, fecha_ingreso = ?, fecha_nacimiento = ?, 
                departamento_id = ?, salario_base = ?, tipo_contrato = ?, 
                numero_cuenta = ?, banco = ?, estado = ?
            WHERE id = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, empleado.getCodigo());
            stmt.setString(2, empleado.getNombre());
            stmt.setString(3, empleado.getApellido());
            stmt.setString(4, empleado.getEmail());
            stmt.setString(5, empleado.getTelefono());
            stmt.setString(6, empleado.getDireccion());
            stmt.setDate(7, Date.valueOf(empleado.getFechaIngreso()));
            stmt.setDate(8, empleado.getFechaNacimiento() != null ? 
                        Date.valueOf(empleado.getFechaNacimiento()) : null);
            stmt.setInt(9, empleado.getDepartamentoId());
            stmt.setBigDecimal(10, empleado.getSalarioBase());
            stmt.setString(11, empleado.getTipoContrato());
            stmt.setString(12, empleado.getNumeroCuenta());
            stmt.setString(13, empleado.getBanco());
            stmt.setString(14, empleado.getEstado());
            stmt.setInt(15, empleado.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void eliminar(int id) throws SQLException {
        String sql = "UPDATE empleados SET estado = 'INACTIVO' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setId(rs.getInt("id"));
        empleado.setCodigo(rs.getString("codigo"));
        empleado.setNombre(rs.getString("nombre"));
        empleado.setApellido(rs.getString("apellido"));
        empleado.setEmail(rs.getString("email"));
        empleado.setTelefono(rs.getString("telefono"));
        empleado.setDireccion(rs.getString("direccion"));
        empleado.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
        
        Date fechaNac = rs.getDate("fecha_nacimiento");
        if (fechaNac != null) {
            empleado.setFechaNacimiento(fechaNac.toLocalDate());
        }
        
        empleado.setDepartamentoId(rs.getInt("departamento_id"));
        empleado.setDepartamentoNombre(rs.getString("departamento_nombre"));
        empleado.setSalarioBase(rs.getBigDecimal("salario_base"));
        empleado.setTipoContrato(rs.getString("tipo_contrato"));
        empleado.setNumeroCuenta(rs.getString("numero_cuenta"));
        empleado.setBanco(rs.getString("banco"));
        empleado.setEstado(rs.getString("estado"));
        empleado.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        
        return empleado;
    }
    
    public List<Empleado> obtenerCumpleaniosMes(int mes) throws SQLException {
        String sql = """
            SELECT e.*, d.nombre as departamento_nombre 
            FROM empleados e 
            LEFT JOIN departamentos d ON e.departamento_id = d.id 
            WHERE MONTH(e.fecha_nacimiento) = ? AND e.estado = 'ACTIVO'
            ORDER BY DAY(e.fecha_nacimiento)
            """;
        
        List<Empleado> empleados = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mes);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    empleados.add(mapearEmpleado(rs));
                }
            }
        }
        
        return empleados;
    }
}