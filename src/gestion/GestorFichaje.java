package gestion;

import java.util.ArrayList;
import java.util.List;
import modelo.Fichaje;

public class GestorFichaje {

    private final List<Fichaje> historialFichajes;


    public GestorFichaje() {
        this.historialFichajes = new ArrayList<>();
    }

    public void agregarMarcaje(Fichaje fichaje) {
        this.historialFichajes.add(fichaje);
    }

    // Método 
    public double calcularTotalHorasEmpleado(String idTrabajador) {
        double totalHoras = 0.0;
        for (Fichaje fichaje : historialFichajes){
            if (fichaje.getIdEmpleado().equals(idTrabajador)){
                totalHoras = totalHoras + fichaje.calculoHoras();
            }
        }
        return totalHoras;
    }
}