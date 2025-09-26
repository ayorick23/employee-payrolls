package com.payrollsystem.services;

import com.payrollsystem.models.Usuario;
import com.payrollsystem.dao.UsuarioDAO;
import java.security.MessageDigest;

public class AuthService {
    private UsuarioDAO usuarioDAO;
    
    public AuthService() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public Usuario authenticate(String username, String password) throws Exception {
        Usuario usuario = usuarioDAO.obtenerPorUsername(username);
        
        if (usuario != null && verificarPassword(password, usuario.getPassword())) {
            return usuario;
        }
        
        return null;
    }
    
    private boolean verificarPassword(String passwordTexto, String passwordHash) {
        // Implementación simple - en producción usar BCrypt
        return passwordTexto.equals(passwordHash);
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generando hash", e);
        }
    }
}