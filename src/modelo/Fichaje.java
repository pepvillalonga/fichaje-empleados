package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Fichaje {
    private String id, idEmpleado;
    private  LocalDate fecha;
    private LocalTime hora;
    private TipoFichaje tipo; 

    public String getId() {
        return id;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public TipoFichaje getTipo() {
        return tipo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setTipo(TipoFichaje tipo) {
        this.tipo = tipo;
    }

    public Fichaje(String id, String idEmpleado, TipoFichaje tipo) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.tipo = tipo;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now(); 
    }
}
