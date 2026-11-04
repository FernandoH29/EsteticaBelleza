package com.bellaimagen.modelo;

public class Especialista {

    private int idEspecialista;
    private String nombre;
    private String especialidad;
    private String estado;

    public Especialista() {
    }

    public Especialista(String nombre, String especialidad, String estado) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    public Especialista(int idEspecialista, String nombre,
                        String especialidad, String estado) {
        this.idEspecialista = idEspecialista;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    public int getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(int idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}