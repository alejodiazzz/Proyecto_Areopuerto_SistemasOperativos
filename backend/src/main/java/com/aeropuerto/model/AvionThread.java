package com.aeropuerto.model;

import com.aeropuerto.service.AeropuertoService;

public class AvionThread extends Thread {
    private final int id;
    private final AeropuertoService service;

    public AvionThread(int id, AeropuertoService service) {
        this.id = id;
        this.service = service;
    }

    @Override
    public void run() {
        try {
            service.procesoCompleto(id);
        } catch (InterruptedException e) {
            System.err.println("Avión " + id + " interrumpido.");
            Thread.currentThread().interrupt();
        }
    }
}
