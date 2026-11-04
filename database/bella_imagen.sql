-- ============================================================
-- BELLA IMAGEN - Estructura de la base de datos
-- Motor: MySQL 8.0+
--
-- INSTRUCCIONES:
-- 1. Cree o seleccione una base de datos vacia en MySQL Workbench.
--    Ejemplo para una instalacion local:
--       CREATE DATABASE bella_imagen
--           CHARACTER SET utf8mb4
--           COLLATE utf8mb4_0900_ai_ci;
--       USE bella_imagen;
-- 2. Ejecute este script completo dentro de esa base de datos.
--
-- NOTA: Este script crea la estructura. No elimina tablas existentes
-- ni copia los registros actuales de Railway.
-- ============================================================

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS CLIENTE (
    id_cliente INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (id_cliente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS ESPECIALISTA (
    id_especialista INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo',
    PRIMARY KEY (id_especialista)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS SERVICIO (
    id_servicio INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    duracion_estimada INT NOT NULL,
    tiempo_buffer INT NOT NULL DEFAULT 0,
    precio DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_servicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS USUARIO (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL,
    contrasena_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL DEFAULT 'RECEPCIONISTA',
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo',
    fecha_creacion TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY usuario (usuario),
    CONSTRAINT chk_usuario_estado CHECK (estado IN ('Activo', 'Inactivo')),
    CONSTRAINT chk_usuario_rol CHECK (rol IN ('ADMINISTRADOR', 'RECEPCIONISTA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS CITA (
    id_cita INT NOT NULL AUTO_INCREMENT,
    fecha_hora_inicio DATETIME NOT NULL,
    fecha_hora_fin DATETIME NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'Programada',
    id_cliente INT NOT NULL,
    id_especialista INT NOT NULL,
    PRIMARY KEY (id_cita),
    KEY fk_cita_cliente (id_cliente),
    KEY fk_cita_especialista (id_especialista),
    CONSTRAINT fk_cita_cliente FOREIGN KEY (id_cliente) REFERENCES CLIENTE (id_cliente),
    CONSTRAINT fk_cita_especialista FOREIGN KEY (id_especialista) REFERENCES ESPECIALISTA (id_especialista)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS DETALLE_CITA (
    id_detalle INT NOT NULL AUTO_INCREMENT,
    id_cita INT NOT NULL,
    id_servicio INT NOT NULL,
    precio_aplicado DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_detalle),
    KEY fk_detalle_cita (id_cita),
    KEY fk_detalle_servicio (id_servicio),
    CONSTRAINT fk_detalle_cita FOREIGN KEY (id_cita) REFERENCES CITA (id_cita),
    CONSTRAINT fk_detalle_servicio FOREIGN KEY (id_servicio) REFERENCES SERVICIO (id_servicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS PERMUTA_TURNO (
    id_permuta INT NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    id_especialista_origen INT NOT NULL,
    id_especialista_destino INT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'Pendiente',
    PRIMARY KEY (id_permuta),
    KEY fk_permuta_origen (id_especialista_origen),
    KEY fk_permuta_destino (id_especialista_destino),
    CONSTRAINT fk_permuta_origen FOREIGN KEY (id_especialista_origen) REFERENCES ESPECIALISTA (id_especialista),
    CONSTRAINT fk_permuta_destino FOREIGN KEY (id_especialista_destino) REFERENCES ESPECIALISTA (id_especialista)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Verificacion: debe devolver las siete tablas del sistema.
SHOW TABLES;
