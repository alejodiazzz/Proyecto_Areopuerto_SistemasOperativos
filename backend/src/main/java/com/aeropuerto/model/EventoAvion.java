package com.aeropuerto.model;

public class EventoAvion {
    private int id;
    private String estado;
    private String mensaje;

    public EventoAvion(int id, String estado, String mensaje) {
        this.id = id;
        this.estado = estado;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
