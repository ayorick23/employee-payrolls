package com.payrollsystem.ui.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.payrollsystem.models.*;
import com.payrollsystem.dao.*;
import com.payrollsystem.services.*;
import com.payrollsystem.ui.utils.AlertUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {
    
    @FXML private TabPane mainTabPane;
    @FXML private Label labelUsuario;
    @FXML private Label labelFecha;
    @FXML private Label labelStatus;
    @FXML private Label labelTotalEmpleados;
    @FXML private Label labelNominasPendientes;
    @FXML private Label labelCumpleanos;
    
    @FXML private TableView<Empleado> tableEmpleadosRecientes;
    @FXML private TableColumn<Empleado, String> colCodigo;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colDepartamento;
    @FXML private TableColumn<Empleado, String> colFechaIngreso;
    
    @FXML private ListView<String> listNotificaciones;
    
    // Menús
    @FXML private MenuItem menuLogout;
    @FXML private MenuItem menuExit;
    @FXML private Menu menuEmpleados;
    @FXML private MenuItem menuNuevoEmpleado;
    @FXML private MenuItem menuListarEmpleados;
    @FXML private MenuItem menuBuscarEmpleados;
    @FXML private Menu menuNominas;
    @FXML private MenuItem menuCalcularNomina;
    @FXML private MenuItem menuHistorialNominas;
    @FXML private Menu menuReportes;
    @FXML private MenuItem menuReporteNomina;
    @FXML private MenuItem menuReporteDepartamento;
    @FXML private MenuItem menuGraficos;
    @FXML private MenuItem menuExportar;
    @FXML private MenuItem menuImportar;
    @FXML private MenuItem menuNotificaciones;
    
    private Usuario usuarioActual;
    private EmpleadoDAO empleadoDAO;
    private NominaDAO nominaDAO;
    private NotificationService notificationService;
    
    @FXML
    private void initialize() {
        inicializarDAOs();
        configurarEventos();
        configurarTablas();
        iniciarActualizacionFecha();
    }
    
    private void inicializarDAOs() {
        empleadoDAO = new EmpleadoDAO();
        nominaDAO = new NominaDAO();
        notificationService = new NotificationService();
    }
    
    private void configurarEventos() {
        // Eventos de menú
        menuLogout.setOnAction(e -> cerrarSesion());
        menuExit.setOnAction(e -> salirAplicacion());
        
        menuNuevoEmpleado.setOnAction(e -> abrirGestionEmpleados());
        menuListarEmpleados.setOnAction(e -> abrirGestionEmpleados());
        menuBuscarEmpleados.setOnAction(e -> abrirBusquedaAvanzada());
        
        menuCalcularNomina.setOnAction(e -> abrirCalculoNomina());
        menuHistorialNominas.setOnAction(e -> abrirHistorialNominas());
        
        menuReporteNomina.setOnAction(e -> abrirReportes());
        menuGraficos.setOnAction(e -> abrirGraficos());
        
        menuExportar.setOnAction(e -> exportarDatos());
        menuImportar.setOnAction(e -> importarDatos());
        menuNotificaciones.setOnAction(e -> mostrarNotificaciones());
    }
    
    private void configurarTablas() {
        // Configurar tabla empleados recientes
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamentoNombre"));
        colFechaIngreso.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFechaIngreso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            ));
    }
    
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        labelUsuario.setText("Usuario: " + usuario.getUsername() + " (" + usuario.getRol() + ")");
        
        // Configurar permisos según rol
        configurarPermisosPorRol();
        
        // Cargar datos iniciales
        cargarDashboard();
    }
    
    private void configurarPermisosPorRol() {
        boolean isAdmin = usuarioActual.isAdmin();
        
        // Los usuarios normales tienen acceso limitado
        if (!isAdmin) {
            menuImportar.setDisable(true);
            // Otras restricciones según necesidades
        }
    }
    
    private void cargarDashboard() {
        Platform.runLater(() -> {
            try {
                cargarEstadisticas();
                cargarEmpleadosRecientes();
                cargarNotificaciones();
            } catch (Exception e) {
                AlertUtil.showError("Error", "Error cargando dashboard: " + e.getMessage());
            }
        });
    }
    
    private void cargarEstadisticas() throws Exception {
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        labelTotalEmpleados.setText(String.valueOf(empleados.size()));
        
        // Calcular nóminas pendientes (ejemplo)
        int nominasPendientes = 0; // Se calcularía según lógica de negocio
        labelNominasPendientes.setText(String.valueOf(nominasPendientes));
        
        // Cumpleaños este mes
        List<Empleado> cumpleanos = empleadoDAO.obtenerCumpleaniosMes(LocalDate.now().getMonthValue());
        labelCumpleanos.setText(String.valueOf(cumpleanos.size()));
    }
    
    private void cargarEmpleadosRecientes() throws Exception {
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        ObservableList<Empleado> empleadosRecientes = FXCollections.observableArrayList();
        
        // Tomar los últimos 10 empleados
        int limite = Math.min(10, empleados.size());
        for (int i = empleados.size() - limite; i < empleados.size(); i++) {
            empleadosRecientes.add(empleados.get(i));
        }
        
        tableEmpleadosRecientes.setItems(empleadosRecientes);
    }
    
    private void cargarNotificaciones() throws Exception {
        ObservableList<String> notificaciones = FXCollections.observableArrayList();
        
        // Cumpleaños este mes
        List<Empleado> cumpleanos = empleadoDAO.obtenerCumpleaniosMes(LocalDate.now().getMonthValue());
        if (!cumpleanos.isEmpty()) {
            notificaciones.add("🎂 " + cumpleanos.size() + " empleados cumplen años este mes");
        }
        
        // Otras notificaciones
        notificaciones.add("✅ Sistema funcionando correctamente");
        notificaciones.add("📊 Datos actualizados: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        listNotificaciones.setItems(notificaciones);
    }
    
    private void iniciarActualizacionFecha() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    labelFecha.setText("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                });
            }
        }, 0, 60000); // Actualizar cada minuto
    }
    
    private void abrirGestionEmpleados() {
        abrirVentanaModal("/fxml/empleado-form.fxml", "Gestión de Empleados", 1000, 700);
    }
    
    private void abrirBusquedaAvanzada() {
        abrirVentanaModal("/fxml/busqueda.fxml", "Búsqueda Avanzada", 800, 600);
    }
    
    private void abrirCalculoNomina() {
        abrirVentanaModal("/fxml/nomina-view.fxml", "Cálculo de Nómina", 900, 600);
    }
    
    private void abrirHistorialNominas() {
        // Implementar
        AlertUtil.showInfo("En Desarrollo", "Función en desarrollo");
    }
    
    private void abrirReportes() {
        abrirVentanaModal("/fxml/reports.fxml", "Reportes", 800, 600);
    }
    
    private void abrirGraficos() {
        // Implementar gráficos
        AlertUtil.showInfo("En Desarrollo", "Función en desarrollo");
    }
    
    private void exportarDatos() {
        // Implementar exportación
        AlertUtil.showInfo("En Desarrollo", "Función en desarrollo");
    }
    
    private void importarDatos() {
        // Implementar importación
        AlertUtil.showInfo("En Desarrollo", "Función en desarrollo");
    }
    
    private void mostrarNotificaciones() {
        // Implementar ventana de notificaciones
        AlertUtil.showInfo("Notificaciones", "Ver panel lateral para notificaciones actuales");
    }
    
    private void abrirVentanaModal(String fxmlPath, String titulo, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load(), width, height);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainTabPane.getScene().getWindow());
            stage.show();
            
        } catch (Exception e) {
            AlertUtil.showError("Error", "No se pudo abrir la ventana: " + e.getMessage());
        }
    }
    
    private void cerrarSesion() {
        if (AlertUtil.showConfirmation("Cerrar Sesión", "¿Está seguro que desea cerrar la sesión?")) {
            try {
                // Abrir ventana de login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                Scene scene = new Scene(loader.load(), 400, 300);
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                
                Stage loginStage = new Stage();
                loginStage.setTitle("Sistema de Nóminas - Login");
                loginStage.setScene(scene);
                loginStage.show();
                
                // Cerrar ventana actual
                Stage currentStage = (Stage) mainTabPane.getScene().getWindow();
                currentStage.close();
                
            } catch (Exception e) {
                AlertUtil.showError("Error", "Error al cerrar sesión: " + e.getMessage());
            }
        }
    }
    
    private void salirAplicacion() {
        if (AlertUtil.showConfirmation("Salir", "¿Está seguro que desea salir de la aplicación?")) {
            Platform.exit();
            System.exit(0);
        }
    }
}
