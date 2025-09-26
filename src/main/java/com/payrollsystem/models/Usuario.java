package com.payrollsystem.models;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String rol;
    private String email;
    private LocalDateTime fechaCreacion;
    private boolean activo;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String username, String password, String rol, String email) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.email = email;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public boolean isAdmin() { return "ADMIN".equals(rol); }
}
