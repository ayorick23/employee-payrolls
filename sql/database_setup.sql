-- Crear base de datos
CREATE DATABASE IF NOT EXISTS payroll_system;
USE payroll_system;

-- Tabla usuarios
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'USER') NOT NULL,
    email VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla departamentos
CREATE TABLE departamentos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla empleados
CREATE TABLE empleados (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telefono VARCHAR(20),
    direccion TEXT,
    fecha_ingreso DATE NOT NULL,
    fecha_nacimiento DATE,
    departamento_id INT,
    salario_base DECIMAL(10,2) NOT NULL,
    tipo_contrato ENUM('PERMANENTE', 'TEMPORAL', 'POR_HORAS') DEFAULT 'PERMANENTE',
    numero_cuenta VARCHAR(50),
    banco VARCHAR(50),
    estado ENUM('ACTIVO', 'INACTIVO', 'LICENCIA') DEFAULT 'ACTIVO',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (departamento_id) REFERENCES departamentos(id)
);

-- Tabla nominas
CREATE TABLE nominas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    empleado_id INT NOT NULL,
    periodo_mes INT NOT NULL,
    periodo_anio INT NOT NULL,
    salario_base DECIMAL(10,2) NOT NULL,
    bonificaciones DECIMAL(10,2) DEFAULT 0,
    horas_extra DECIMAL(10,2) DEFAULT 0,
    descuento_isss DECIMAL(10,2) DEFAULT 0,
    descuento_afp DECIMAL(10,2) DEFAULT 0,
    descuento_isr DECIMAL(10,2) DEFAULT 0,
    otros_descuentos DECIMAL(10,2) DEFAULT 0,
    salario_neto DECIMAL(10,2) NOT NULL,
    fecha_calculo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('CALCULADA', 'PAGADA', 'CANCELADA') DEFAULT 'CALCULADA',
    FOREIGN KEY (empleado_id) REFERENCES empleados(id),
    UNIQUE KEY unique_nomina (empleado_id, periodo_mes, periodo_anio)
);

-- Tabla historial de pagos
CREATE TABLE historial_pagos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nomina_id INT NOT NULL,
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metodo_pago ENUM('TRANSFERENCIA', 'EFECTIVO', 'CHEQUE') DEFAULT 'TRANSFERENCIA',
    referencia_pago VARCHAR(100),
    observaciones TEXT,
    pagado_por VARCHAR(100),
    FOREIGN KEY (nomina_id) REFERENCES nominas(id)
);

-- Inserción de datos iniciales
INSERT INTO usuarios (username, password, rol, email) VALUES 
('admin', 'admin123', 'ADMIN', 'admin@company.com'),
('user', 'user123', 'USER', 'user@company.com');

INSERT INTO departamentos (nombre, descripcion) VALUES
('Recursos Humanos', 'Gestión del talento humano'),
('Finanzas', 'Administración financiera'),
('Tecnología', 'Desarrollo y soporte tecnológico'),
('Ventas', 'Comercialización y ventas'),
('Marketing', 'Marketing y publicidad');

-- Empleados de ejemplo
INSERT INTO empleados (codigo, nombre, apellido, email, telefono, fecha_ingreso, fecha_nacimiento, departamento_id, salario_base, numero_cuenta, banco) VALUES
('EMP001', 'Juan', 'Pérez', 'juan.perez@company.com', '7777-1234', '2023-01-15', '1990-05-20', 1, 800.00, '1234567890', 'Banco Agrícola'),
('EMP002', 'María', 'González', 'maria.gonzalez@company.com', '7777-5678', '2023-02-20', '1988-08-15', 2, 1200.00, '0987654321', 'Banco de América Central'),
('EMP003', 'Carlos', 'Rodríguez', 'carlos.rodriguez@company.com', '7777-9012', '2023-03-10', '1992-12-03', 3, 1500.00, '1122334455', 'Banco Cuscatlán');