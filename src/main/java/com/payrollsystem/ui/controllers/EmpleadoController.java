package com.payrollsystem.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import com.payrollsystem.models.Empleado;
import com.payrollsystem.models.Departamento;
import com.payrollsystem.dao.EmpleadoDAO;
import com.payrollsystem.dao.DepartamentoDAO;
import com.payrollsystem.ui.utils.AlertUtil;
import com.payrollsystem.ui.utils.ValidationUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmpleadoController {
    
    // Campos del formulario
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtSalarioBase;
    @FXML private TextField txtBanco;
    @FXML private TextField txtNumeroCuenta;
    
    @FXML private ComboBox<Departamento> cbDepartamento;
    @FXML private ComboBox<String> cbTipoContrato;
    @FXML private ComboBox<String> cbEstado;
    
    @FXML private DatePicker dpFechaIngreso;
    @FXML private DatePicker dpFechaNacimiento;
    
    // Botones
    @FXML private Button btnNuevo;
    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpiar;
    
    // Tabla y búsqueda
    @FXML private TextField txtBuscar;
    @FXML private TableView<Empleado> tableEmpleados;
    @FXML private TableColumn<Empleado, String> colEmpleadoCodigo;
    @FXML private TableColumn<Empleado, String> colEmpleadoNombre;
    @FXML private TableColumn<Empleado, String> colEmpleadoDepartamento;
    @FXML private TableColumn<Empleado, String> colEmpleadoSalario;
    @FXML private TableColumn<Empleado, String> colEmpleadoEstado;
    @FXML private TableColumn<Empleado, String> colEmpleadoFechaIngreso;
    
    private EmpleadoDAO empleadoDAO;
    private DepartamentoDAO departamentoDAO;
    private Empleado empleadoActual;
    private ObservableList<Empleado> empleados;
    
    @FXML
    private void initialize() {
        empleadoDAO = new EmpleadoDAO();
        departamentoDAO = new DepartamentoDAO();
        empleados = FXCollections.observableArrayList();
        
        configurarComponentes();
        configurarEventos();
        configurarTabla();
        cargarDatos();
        limpiarFormulario();
    }
    
    private void configurarComponentes() {
        // Configurar ComboBoxes
        cbTipoContrato.setItems(FXCollections.observableArrayList(
            "PERMANENTE", "TEMPORAL", "POR_HORAS"));
        cbTipoContrato.setValue("PERMANENTE");
        
        cbEstado.setItems(FXCollections.observableArrayList(
            "ACTIVO", "INACTIVO", "LICENCIA"));
        cbEstado.setValue("ACTIVO");
        
        // Configurar DatePickers
        dpFechaIngreso.setValue(LocalDate.now());
    }
    
    private void configurarEventos() {
        btnNuevo.setOnAction(e -> limpiarFormulario());
        btnGuardar.setOnAction(e -> guardarEmpleado());
        btnEliminar.setOnAction(e -> eliminarEmpleado());
        btnBuscar.setOnAction(e -> buscarEmpleados());
        btnLimpiar.setOnAction(e -> limpiarBusqueda());
        
        // Evento de selección en tabla
        tableEmpleados.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    cargarEmpleadoEnFormulario(newSelection);
                }
            });
    }
    
    private void configurarTabla() {
        colEmpleadoCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colEmpleadoNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colEmpleadoDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamentoNombre"));
        colEmpleadoSalario.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                "$" + cellData.getValue().getSalarioBase().toString()));
        colEmpleadoEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEmpleadoFechaIngreso.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFechaIngreso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        
        tableEmpleados.setItems(empleados);
    }
    
    private void cargarDatos() {
        try {
            // Cargar departamentos
            List<Departamento> departamentos = departamentoDAO.obtenerTodos();
            cbDepartamento.setItems(FXCollections.observableArrayList(departamentos));
            
            // Cargar empleados
            cargarEmpleados();
            
        } catch (Exception e) {
            AlertUtil.showError("Error", "Error cargando datos: " + e.getMessage());
        }
    }
    
    private void cargarEmpleados() {
        try {
            List<Empleado> lista = empleadoDAO.obtenerTodos();
            empleados.clear();
            empleados.addAll(lista);
        } catch (Exception e) {
            AlertUtil.showError("Error", "Error cargando empleados: " + e.getMessage());
        }
    }
    
    private void limpiarFormulario() {
        empleadoActual = null;
        txtCodigo.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtSalarioBase.clear();
        txtBanco.clear();
        txtNumeroCuenta.clear();
        
        cbDepartamento.setValue(null);
        cbTipoContrato.setValue("PERMANENTE");
        cbEstado.setValue("ACTIVO");
        
        dpFechaIngreso.setValue(LocalDate.now());
        dpFechaNacimiento.setValue(null);
        
        tableEmpleados.getSelectionModel().clearSelection();
        
        // Generar nuevo código
        generarCodigoEmpleado();
    }
    
    private void generarCodigoEmpleado() {
        try {
            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            int siguiente = empleados.size() + 1;
            txtCodigo.setText(String.format("EMP%03d", siguiente));
        } catch (Exception e) {
            txtCodigo.setText("EMP001");
        }
    }
    
    private void cargarEmpleadoEnFormulario(Empleado empleado) {
        empleadoActual = empleado;
        
        txtCodigo.setText(empleado.getCodigo());
        txtNombre.setText(empleado.getNombre());
        txtApellido.setText(empleado.getApellido());
        txtEmail.setText(empleado.getEmail());
        txtTelefono.setText(empleado.getTelefono());
        txtDireccion.setText(empleado.getDireccion());
        txtSalarioBase.setText(empleado.getSalarioBase().toString());
        txtBanco.setText(empleado.getBanco());
        txtNumeroCuenta.setText(empleado.getNumeroCuenta());
        
        // Buscar y seleccionar departamento
        for (Departamento dept : cbDepartamento.getItems()) {
            if (dept.getId() == empleado.getDepartamentoId()) {
                cbDepartamento.setValue(dept);
                break;
            }
        }
        
        cbTipoContrato.setValue(empleado.getTipoContrato());
        cbEstado.setValue(empleado.getEstado());
        
        dpFechaIngreso.setValue(empleado.getFechaIngreso());
        dpFechaNacimiento.setValue(empleado.getFechaNacimiento());
    }
    
    private void guardarEmpleado() {
        if (!validarFormulario()) {
            return;
        }
        
        try {
            Empleado empleado = empleadoActual != null ? empleadoActual : new Empleado();
            
            empleado.setCodigo(txtCodigo.getText().trim());
            empleado.setNombre(txtNombre.getText().trim());
            empleado.setApellido(txtApellido.getText().trim());
            empleado.setEmail(txtEmail.getText().trim());
            empleado.setTelefono(txtTelefono.getText().trim());
            empleado.setDireccion(txtDireccion.getText().trim());
            empleado.setSalarioBase(new BigDecimal(txtSalarioBase.getText()));
            empleado.setBanco(txtBanco.getText().trim());
            empleado.setNumeroCuenta(txtNumeroCuenta.getText().trim());
            
            empleado.setDepartamentoId(cbDepartamento.getValue().getId());
            empleado.setTipoContrato(cbTipoContrato.getValue());
            empleado.setEstado(cbEstado.getValue());
            
            empleado.setFechaIngreso(dpFechaIngreso.getValue());
            empleado.setFechaNacimiento(dpFechaNacimiento.getValue());
            
            if (empleadoActual == null) {
                empleadoDAO.crear(empleado);
                AlertUtil.showInfo("Éxito", "Empleado creado correctamente");
            } else {
                empleadoDAO.actualizar(empleado);
                AlertUtil.showInfo("Éxito", "Empleado actualizado correctamente");
            }
            
            cargarEmpleados();
            limpiarFormulario();
            
        } catch (Exception e) {
            AlertUtil.showError("Error", "Error guardando empleado: " + e.getMessage());
        }
    }
    
    private boolean validarFormulario() {
        StringBuilder errores = new StringBuilder();
        
        if (ValidationUtil.isEmpty(txtCodigo.getText())) {
            errores.append("- El código es requerido\n");
        }
        
        if (ValidationUtil.isEmpty(txtNombre.getText())) {
            errores.append("- El nombre es requerido\n");
        }
        
        if (ValidationUtil.isEmpty(txtApellido.getText())) {
            errores.append("- El apellido es requerido\n");
        }
        
        if (!ValidationUtil.isEmpty(txtEmail.getText()) && !ValidationUtil.isValidEmail(txtEmail.getText())) {
            errores.append("- El email no es válido\n");
        }
        
        if (ValidationUtil.isEmpty(txtSalarioBase.getText())) {
            errores.append("- El salario base es requerido\n");
        } else {
            try {
                BigDecimal salario = new BigDecimal(txtSalarioBase.getText());
                if (salario.compareTo(BigDecimal.ZERO) <= 0) {
                    errores.append("- El salario debe ser mayor a cero\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- El salario debe ser un número válido\n");
            }
        }
        
        if (cbDepartamento.getValue() == null) {
            errores.append("- Debe seleccionar un departamento\n");
        }
        
        if (dpFechaIngreso.getValue() == null) {
            errores.append("- La fecha de ingreso es requerida\n");
        } else if (dpFechaIngreso.getValue().isAfter(LocalDate.now())) {
            errores.append("- La fecha de ingreso no puede ser futura\n");
        }
        
        if (errores.length() > 0) {
            AlertUtil.showWarning("Errores de Validación", errores.toString());
            return false;
        }
        
        return true;
    }
    
    private void eliminarEmpleado() {
        if (empleadoActual == null) {
            AlertUtil.showWarning("Advertencia", "Seleccione un empleado para eliminar");
            return;
        }
        
        if (AlertUtil.showConfirmation("Confirmar Eliminación", 
            "¿Está seguro que desea eliminar al empleado " + empleadoActual.getNombreCompleto() + "?")) {
            
            try {
                empleadoDAO.eliminar(empleadoActual.getId());
                AlertUtil.showInfo("Éxito", "Empleado eliminado correctamente");
                cargarEmpleados();
                limpiarFormulario();
                
            } catch (Exception e) {
                AlertUtil.showError("Error", "Error eliminando empleado: " + e.getMessage());
            }
        }
    }
    
    private void buscarEmpleados() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty()) {
            cargarEmpleados();
            return;
        }
        
        try {
            List<Empleado> resultados = empleadoDAO.buscar(busqueda, null, null, null);
            empleados.clear();
            empleados.addAll(resultados);
            
        } catch (Exception e) {
            AlertUtil.showError("Error", "Error en búsqueda: " + e.getMessage());
        }
    }
    
    private void limpiarBusqueda() {
        txtBuscar.clear();
        cargarEmpleados();
    }
}
