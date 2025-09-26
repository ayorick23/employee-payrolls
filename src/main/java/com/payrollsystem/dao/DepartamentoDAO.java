package com.payrollsystem.dao;

import com.payrollsystem.models.Departamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAO {
    
    public List<Departamento> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM departamentos WHERE activo = TRUE ORDER BY nombre";
        List<Departamento> departamentos = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                departamentos.add(mapearDepartamento(rs));
            }
        }
        
        return departamentos;
    }
    
    public Departamento obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM departamentos WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDepartamento(rs);
                }
            }
        }
        
        return null;
    }
    
    private Departamento mapearDepartamento(ResultSet rs) throws SQLException {
        Departamento dept = new Departamento();
        dept.setId(rs.getInt("id"));
        dept.setNombre(rs.getString("nombre"));
        dept.setDescripcion(rs.getString("descripcion"));
        dept.setActivo(rs.getBoolean("activo"));
        
        return dept;
    }
}
