package modelo;

// Datos de un empleado
public class Empleado {

    private String id;
    private String nombre;
    private String password;

    public Empleado(String id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
    }

    // Sin contraseña: se deja vacía para evitar null
    public Empleado(String id, String nombre) {
        this(id, nombre, "");
    }

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

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
