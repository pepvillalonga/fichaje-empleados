package gestion;

import java.util.List;
import modelo.Empleado;
import persistencia.GestorBD;

public class GestorEmpleados {

    private final GestorBD bd;

    public GestorEmpleados() {
        this.bd = new GestorBD();
    }

    public boolean hayConexion() {
        return bd.probarConexion();
    }

    public Empleado buscarPorId(String id) {
        return bd.buscarEmpleado(id);
    }

    public boolean registrar(String id, String nombre, String password) {
        if (buscarPorId(id) != null) {
            return false;
        }
        bd.insertarEmpleado(new Empleado(id, nombre, password));
        return true;
    }

    public Empleado login(String id, String password) {
        Empleado empleado = buscarPorId(id);
        if (empleado != null && empleado.getPassword().equals(password)) {
            return empleado;
        }
        return null;
    }

    public boolean eliminarEmpleado(String id) {
        return bd.eliminarEmpleado(id);
    }

    public void RegistrarEmpleado(Empleado empleado) {
        bd.insertarEmpleado(empleado);
    }

    public void EliminarEmpleado(Empleado empleado) {
        bd.eliminarEmpleado(empleado.getId());
    }

    public List<Empleado> ListaEmpleados() {
        return bd.listarEmpleados();
    }

}
