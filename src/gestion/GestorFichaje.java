package gestion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import modelo.Fichaje;

public class GestorFichaje {

    private final List<Fichaje> historialFichajes;
    private int siguienteId;

    public GestorFichaje() {
        this.historialFichajes = new ArrayList<>();
        this.siguienteId = 1;
    }

    public void agregarMarcaje(Fichaje fichaje) {
        this.historialFichajes.add(fichaje);
    }

    public boolean ficharEntrada(String idEmpleado) {
        if (buscarFichajeAbierto(idEmpleado) != null) {
            return false;
        }
        String id = String.valueOf(siguienteId);
        siguienteId++;
        agregarMarcaje(new Fichaje(id, idEmpleado, LocalDate.now(), horaActual(), null));
        return true;
    }

    public boolean ficharSalida(String idEmpleado) {
        Fichaje abierto = buscarFichajeAbierto(idEmpleado);
        if (abierto == null) {
            return false;
        }
        abierto.setHoraSalida(horaActual());
        return true;
    }

    public Fichaje buscarFichajeAbierto(String idEmpleado) {
        for (Fichaje fichaje : historialFichajes) {
            if (fichaje.getIdEmpleado().equalsIgnoreCase(idEmpleado) && fichaje.estaAbierto()) {
                return fichaje;
            }
        }
        return null;
    }

    public List<Fichaje> fichajesDe(String idEmpleado) {
        List<Fichaje> resultado = new ArrayList<>();
        for (Fichaje fichaje : historialFichajes) {
            if (fichaje.getIdEmpleado().equalsIgnoreCase(idEmpleado)) {
                resultado.add(fichaje);
            }
        }
        return resultado;
    }

    public void eliminarFichajesDe(String idEmpleado) {
        for (int i = historialFichajes.size() - 1; i >= 0; i--) {
            if (historialFichajes.get(i).getIdEmpleado().equalsIgnoreCase(idEmpleado)) {
                historialFichajes.remove(i);
            }
        }
    }

    public double calcularTotalHorasEmpleado(String idTrabajador) {
        double totalHoras = 0.0;
        for (Fichaje fichaje : historialFichajes) {
            if (fichaje.getIdEmpleado().equalsIgnoreCase(idTrabajador)) {
                totalHoras = totalHoras + fichaje.calculoHoras();
            }
        }
        return totalHoras;
    }

    private LocalTime horaActual() {
        return LocalTime.now().withSecond(0).withNano(0);
    }
}
