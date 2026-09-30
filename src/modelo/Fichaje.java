package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Una jornada: fecha, hora de entrada y hora de salida
public class Fichaje {
    private String id, idEmpleado;
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    public String getId() {
        return id;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
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

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Fichaje(String id, String idEmpleado, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    // Sin fecha: usa la de hoy
    public Fichaje(String id, String idEmpleado, LocalTime horaEntrada, LocalTime horaSalida) {
        this(id, idEmpleado, LocalDate.now(), horaEntrada, horaSalida);
    }

    // Horas trabajadas. 0 si está abierto o la salida es anterior a la entrada
    public double calculoHoras() {
        if (horaSalida == null || horaSalida.isBefore(horaEntrada)) {
            return 0;
        }
        int minutosEntrada = (horaEntrada.getHour() * 60) + horaEntrada.getMinute();
        int minutosSalida = (horaSalida.getHour() * 60) + horaSalida.getMinute();

        int diferenciaMinutos = minutosSalida - minutosEntrada;

        return diferenciaMinutos / 60.0;
    }

    // true si aún no tiene hora de salida
    public boolean estaAbierto() {
        return horaSalida == null;
    }

    // Convierte horas decimales (7.5) a "7 h 30 min"
    public static String formatearHoras(double horas) {
        long minutosTotales = Math.round(horas * 60);
        return (minutosTotales / 60) + " h " + (minutosTotales % 60) + " min";
    }

    // Ej: "30/09/2026 | Entrada: 09:00 | Salida: 17:00 | 8 h 0 min"
    @Override
    public String toString() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        String texto = fecha.format(formatoFecha) + " | Entrada: " + horaEntrada.format(formatoHora) + " | Salida: ";
        if (estaAbierto()) {
            return texto + "en curso";
        }
        return texto + horaSalida.format(formatoHora) + " | " + formatearHoras(calculoHoras());
    }

}
