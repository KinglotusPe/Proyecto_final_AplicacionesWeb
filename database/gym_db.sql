-- ==========================================================
-- SISTEMA DE GESTIÓN PARA GIMNASIO (GYM SISTEMA)
-- Script de Creación de Base de Datos y Datos de Prueba
-- Motor: MySQL 8.x
-- ==========================================================

-- 1. Crear Base de Datos
CREATE DATABASE IF NOT EXISTS gym_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE gym_db;

-- 2. Eliminar tablas si existen (orden inverso por llaves foraneas)
DROP TABLE IF EXISTS asistencia;
DROP TABLE IF EXISTS pago;
DROP TABLE IF EXISTS membresia;
DROP TABLE IF EXISTS cliente;

-- ==========================================================
-- TABLA: CLIENTE
-- ==========================================================
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    edad INT NULL,
    telefono VARCHAR(20) NULL,
    fecha_inscripcion DATE NULL
) ENGINE=InnoDB;

-- ==========================================================
-- TABLA: MEMBRESIA
-- ==========================================================
CREATE TABLE membresia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    tipo ENUM('DIARIO', 'SEMANAL', 'MENSUAL', 'TRIMESTRAL', 'ANUAL', 'PERSONALIZADA') NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado ENUM('ACTIVA', 'VENCIDA') NOT NULL DEFAULT 'ACTIVA',
    CONSTRAINT fk_membresia_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==========================================================
-- TABLA: PAGO
-- ==========================================================
CREATE TABLE pago (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha DATE NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TARJETA', 'YAPE', 'PLIN', 'TRANSFERENCIA') NOT NULL,
    proxima_fecha_pago DATE NULL,
    CONSTRAINT fk_pago_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==========================================================
-- TABLA: ASISTENCIA
-- ==========================================================
CREATE TABLE asistencia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    fecha_hora DATETIME(6) NOT NULL,
    CONSTRAINT fk_asistencia_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==========================================================
-- DATOS DE PRUEBA (OPCIONAL)
-- ==========================================================
INSERT INTO cliente (dni, nombres, apellidos, edad, telefono, fecha_inscripcion) VALUES
('72345678', 'Juan Carlos', 'Perez Lopez', 28, '987654321', CURDATE()),
('45891234', 'Maria Elena', 'Gomez Quispe', 24, '912345678', CURDATE()),
('10293847', 'Carlos Alberto', 'Mendoza Ramos', 32, '955443322', CURDATE());

INSERT INTO membresia (cliente_id, tipo, fecha_inicio, fecha_vencimiento, estado) VALUES
(1, 'MENSUAL', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 'ACTIVA'),
(2, 'TRIMESTRAL', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 3 MONTH), 'ACTIVA'),
(3, 'SEMANAL', DATE_SUB(CURDATE(), INTERVAL 2 WEEK), DATE_SUB(CURDATE(), INTERVAL 1 WEEK), 'VENCIDA');

INSERT INTO pago (cliente_id, monto, fecha, metodo_pago, proxima_fecha_pago) VALUES
(1, 100.00, CURDATE(), 'YAPE', DATE_ADD(CURDATE(), INTERVAL 1 MONTH)),
(2, 270.00, CURDATE(), 'TARJETA', DATE_ADD(CURDATE(), INTERVAL 3 MONTH)),
(3, 35.00, DATE_SUB(CURDATE(), INTERVAL 2 WEEK), 'EFECTIVO', DATE_SUB(CURDATE(), INTERVAL 1 WEEK));

INSERT INTO asistencia (cliente_id, fecha_hora) VALUES
(1, NOW()),
(2, NOW());
