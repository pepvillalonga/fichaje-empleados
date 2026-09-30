package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Fichaje {
    private String id, idEmpleado;
    private  LocalDate fecha;
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

    public Fichaje(String id, String idEmpleado, LocalTime horaEntrada, LocalTime horaSalida) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.fecha = LocalDate.now();
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    // Metodo
    public double calculoHoras() {
        if (horaEntrada.isBefore(horaSalida)){
            return 0;
        }
        int minutosEntrada = (horaEntrada.getHour() * 60) + horaEntrada.getMinute();
        int minutosSalida = (horaSalida.getHour() * 60) + horaSalida.getMinute();

        int diferenciaMinutos = minutosSalida - minutosEntrada;

        return diferenciaMinutos / 60.0;
    }
    
}
