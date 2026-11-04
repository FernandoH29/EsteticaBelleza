package com.bellaimagen.modelo;

import java.time.LocalDate;

public class Permuta {

    private int idPermuta;
    private LocalDate fecha;
    private int idEspecialistaOrigen;
    private int idEspecialistaDestino;
    private String estado;
    private String nombreOrigen;
    private String nombreDestino;

    public Permuta() {
    }

    public Permuta(int idPermuta, LocalDate fecha,
            int idEspecialistaOrigen, int idEspecialistaDestino,
            String estado) {
        this.idPermuta = idPermuta;
        this.fecha = fecha;
        this.idEspecialistaOrigen = idEspecialistaOrigen;
        this.idEspecialistaDestino = idEspecialistaDestino;
        this.estado = estado;
    }

    public int getIdPermuta() {
        return idPermuta;
    }

    public void setIdPermuta(int idPermuta) {
        this.idPermuta = idPermuta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getIdEspecialistaOrigen() {
        return idEspecialistaOrigen;
    }

    public void setIdEspecialistaOrigen(int idEspecialistaOrigen) {
        this.idEspecialistaOrigen = idEspecialistaOrigen;
    }

    public int getIdEspecialistaDestino() {
        return idEspecialistaDestino;
    }

    public void setIdEspecialistaDestino(int idEspecialistaDestino) {
        this.idEspecialistaDestino = idEspecialistaDestino;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreOrigen() {
        return nombreOrigen;
    }

    public void setNombreOrigen(String nombreOrigen) {
        this.nombreOrigen = nombreOrigen;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }
}
