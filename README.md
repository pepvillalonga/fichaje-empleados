# Control Horario

Aplicación de consola en Java para registrar la entrada y salida de empleados y consultar sus horas trabajadas.

## Funcionalidades

- Registro e inicio de sesión de empleados
- Fichar entrada y salida
- Consultar los fichajes y las horas totales trabajadas
- Eliminar la cuenta

## Estructura

```
src/
├── Main.java          → Menús por consola
├── modelo/            → Empleado y Fichaje
├── gestion/           → Lógica del programa
└── persistencia/      → Consultas a MySQL (GestorBD)
lib/                   → Driver JDBC de MySQL
sql/crear_tablas.sql   → Tablas empleados y marcatges
docker-compose.yml     → Base de datos MySQL
```

## Requisitos

- Java 17 o superior
- Docker Desktop (o un MySQL/MariaDB propio, ver abajo)

## Cómo ejecutar

1. Arrancar la base de datos (la primera vez crea las tablas sola):

   ```
   docker compose up -d
   ```

2. Compilar:

   ```
   javac -encoding UTF-8 -cp "lib/*" -d out src/*.java src/*/*.java
   ```

3. Ejecutar:

   ```
   java -cp "out;lib/*" Main
   ```

   En Linux/Mac el separador es `:` en vez de `;`.

Para parar la base de datos: `docker compose down` (los datos se conservan).
Para borrar todos los datos: `docker compose down -v`.

### Sin Docker (XAMPP u otro MySQL)

Ejecutar `sql/crear_tablas.sql` (por ejemplo en phpMyAdmin) y crear el usuario:

```sql
CREATE USER 'fichajes'@'localhost' IDENTIFIED BY 'fichajes';
GRANT ALL PRIVILEGES ON fichajes.* TO 'fichajes'@'localhost';
```
