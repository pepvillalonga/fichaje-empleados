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

    // Registra un empleado. false si el ID ya existe
    public boolean registrar(String id, String nombre, String password) {
        if (buscarPorId(id) != null) {
            return false;
        }
        bd.insertarEmpleado(new Empleado(id, nombre, password));
        return true;
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
