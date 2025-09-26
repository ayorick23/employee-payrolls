package com.payrollsystem.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "/config/database.properties";
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = ConfigManager.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Error cargando archivo de configuración: " + e.getMessage());
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static double getDoubleProperty(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    // Métodos específicos para configuración de nómina
    public static double getTasaISSS() {
        return getDoubleProperty("calc.isss.rate", 0.0525);
    }
    
    public static double getMaximoISSS() {
        return getDoubleProperty("calc.isss.max", 1000.00);
    }
    
    public static double getTasaAFP() {
        return getDoubleProperty("calc.afp.rate", 0.0775);
    }
    
    public static double getTasaISR() {
        return getDoubleProperty("calc.isr.rate", 0.30);
    }
    
    public static double getMinimoISR() {
        return getDoubleProperty("calc.isr.minimum", 472.00);
    }
    
    public static String getRutaReportes() {
        return getProperty("reports.output.path", "./reports/");
    }
    
    public static String getRutaExportaciones() {
        return getProperty("exports.output.path", "./exports/");
    }
}