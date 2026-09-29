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

    public Empleado(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.fichajes = new ArrayList<>();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public List<Fichaje> getFichajes() {
        return fichajes;
    }
}
