package gestion;

import java.util.ArrayList;
import java.util.List;
import modelo.Empleado;

public class GestorEmpleados {

    private final List<Empleado> listaEmpleados;

    public GestorEmpleados() {
        this.listaEmpleados = new ArrayList<>();
    }

    public Empleado buscarPorId(String id) {
        for (Empleado empleado : listaEmpleados) {
            if (empleado.getId().equalsIgnoreCase(id)) {
                return empleado;
            }
        }
        return null;
    }

    public boolean registrar(String id, String nombre, String password) {
        if (buscarPorId(id) != null) {
            return false;
        }
        listaEmpleados.add(new Empleado(id, nombre, password));
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
        Empleado empleado = buscarPorId(id);
        if (empleado == null) {
            return false;
        }
        listaEmpleados.remove(empleado);
        return true;
    }

    public void RegistrarEmpleado(Empleado empleado) {
        listaEmpleados.add(empleado);
    }

    public void EliminarEmpleado(Empleado empleado) {
        if (listaEmpleados.contains(empleado)) {
            listaEmpleados.remove(empleado);
        }
    }

    public List<Empleado> ListaEmpleados() {
        return listaEmpleados;
    }

}
