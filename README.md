# AeroSim — Simulación de Aeropuerto

Este proyecto es una simulación avanzada de la operación de un aeropuerto enfocada en conceptos de **Sistemas Operativos** como concurrencia, semáforos, exclusión mutua (Mutex) y prevención de condiciones de carrera.

El sistema cuenta con un **Backend en Java** que gestiona la lógica de hilos y sincronización, y un **Frontend Web** dinámico que visualiza los eventos en tiempo real mediante WebSockets.

---

## 🚀 Instrucciones de Despliegue

Sigue estos pasos para poner en funcionamiento el proyecto en tu máquina local.

### 📋 Requisitos Previos

Asegúrate de tener instalado lo siguiente:
- **Java JDK 21** (o superior).
- **Maven** (opcional, ya que el proyecto incluye el envoltorio `mvnw`).
- Un navegador web moderno (Chrome, Edge, Firefox).

---

### 🖥️ 1. Iniciar el Backend (Java)

El servidor central gestiona los hilos de los aviones y la sincronización de recursos (pistas y puertas).

1. Abre una terminal en la carpeta raíz del proyecto.
2. Navega a la carpeta del backend:
   ```powershell
   cd backend
   ```
3. Ejecuta el servidor usando el comando de Maven:
   ```powershell
   ./mvnw spring-boot:run
   ```
4. El backend estará listo cuando veas el mensaje `Started SimulacionAeropuertoApplication` en la terminal. Por defecto, corre en el puerto **8090**.

---

### 🌐 2. Iniciar el Frontend (Web)

La interfaz es estática, por lo que no requiere instalación de dependencias de Node.js.

1. Navega a la carpeta `frontend/`.
2. Tienes dos opciones para abrirlo:
   - **Opción A (Fácil):** Haz doble clic en el archivo `index.html` para abrirlo directamente en tu navegador.
   - **Opción B (Recomendada):** Usa un servidor local sencillo (como la extensión "Live Server" de VS Code o `python -m http.server`) para evitar problemas de permisos de archivos locales.

---

## 🛠️ Cómo Funciona la Simulación

Una vez que ambos componentes estén activos:

1. **Conexión:** Verifica que el indicador en la esquina superior derecha diga **"CONECTADO — :8090"**.
2. **Configuración:**
   - Usa el slider para elegir la cantidad de **aviones** (1 a 20).
   - Selecciona el **Modo de Simulación**:
     - **Mutex + concurrencia:** El backend utiliza semáforos y bloques sincronizados para asegurar que no haya colisiones ni estados inconsistentes.
     - **Sólo semáforos:** Una simulación directa del uso de recursos.
3. **Control:** Haz clic en **▶ INICIAR** para disparar los hilos en el backend. Verás cómo los aviones aparecen en la cola, aterrizan en la pista y se dirigen a las puertas de embarque.

---

## 🏗️ Estructura del Proyecto

```text
├── backend/
│   ├── src/main/java/      # Lógica de Semáforos y Hilos (Java)
│   ├── pom.xml             # Dependencias de Spring Boot
│   └── mvnw                # Ejecutable de Maven
├── frontend/
│   ├── index.html          # Interfaz y Dashboard
│   └── styles.css          # Estilos premium y animaciones
└── README.md               # Este archivo
```


