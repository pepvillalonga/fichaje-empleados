package gestion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import modelo.Fichaje;
import persistencia.GestorBD;

// Lógica de fichajes: fichar y calcular horas
public class GestorFichaje {

    private final GestorBD bd;

    public GestorFichaje() {
        this.bd = new GestorBD();
    }

    // Guarda el fichaje en la BD y le asigna el id generado
    public void agregarMarcaje(Fichaje fichaje) {
        String id = bd.insertarFichaje(fichaje);
        fichaje.setId(id);
    }

    // Guarda un fichaje con fecha y horas indicadas. Devuelve el error o null si todo va bien
    public String registrarManual(String idEmpleado, LocalDate fecha, LocalTime entrada, LocalTime salida) {
        if (fecha.isAfter(LocalDate.now())) {
            return "La fecha no puede ser futura.";
        }
        if (!salida.isAfter(entrada)) {
            return "La hora de salida debe ser posterior a la de entrada.";
        }
        if (seSolapa(idEmpleado, fecha, entrada, salida)) {
            return "Se solapa con otro fichaje de ese día.";
        }
        agregarMarcaje(new Fichaje(null, idEmpleado, fecha, entrada, salida));
        return null;
    }

    // true si el tramo entrada-salida se cruza con otro fichaje del mismo día
    private boolean seSolapa(String idEmpleado, LocalDate fecha, LocalTime entrada, LocalTime salida) {
        for (Fichaje otro : fichajesDe(idEmpleado)) {
            if (!otro.getFecha().equals(fecha)) {
                continue;
            }
            // Un fichaje abierto se considera que sigue hasta el final del día
            LocalTime finOtro = otro.estaAbierto() ? LocalTime.MAX : otro.getHoraSalida();
            if (entrada.isBefore(finOtro) && salida.isAfter(otro.getHoraEntrada())) {
                return true;
            }
        }
        return false;
    }

    // Abre un fichaje con la hora actual. false si ya hay uno abierto
    public boolean ficharEntrada(String idEmpleado) {
        if (buscarFichajeAbierto(idEmpleado) != null) {
            return false;
        }
        agregarMarcaje(new Fichaje(null, idEmpleado, LocalDate.now(), horaActual(), null));
        return true;
    }

    // Cierra el fichaje abierto. false si no hay ninguno
    public boolean ficharSalida(String idEmpleado) {
        Fichaje abierto = buscarFichajeAbierto(idEmpleado);
        if (abierto == null) {
            return false;
        }
        abierto.setHoraSalida(horaActual());
        bd.actualizarSalida(abierto.getId(), abierto.getHoraSalida());
        return true;
    }

    // Devuelve el fichaje sin salida o null
    public Fichaje buscarFichajeAbierto(String idEmpleado) {
        for (Fichaje fichaje : fichajesDe(idEmpleado)) {
            if (fichaje.estaAbierto()) {
                return fichaje;
            }
        }
        return null;
    }

    // Devuelve los fichajes de un empleado
    public List<Fichaje> fichajesDe(String idEmpleado) {
        return bd.fichajesDe(idEmpleado);
    }

    // Devuelve los fichajes de todos los empleados
    public List<Fichaje> todosLosFichajes() {
        return bd.todosLosFichajes();
    }

    // Borra todos los fichajes de un empleado
    public void eliminarFichajesDe(String idEmpleado) {
        bd.eliminarFichajesDe(idEmpleado);
    }

    // Suma las horas de los fichajes cerrados
    public double calcularTotalHorasEmpleado(String idTrabajador) {
        double totalHoras = 0.0;
        for (Fichaje fichaje : fichajesDe(idTrabajador)) {
            totalHoras = totalHoras + fichaje.calculoHoras();
        }
        return totalHoras;
    }

    // Hora actual sin segundos
    private LocalTime horaActual() {
        return LocalTime.now().withSecond(0).withNano(0);
    }
}
