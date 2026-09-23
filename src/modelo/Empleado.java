package modelo;

import java.util.ArrayList;
import java.util.List;

public class Empleado {

    private String id;
    private String nombre;
    private String password;
    private List<Fichaje> fichajes;

    public Empleado(String id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.fichajes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public List<Fichaje> getFichajes() {
        return fichajes;
    }
}
