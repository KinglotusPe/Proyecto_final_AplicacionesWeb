/* ==========================================================
   BRUTAL FITNESS - SISTEMA DE GESTIÓN INTEGRAL
   Script DDL y Datos de Prueba para PostgreSQL 14+
   ========================================================== */

-- 1. Eliminar tablas previas si existen (orden inverso)
DROP TABLE IF EXISTS seguimiento_fisico CASCADE;
DROP TABLE IF EXISTS asistencia CASCADE;
DROP TABLE IF EXISTS pago CASCADE;
DROP TABLE IF EXISTS membresia CASCADE;
DROP TABLE IF EXISTS entrenador CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;

-- 2. Eliminar tipos enum previos si existen
DROP TYPE IF EXISTS tipo_membresia_enum CASCADE;
DROP TYPE IF EXISTS estado_membresia_enum CASCADE;
DROP TYPE IF EXISTS metodo_pago_enum CASCADE;

-- 3. Crear tipos ENUM
CREATE TYPE tipo_membresia_enum AS ENUM ('DIARIO', 'SEMANAL', 'MENSUAL', 'TRIMESTRAL', 'ANUAL', 'PERSONALIZADA');
CREATE TYPE estado_membresia_enum AS ENUM ('ACTIVA', 'VENCIDA');
CREATE TYPE metodo_pago_enum AS ENUM ('EFECTIVO', 'TARJETA', 'YAPE', 'PLIN', 'TRANSFERENCIA');

/* ==========================================================
   TABLA: CLIENTE
   ========================================================== */
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    edad INTEGER NULL,
    telefono VARCHAR(20) NULL,
    fecha_inscripcion DATE NULL
);

/* ==========================================================
   TABLA: ENTRENADOR
   ========================================================== */
CREATE TABLE entrenador (
    id BIGSERIAL PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NULL,
    correo VARCHAR(100) NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
);

/* ==========================================================
   TABLA: MEMBRESIA
   ========================================================== */
CREATE TABLE membresia (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    tipo tipo_membresia_enum NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado estado_membresia_enum NOT NULL DEFAULT 'ACTIVA',
    CONSTRAINT fk_membresia_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

/* ==========================================================
   TABLA: PAGO
   ========================================================== */
CREATE TABLE pago (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    monto NUMERIC(10, 2) NOT NULL,
    fecha DATE NOT NULL,
    metodo_pago metodo_pago_enum NOT NULL,
    proxima_fecha_pago DATE NULL,
    CONSTRAINT fk_pago_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

/* ==========================================================
   TABLA: ASISTENCIA
   ========================================================== */
CREATE TABLE asistencia (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    fecha_hora TIMESTAMP(6) NOT NULL,
    CONSTRAINT fk_asistencia_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

/* ==========================================================
   TABLA: SEGUIMIENTO_FISICO (ANTROPOMETRÍA)
   ========================================================== */
CREATE TABLE seguimiento_fisico (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    entrenador_id BIGINT NULL,
    fecha_registro DATE NOT NULL,
    peso_kg DOUBLE PRECISION NOT NULL,
    altura_cm DOUBLE PRECISION NULL,
    porcentaje_grasa DOUBLE PRECISION NULL,
    masa_muscular DOUBLE PRECISION NULL,
    objetivo VARCHAR(100) NULL,
    observaciones VARCHAR(255) NULL,
    CONSTRAINT fk_seguimiento_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_seguimiento_entrenador 
        FOREIGN KEY (entrenador_id) REFERENCES entrenador(id) 
        ON DELETE SET NULL ON UPDATE CASCADE
);

/* ==========================================================
   DATOS DE PRUEBA
   ========================================================== */
INSERT INTO cliente (dni, nombres, apellidos, edad, telefono, fecha_inscripcion) VALUES
('72345678', 'Juan Carlos', 'Perez Lopez', 28, '987654321', CURRENT_DATE),
('45891234', 'Maria Elena', 'Gomez Quispe', 24, '912345678', CURRENT_DATE),
('10293847', 'Carlos Alberto', 'Mendoza Ramos', 32, '955443322', CURRENT_DATE);

INSERT INTO entrenador (dni, nombres, apellidos, especialidad, telefono, correo, estado) VALUES
('40112233', 'Marco Antonio', 'Vargas Torres', 'Musculación y Fuerza', '998877665', 'marco@brutalfitness.pe', 'ACTIVO'),
('41223344', 'Lucia', 'Fernandez Huaman', 'Crossfit / Funcional', '988776655', 'lucia@brutalfitness.pe', 'ACTIVO');

INSERT INTO membresia (cliente_id, tipo, fecha_inicio, fecha_vencimiento, estado) VALUES
(1, 'MENSUAL', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 month', 'ACTIVA'),
(2, 'TRIMESTRAL', CURRENT_DATE, CURRENT_DATE + INTERVAL '3 months', 'ACTIVA'),
(3, 'SEMANAL', CURRENT_DATE - INTERVAL '2 weeks', CURRENT_DATE - INTERVAL '1 week', 'VENCIDA');

INSERT INTO pago (cliente_id, monto, fecha, metodo_pago, proxima_fecha_pago) VALUES
(1, 100.00, CURRENT_DATE, 'YAPE', CURRENT_DATE + INTERVAL '1 month'),
(2, 270.00, CURRENT_DATE, 'TARJETA', CURRENT_DATE + INTERVAL '3 months'),
(3, 35.00, CURRENT_DATE - INTERVAL '2 weeks', 'EFECTIVO', CURRENT_DATE - INTERVAL '1 week');

INSERT INTO asistencia (cliente_id, fecha_hora) VALUES
(1, CURRENT_TIMESTAMP),
(2, CURRENT_TIMESTAMP);

INSERT INTO seguimiento_fisico (cliente_id, entrenador_id, fecha_registro, peso_kg, altura_cm, porcentaje_grasa, masa_muscular, objetivo, observaciones) VALUES
(1, 1, CURRENT_DATE, 76.5, 175.0, 18.0, 35.5, 'Hipertrofia / Ganancia Muscular', 'Progreso óptimo, buena respuesta a la carga de entrenamiento.'),
(2, 2, CURRENT_DATE, 58.0, 162.0, 21.0, 24.0, 'Definición / Pérdida de Grasa', 'Excelente resistencia en circuito funcional.');
