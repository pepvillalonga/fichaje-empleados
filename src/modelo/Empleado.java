package modelo;

public class Empleado {
    private String id, nombre;
    private Boolean trabajando;


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

    public boolean isTrabajando() {
        return trabajando;
    }

    public void setTrabajando(boolean trabajando) {
        this.trabajando = trabajando;
    }

    // Metodos
    public void registrarEntrada() {
        this.trabajando = true;
    }

    public void registrarSalida() {
        this.trabajando = false;
    }
}
