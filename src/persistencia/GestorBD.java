package persistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import modelo.Empleado;
import modelo.Fichaje;

// Consultas a la base de datos MySQL
public class GestorBD {

    private static final String URL = "jdbc:mysql://localhost:3306/fichajes";
    private static final String USUARIO = "fichajes";
    private static final String PASSWORD = "fichajes";

    // Abre una conexión nueva con la BD
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // true si se puede conectar
    public boolean probarConexion() {
        try (Connection con = conectar()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // INSERT de un empleado
    public void insertarEmpleado(Empleado empleado) {
        String sql = "INSERT INTO empleados (id, nombre, password) VALUES (?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, empleado.getId());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el empleado: " + e.getMessage());
        }
    }

    // Busca por ID (sin distinguir mayúsculas). null si no existe
    public Empleado buscarEmpleado(String id) {
        String sql = "SELECT id, nombre, password FROM empleados WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(rs.getString("id"), rs.getString("nombre"), rs.getString("password"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el empleado: " + e.getMessage());
        }
    }

    // Devuelve todos los empleados
    public List<Empleado> listarEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT id, nombre, password FROM empleados ORDER BY id";
        try (Connection con = conectar();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                empleados.add(new Empleado(rs.getString("id"), rs.getString("nombre"), rs.getString("password")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los empleados: " + e.getMessage());
        }
        return empleados;
    }

    // Borra los fichajes del empleado y después el empleado
    public boolean eliminarEmpleado(String id) {
        eliminarFichajesDe(id);
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el empleado: " + e.getMessage());
        }
    }

    // INSERT de un fichaje. Devuelve el id generado
    public String insertarFichaje(Fichaje fichaje) {
        String sql = "INSERT INTO marcatges (id_empleado, fecha, hora_entrada, hora_salida) VALUES (?, ?, ?, ?)";
        try (Connection con = conectar();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, fichaje.getIdEmpleado());
            ps.setDate(2, Date.valueOf(fichaje.getFecha()));
            ps.setTime(3, Time.valueOf(fichaje.getHoraEntrada()));
            if (fichaje.getHoraSalida() == null) {
                ps.setNull(4, java.sql.Types.TIME);
            } else {
                ps.setTime(4, Time.valueOf(fichaje.getHoraSalida()));
            }
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                claves.next();
                return String.valueOf(claves.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el fichaje: " + e.getMessage());
        }
    }

    // Guarda la hora de salida de un fichaje
    public void actualizarSalida(String idFichaje, LocalTime horaSalida) {
        String sql = "UPDATE marcatges SET hora_salida = ? WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTime(1, Time.valueOf(horaSalida));
            ps.setInt(2, Integer.parseInt(idFichaje));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la salida: " + e.getMessage());
        }
    }

    // Fichajes de un empleado ordenados por fecha y hora
    public List<Fichaje> fichajesDe(String idEmpleado) {
        String sql = "SELECT * FROM marcatges WHERE id_empleado = ? ORDER BY fecha, hora_entrada";
        List<Fichaje> fichajes = new ArrayList<>();
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    fichajes.add(leerFichaje(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer los fichajes: " + e.getMessage());
        }
        return fichajes;
    }

    // Fichajes de todos los empleados
    public List<Fichaje> todosLosFichajes() {
        String sql = "SELECT * FROM marcatges ORDER BY fecha, hora_entrada";
        List<Fichaje> fichajes = new ArrayList<>();
        try (Connection con = conectar();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                fichajes.add(leerFichaje(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer los fichajes: " + e.getMessage());
        }
        return fichajes;
    }

    // Borra todos los fichajes de un empleado
    public void eliminarFichajesDe(String idEmpleado) {
        String sql = "DELETE FROM marcatges WHERE id_empleado = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEmpleado);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar los fichajes: " + e.getMessage());
        }
    }

    // Convierte la fila actual en un Fichaje
    private Fichaje leerFichaje(ResultSet rs) throws SQLException {
        Time salida = rs.getTime("hora_salida");
        return new Fichaje(
                String.valueOf(rs.getInt("id")),
                rs.getString("id_empleado"),
                rs.getDate("fecha").toLocalDate(),
                rs.getTime("hora_entrada").toLocalTime(),
                salida == null ? null : salida.toLocalTime());
    }
}
