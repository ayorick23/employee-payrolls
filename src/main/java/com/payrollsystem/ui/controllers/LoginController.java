package com.payrollsystem.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.payrollsystem.services.AuthService;
import com.payrollsystem.models.Usuario;
import com.payrollsystem.ui.utils.AlertUtil;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;
    
    private AuthService authService = new AuthService();
    
    @FXML
    private void initialize() {
        loginButton.setOnAction(this::handleLogin);
        usernameField.setOnAction(e -> handleLogin(e));
        passwordField.setOnAction(e -> handleLogin(e));
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Complete todos los campos");
            return;
        }
        
        try {
            Usuario usuario = authService.authenticate(username, password);
            
            if (usuario != null) {
                abrirVentanaPrincipal(usuario);
                cerrarVentanaLogin();
            } else {
                statusLabel.setText("Usuario o contraseña incorrectos");
                passwordField.clear();
                passwordField.requestFocus();
            }
            
        } catch (Exception e) {
            AlertUtil.showError("Error", "Error al conectar con la base de datos: " + e.getMessage());
        }
    }
    
    private void abrirVentanaPrincipal(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            MainController mainController = loader.getController();
            mainController.setUsuarioActual(usuario);
            
            Stage stage = new Stage();
            stage.setTitle("Sistema de Nóminas - " + usuario.getUsername() + " (" + usuario.getRol() + ")");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            
        } catch (Exception e) {
            AlertUtil.showError("Error", "No se pudo abrir la ventana principal: " + e.getMessage());
        }
    }
    
    private void cerrarVentanaLogin() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
}
