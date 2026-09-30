package gestion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import modelo.Fichaje;
import persistencia.GestorBD;

public class GestorFichaje {

    private final GestorBD bd;

    public GestorFichaje() {
        this.bd = new GestorBD();
    }

    public void agregarMarcaje(Fichaje fichaje) {
        String id = bd.insertarFichaje(fichaje);
        fichaje.setId(id);
    }

    public boolean ficharEntrada(String idEmpleado) {
        if (buscarFichajeAbierto(idEmpleado) != null) {
            return false;
        }
        agregarMarcaje(new Fichaje(null, idEmpleado, LocalDate.now(), horaActual(), null));
        return true;
    }

    public boolean ficharSalida(String idEmpleado) {
        Fichaje abierto = buscarFichajeAbierto(idEmpleado);
        if (abierto == null) {
            return false;
        }
        abierto.setHoraSalida(horaActual());
        bd.actualizarSalida(abierto.getId(), abierto.getHoraSalida());
        return true;
    }

    public Fichaje buscarFichajeAbierto(String idEmpleado) {
        for (Fichaje fichaje : fichajesDe(idEmpleado)) {
            if (fichaje.estaAbierto()) {
                return fichaje;
            }
        }
        return null;
    }

    public List<Fichaje> fichajesDe(String idEmpleado) {
        return bd.fichajesDe(idEmpleado);
    }

    public List<Fichaje> todosLosFichajes() {
        return bd.todosLosFichajes();
    }

    public void eliminarFichajesDe(String idEmpleado) {
        bd.eliminarFichajesDe(idEmpleado);
    }

    public double calcularTotalHorasEmpleado(String idTrabajador) {
        double totalHoras = 0.0;
        for (Fichaje fichaje : fichajesDe(idTrabajador)) {
            totalHoras = totalHoras + fichaje.calculoHoras();
        }
        return totalHoras;
    }

    private LocalTime horaActual() {
        return LocalTime.now().withSecond(0).withNano(0);
    }
}
