package com.bellaimagen.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cita {

    private int idCita;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String estado;
    private int idCliente;
    private int idEspecialista;
    private List<Servicio> servicios;

    // Datos adicionales para mostrar la agenda
    private String nombreCliente;
    private String nombreEspecialista;
    private String nombresServicios;

    public Cita() {
        servicios = new ArrayList<>();
    }

    public Cita(LocalDateTime fechaHoraInicio,
                int idCliente,
                int idEspecialista,
                List<Servicio> servicios) {

        this.fechaHoraInicio = fechaHoraInicio;
        this.idCliente = idCliente;
        this.idEspecialista = idEspecialista;
        this.servicios = new ArrayList<>(servicios);
        this.estado = "Programada";
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(int idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = new ArrayList<>(servicios);
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreEspecialista() {
        return nombreEspecialista;
    }

    public void setNombreEspecialista(String nombreEspecialista) {
        this.nombreEspecialista = nombreEspecialista;
    }

    public String getNombresServicios() {
        return nombresServicios;
    }

    public void setNombresServicios(String nombresServicios) {
        this.nombresServicios = nombresServicios;
    }
}