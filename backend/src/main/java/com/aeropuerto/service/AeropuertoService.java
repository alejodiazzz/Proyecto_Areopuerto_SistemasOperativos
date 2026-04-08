package com.aeropuerto.service;

import com.aeropuerto.model.EventoAvion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AeropuertoService {

    private final Semaphore semPistas = new Semaphore(2, true); // 2 Pistas
    private final Semaphore semPuertas = new Semaphore(4, true); // 4 Puertas
    private final ReentrantLock lockSincronizacion = new ReentrantLock();

    // Para demostración de condiciones de carrera
    private int contadorInseguro = 0;
    private final ReentrantLock lockSeguro = new ReentrantLock();
    private int contadorSeguro = 0;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void procesoCompleto(int avionId) throws InterruptedException {
        // --- 1. Sincronización Compuesta: Aterrizaje ---
        notificar(avionId, "SOLICITANDO_ATERRIZAJE", "Avión " + avionId + " solicita pista y puerta.");
        
        boolean recursosObtenidos = false;
        while (!recursosObtenidos) {
            lockSincronizacion.lock();
            try {
                if (semPistas.availablePermits() > 0 && semPuertas.availablePermits() > 0) {
                    semPistas.acquire();
                    semPuertas.acquire();
                    recursosObtenidos = true;
                }
            } finally {
                lockSincronizacion.unlock();
            }
            if (!recursosObtenidos) Thread.sleep(500); // Esperar si no hay ambos disponibles
        }

        notificar(avionId, "ATERRIZANDO", "Avión " + avionId + " aterrizando (Pista y Puerta RESERVADAS).");
        Thread.sleep(2000); // Tiempo de aterrizaje

        // Liberar pista tras aterrizar, pero sigue en puerta
        semPistas.release();
        notificar(avionId, "EN_PUERTA", "Avión " + avionId + " en puerta. Pista liberada.");

        // --- 2. Desembarque (Estancia en puerta) ---
        Thread.sleep(5000); 

        // --- 3. Despegue ---
        notificar(avionId, "SOLICITANDO_DESPEGUE", "Avión " + avionId + " solicita pista para despegar.");
        semPistas.acquire(); // Bloquea hasta que haya pista
        
        notificar(avionId, "DESPEGANDO", "Avión " + avionId + " despegando de la pista.");
        Thread.sleep(2000);

        // Liberar todo
        semPistas.release();
        semPuertas.release();
        notificar(avionId, "FINALIZADO", "Avión " + avionId + " despegó y liberó puerta.");
        
        // Incrementar contadores de simulación
        incrementarContadores();
    }

    private void incrementarContadores() {
        // Incrementar de forma insegura (Condición de carrera)
        int temp = contadorInseguro;
        try { Thread.sleep(10); } catch (InterruptedException e) {} // Forzar posibilidad de interrupción
        contadorInseguro = temp + 1;

        // Incrementar de forma segura
        lockSeguro.lock();
        try {
            contadorSeguro++;
        } finally {
            lockSeguro.unlock();
        }
    }

    public int getContadorInseguro() { return contadorInseguro; }
    public int getContadorSeguro() { return contadorSeguro; }
    public void resetContadores() { contadorInseguro = 0; contadorSeguro = 0; }

    private void notificar(int id, String estado, String mensaje) {
        EventoAvion evento = new EventoAvion(id, estado, mensaje);
        messagingTemplate.convertAndSend("/topic/aeropuerto", evento);
        System.out.println("[WebSocket] Avion " + id + ": " + estado);
    }
}
