package gestion;

import java.util.ArrayList;
import java.util.List;
import modelo.Empleado;

public class GestorEmpleados {

    private final List<Empleado> listaEmpleados;

    public GestorEmpleados(){
        this.listaEmpleados = new ArrayList<>();
    }

    public void RegistrarEmpleado(Empleado empleado){
        listaEmpleados.add(empleado);
    }

    public void EliminarEmpleado(Empleado empleado){
        if (listaEmpleados.contains(empleado)){
            listaEmpleados.remove(empleado);
        }
    }

    public List<Empleado> ListaEmpleados(){
        return listaEmpleados;
    }
    
}
