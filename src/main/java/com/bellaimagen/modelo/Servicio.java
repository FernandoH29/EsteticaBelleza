package com.bellaimagen.modelo;

public class Servicio {

    private int idServicio;
    private String nombre;
    private int duracionEstimada;
    private int tiempoBuffer;
    private double precio;

    public Servicio() {
    }

    public Servicio(String nombre, int duracionEstimada,
                    int tiempoBuffer, double precio) {
        this.nombre = nombre;
        this.duracionEstimada = duracionEstimada;
        this.tiempoBuffer = tiempoBuffer;
        this.precio = precio;
    }

    public Servicio(int idServicio, String nombre,
                    int duracionEstimada, int tiempoBuffer,
                    double precio) {
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.duracionEstimada = duracionEstimada;
        this.tiempoBuffer = tiempoBuffer;
        this.precio = precio;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(int duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public int getTiempoBuffer() {
        return tiempoBuffer;
    }

    public void setTiempoBuffer(int tiempoBuffer) {
        this.tiempoBuffer = tiempoBuffer;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}