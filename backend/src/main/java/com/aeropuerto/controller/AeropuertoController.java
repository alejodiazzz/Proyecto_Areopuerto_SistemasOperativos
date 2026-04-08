package com.aeropuerto.controller;

import com.aeropuerto.model.AvionThread;
import com.aeropuerto.service.AeropuertoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AeropuertoController {

    @Autowired
    private AeropuertoService service;

    @GetMapping("/simular")
    public String simular(@RequestParam(defaultValue = "5") int aviones) {
        service.resetContadores();
        for (int i = 1; i <= aviones; i++) {
            new AvionThread(i, service).start();
        }
        return "Simulación iniciada con " + aviones + " aviones.";
    }

    @GetMapping("/resultados")
    public String getResultados() {
        return "Resultados de la Simulación:\n" +
               "Aviones servidos (Seguro - Mutex): " + service.getContadorSeguro() + "\n" +
               "Aviones servidos (Inseguro - Race Condition): " + service.getContadorInseguro();
    }
}
