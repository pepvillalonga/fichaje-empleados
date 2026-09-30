-- Base de datos del Control Horario
-- Con Docker se ejecuta sola la primera vez que arranca el contenedor.
-- Sin Docker: ejecutar este script en MySQL/MariaDB (por ejemplo desde phpMyAdmin).

CREATE DATABASE IF NOT EXISTS fichajes;
USE fichajes;

CREATE TABLE IF NOT EXISTS empleados (
    id       VARCHAR(20)  PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Cada fila es una jornada: entrada y salida (salida NULL mientras está abierta)
CREATE TABLE IF NOT EXISTS marcatges (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado  VARCHAR(20) NOT NULL,
    fecha        DATE        NOT NULL,
    hora_entrada TIME        NOT NULL,
    hora_salida  TIME        NULL,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id)
);
