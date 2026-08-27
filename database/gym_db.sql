/* ==========================================================
   BRUTAL FITNESS - SISTEMA DE GESTIÓN INTEGRAL
   Script DDL y Datos de Prueba para MySQL 8.x
   ========================================================== */

-- 1. Crear Base de Datos
CREATE DATABASE IF NOT EXISTS gym_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE gym_db;
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 2. Eliminar tablas previas si existen (orden inverso)
DROP TABLE IF EXISTS rutina;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS seguimiento_fisico;
DROP TABLE IF EXISTS asistencia;
DROP TABLE IF EXISTS pago;
DROP TABLE IF EXISTS membresia;
DROP TABLE IF EXISTS entrenador;
DROP TABLE IF EXISTS cliente;

/* ==========================================================
   TABLA: CLIENTE
   ========================================================== */
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    edad INT NULL,
    telefono VARCHAR(20) NULL,
    fecha_inscripcion DATE NULL
) ENGINE=InnoDB;

/* ==========================================================
   TABLA: ENTRENADOR
   ========================================================== */
CREATE TABLE entrenador (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NULL,
    correo VARCHAR(100) NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

/* ==========================================================
   TABLA: MEMBRESIA
   ========================================================== */
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

/* ==========================================================
   TABLA: PAGO
   ========================================================== */
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

/* ==========================================================
   TABLA: ASISTENCIA
   ========================================================== */
CREATE TABLE asistencia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    fecha_hora DATETIME(6) NOT NULL,
    CONSTRAINT fk_asistencia_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

/* ==========================================================
   TABLA: SEGUIMIENTO_FISICO (ANTROPOMETRÍA)
   ========================================================== */
CREATE TABLE seguimiento_fisico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    entrenador_id BIGINT NULL,
    fecha_registro DATE NOT NULL,
    peso_kg DOUBLE NOT NULL,
    altura_cm DOUBLE NULL,
    porcentaje_grasa DOUBLE NULL,
    masa_muscular DOUBLE NULL,
    objetivo VARCHAR(100) NULL,
    observaciones VARCHAR(255) NULL,
    CONSTRAINT fk_seguimiento_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_seguimiento_entrenador 
        FOREIGN KEY (entrenador_id) REFERENCES entrenador(id) 
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

/* ==========================================================
   TABLA: RUTINA (PLANES DE ENTRENAMIENTO)
   ========================================================== */
CREATE TABLE rutina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    entrenador_id BIGINT NULL,
    nombre VARCHAR(100) NOT NULL,
    dia_semana VARCHAR(50) NOT NULL,
    ejercicios TEXT NOT NULL,
    nivel VARCHAR(50) NULL DEFAULT 'Intermedio',
    observaciones VARCHAR(255) NULL,
    CONSTRAINT fk_rutina_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rutina_entrenador 
        FOREIGN KEY (entrenador_id) REFERENCES entrenador(id) 
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

/* ==========================================================
   TABLA: USUARIO (AUTENTICACIÓN Y ROLES)
   ========================================================== */
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id BIGINT NULL,
    entrenador_id BIGINT NULL,
    CONSTRAINT fk_usuario_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id) 
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_entrenador 
        FOREIGN KEY (entrenador_id) REFERENCES entrenador(id) 
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

/* ==========================================================
   DATOS DE PRUEBA
   ========================================================== */
INSERT INTO cliente (dni, nombres, apellidos, edad, telefono, fecha_inscripcion) VALUES
('72345678', 'Juan Carlos', 'Perez Lopez', 28, '987654321', CURDATE()),
('45891234', 'Maria Elena', 'Gomez Quispe', 24, '912345678', CURDATE()),
('10293847', 'Carlos Alberto', 'Mendoza Ramos', 32, '955443322', CURDATE());

INSERT INTO entrenador (dni, nombres, apellidos, especialidad, telefono, correo, estado) VALUES
('40112233', 'Marco Antonio', 'Vargas Torres', 'Musculación y Fuerza', '998877665', 'marco@brutalfitness.pe', 'ACTIVO'),
('41223344', 'Lucia', 'Fernandez Huaman', 'Crossfit / Funcional', '988776655', 'lucia@brutalfitness.pe', 'ACTIVO');

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

INSERT INTO seguimiento_fisico (cliente_id, entrenador_id, fecha_registro, peso_kg, altura_cm, porcentaje_grasa, masa_muscular, objetivo, observaciones) VALUES
(1, 1, CURDATE(), 76.5, 175.0, 18.0, 35.5, 'Hipertrofia / Ganancia Muscular', 'Progreso óptimo, buena respuesta a la carga de entrenamiento.'),
(2, 2, CURDATE(), 58.0, 162.0, 21.0, 24.0, 'Definición / Pérdida de Grasa', 'Excelente resistencia en circuito funcional.');

INSERT INTO rutina (cliente_id, entrenador_id, nombre, dia_semana, ejercicios, nivel, observaciones) VALUES
(1, 1, 'Fuerza e Hipertrofia A', 'Lunes - Pecho y Tríceps', '1. Press Banca: 4x10\n2. Press Inclinado con Mancuernas: 3x12\n3. Fondos en Paralelas: 3x al fallo\n4. Extensión Tríceps en Polea: 4x15', 'Intermedio', 'Descanso de 90 segundos entre series pesadas.'),
(1, 1, 'Fuerza e Hipertrofia B', 'Miércoles - Espalda y Bíceps', '1. Jalón al Pecho: 4x12\n2. Remo con Barra: 4x10\n3. Curl de Bíceps con Barra Z: 3x12\n4. Martillo con Mancuernas: 3x15', 'Intermedio', 'Cuidar la retracción escapular.'),
(2, 2, 'Acondicionamiento Físico', 'Martes - Piernas y Core', '1. Sentadillas con barra: 4x12\n2. Prensa inclinada: 3x15\n3. Zancadas con mancuernas: 3x12/pierna\n4. Plancha abdominal: 4x45 seg', 'Principiante', 'Priorizar técnica y respiración.');

/* Contraseña encriptada BCrypt por defecto: admin123, recepcion123, etc. */
INSERT INTO usuario (username, password, nombre, rol, activo, cliente_id, entrenador_id) VALUES
('admin', '$2a$10$eAccYoNO32CpArGPl9OxP.4jGzT7mBvHcqIe7tJ9P7KkV6OqN4qC6', 'César Luza (Administrador General)', 'ROLE_ADMIN', TRUE, NULL, NULL),
('recepcion', '$2a$10$eAccYoNO32CpArGPl9OxP.4jGzT7mBvHcqIe7tJ9P7KkV6OqN4qC6', 'Staff Recepción BRUTAL', 'ROLE_RECEPCIONISTA', TRUE, NULL, NULL),
('entrenador', '$2a$10$eAccYoNO32CpArGPl9OxP.4jGzT7mBvHcqIe7tJ9P7KkV6OqN4qC6', 'Prof. Marco Antonio Vargas', 'ROLE_ENTRENADOR', TRUE, NULL, 1),
('72345678', '$2a$10$eAccYoNO32CpArGPl9OxP.4jGzT7mBvHcqIe7tJ9P7KkV6OqN4qC6', 'Juan Carlos Perez Lopez', 'ROLE_CLIENTE', TRUE, 1, NULL);
