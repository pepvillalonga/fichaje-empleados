package gestion;

import java.util.List;
import modelo.Empleado;
import persistencia.GestorBD;

// Lógica de empleados: registro, login y baja
public class GestorEmpleados {

    private final GestorBD bd;

    public GestorEmpleados() {
        this.bd = new GestorBD();
    }

    // true si la base de datos está disponible
    public boolean hayConexion() {
        return bd.probarConexion();
    }

    // Devuelve el empleado o null si no existe
    public Empleado buscarPorId(String id) {
        return bd.buscarEmpleado(id);
    }

    // Registra un empleado. Devuelve el error o null si todo va bien
    public String registrar(String id, String nombre, String password) {
        if (id.isEmpty() || nombre.isEmpty() || password.isEmpty()) {
            return "Rellena todos los campos.";
        }
        // Límites de las columnas de la tabla empleados
        if (id.length() > 20 || nombre.length() > 100 || password.length() > 100) {
            return "El ID admite 20 caracteres y el nombre y la contraseña 100.";
        }
        if (buscarPorId(id) != null) {
            return "Ya existe un empleado con ese ID.";
        }
        bd.insertarEmpleado(new Empleado(id, nombre, password));
        return null;
    }

    // Devuelve el empleado si ID y contraseña coinciden, si no null
    public Empleado login(String id, String password) {
        Empleado empleado = buscarPorId(id);
        if (empleado != null && empleado.getPassword().equals(password)) {
            return empleado;
        }
        return null;
    }

    // Borra el empleado y sus fichajes. true si se ha borrado
    public boolean eliminarEmpleado(String id) {
        return bd.eliminarEmpleado(id);
    }

    // Devuelve todos los empleados
    public List<Empleado> listarEmpleados() {
        return bd.listarEmpleados();
    }

}
