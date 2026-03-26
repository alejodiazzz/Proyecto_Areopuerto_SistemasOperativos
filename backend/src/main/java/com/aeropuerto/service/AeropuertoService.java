package com.aeropuerto.service;

import com.aeropuerto.model.EventoAvion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

@Service
public class AeropuertoService {

    private final Semaphore semPistas = new Semaphore(1); // 1 Pista
    private final Semaphore semPuertas = new Semaphore(4); // 4 Puertas

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void aterrizar(int avionId) throws InterruptedException {
        // 1. Esperando Pista
        notificar(avionId, "ESPERANDO_PISTA", "Avión " + avionId + " esperando pista...");
        
        semPistas.acquire();
        // 2. Aterrizando
        notificar(avionId, "ATERRIZANDO", "Avión " + avionId + " aterriza en la pista.");
        Thread.sleep(2000); // Tiempo en pista

        // 3. Solicitar Puerta
        semPuertas.acquire();
        notificar(avionId, "EN_PUERTA", "Avión " + avionId + " se dirige a una puerta.");
        
        // Liberar pista
        semPistas.release();
        notificar(avionId, "PISTA_LIBRE", "Pista liberada por avión " + avionId);

        // 4. Desembarque
        Thread.sleep(5000); // Proceso de desembarque
        
        // 5. Liberar Puerta
        semPuertas.release();
        notificar(avionId, "FINALIZADO", "Avión " + avionId + " ha liberado la puerta y terminado proceso.");
    }

    private void notificar(int id, String estado, String mensaje) {
        EventoAvion evento = new EventoAvion(id, estado, mensaje);
        messagingTemplate.convertAndSend("/topic/aeropuerto", evento);
        System.out.println("[WebSocket] Avion " + id + ": " + estado);
    }
}
