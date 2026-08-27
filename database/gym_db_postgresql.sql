-- ==========================================================
-- SISTEMA DE GESTIÓN PARA GIMNASIO (GYM SISTEMA)
-- Script de Creación de Base de Datos y Datos de Prueba
-- Motor: PostgreSQL 14 / 15 / 16 / 17
-- ==========================================================

-- 1. Crear Base de Datos (ejecutar en postgres admin si es necesario)
-- CREATE DATABASE gym_db WITH ENCODING 'UTF8';
-- \c gym_db;

-- 2. Eliminar tablas si existen (en orden inverso por llaves foraneas)
DROP TABLE IF EXISTS asistencia CASCADE;
DROP TABLE IF EXISTS pago CASCADE;
DROP TABLE IF EXISTS membresia CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;

-- 3. Eliminar tipos ENUM si existen
DROP TYPE IF EXISTS tipo_membresia_enum CASCADE;
DROP TYPE IF EXISTS estado_membresia_enum CASCADE;
DROP TYPE IF EXISTS metodo_pago_enum CASCADE;

-- ==========================================================
-- TABLA: CLIENTE
-- ==========================================================
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    edad INTEGER NULL,
    telefono VARCHAR(20) NULL,
    fecha_inscripcion DATE NULL
);

-- ==========================================================
-- TABLA: MEMBRESIA
-- ==========================================================
CREATE TABLE membresia (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('DIARIO', 'SEMANAL', 'MENSUAL', 'TRIMESTRAL', 'ANUAL', 'PERSONALIZADA')),
    fecha_inicio DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA', 'VENCIDA')),
    CONSTRAINT fk_membresia_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ==========================================================
-- TABLA: PAGO
-- ==========================================================
CREATE TABLE pago (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    monto NUMERIC(10, 2) NOT NULL,
    fecha DATE NOT NULL,
    metodo_pago VARCHAR(20) NOT NULL CHECK (metodo_pago IN ('EFECTIVO', 'TARJETA', 'YAPE', 'PLIN', 'TRANSFERENCIA')),
    proxima_fecha_pago DATE NULL,
    CONSTRAINT fk_pago_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ==========================================================
-- TABLA: ASISTENCIA
-- ==========================================================
CREATE TABLE asistencia (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    fecha_hora TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_asistencia_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ==========================================================
-- DATOS DE PRUEBA (OPCIONAL)
-- ==========================================================
INSERT INTO cliente (dni, nombres, apellidos, edad, telefono, fecha_inscripcion) VALUES
('72345678', 'Juan Carlos', 'Perez Lopez', 28, '987654321', CURRENT_DATE),
('45891234', 'Maria Elena', 'Gomez Quispe', 24, '912345678', CURRENT_DATE),
('10293847', 'Carlos Alberto', 'Mendoza Ramos', 32, '955443322', CURRENT_DATE);

INSERT INTO membresia (cliente_id, tipo, fecha_inicio, fecha_vencimiento, estado) VALUES
(1, 'MENSUAL', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 month', 'ACTIVA'),
(2, 'TRIMESTRAL', CURRENT_DATE, CURRENT_DATE + INTERVAL '3 month', 'ACTIVA'),
(3, 'SEMANAL', CURRENT_DATE - INTERVAL '2 week', CURRENT_DATE - INTERVAL '1 week', 'VENCIDA');

INSERT INTO pago (cliente_id, monto, fecha, metodo_pago, proxima_fecha_pago) VALUES
(1, 100.00, CURRENT_DATE, 'YAPE', CURRENT_DATE + INTERVAL '1 month'),
(2, 270.00, CURRENT_DATE, 'TARJETA', CURRENT_DATE + INTERVAL '3 month'),
(3, 35.00, CURRENT_DATE - INTERVAL '2 week', 'EFECTIVO', CURRENT_DATE - INTERVAL '1 week');

INSERT INTO asistencia (cliente_id, fecha_hora) VALUES
(1, NOW()),
(2, NOW());
