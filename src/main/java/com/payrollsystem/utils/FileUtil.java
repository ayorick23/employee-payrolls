package com.payrollsystem.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtil {
    
    public static void crearDirectorioSiNoExiste(String ruta) throws IOException {
        Path path = Paths.get(ruta);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
    
    public static String generarNombreArchivoConFecha(String nombreBase, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return nombreBase + "_" + timestamp + "." + extension;
    }
    
    public static String obtenerRutaCompleta(String directorio, String nombreArchivo) throws IOException {
        crearDirectorioSiNoExiste(directorio);
        return Paths.get(directorio, nombreArchivo).toString();
    }
    
    public static boolean existeArchivo(String rutaArchivo) {
        return Files.exists(Paths.get(rutaArchivo));
    }
    
    public static void eliminarArchivoSiExiste(String rutaArchivo) throws IOException {
        Path path = Paths.get(rutaArchivo);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
    
    public static String obtenerExtension(String nombreArchivo) {
        int puntoIndex = nombreArchivo.lastIndexOf('.');
        return puntoIndex > 0 ? nombreArchivo.substring(puntoIndex + 1) : "";
    }
    
    public static long obtenerTamanoArchivo(String rutaArchivo) {
        try {
            return Files.size(Paths.get(rutaArchivo));
        } catch (IOException e) {
            return 0;
        }
    }
}