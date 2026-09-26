package modelo;

public class Empleado {
    private String id, nombre;


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

    public Empleado(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
}
