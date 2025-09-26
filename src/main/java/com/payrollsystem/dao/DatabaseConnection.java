package com.payrollsystem.dao;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import com.payrollsystem.utils.ConfigManager;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "/config/database.properties";
    private static Properties props;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        props = new Properties();
        try (InputStream is = DatabaseConnection.class.getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                props.load(is);
            } else {
                // Valores por defecto
                props.setProperty("db.url", "jdbc:mysql://localhost:3306/payroll_system");
                props.setProperty("db.username", "root");
                props.setProperty("db.password", "");
                props.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            }
        } catch (Exception e) {
            System.err.println("Error cargando configuración de BD: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(props.getProperty("db.driver"));
            return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.username"),
                props.getProperty("db.password")
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Conexión a BD exitosa!");
        } catch (SQLException e) {
            System.err.println("Error conectando a BD: " + e.getMessage());
        }
    }
}