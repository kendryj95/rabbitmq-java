# Caso de Uso: Sistema de Registro y Notificaciones para una Aplicación IoT de Hogar Inteligente

---

## Escenario

Una aplicación de **hogar inteligente (IoT)** necesita un sistema eficiente para **gestionar y enrutar mensajes** provenientes de distintos dispositivos conectados. Los dispositivos incluyen:

1. Sensores de temperatura.
2. Cámaras de seguridad.
3. Sensores de movimiento.
4. Dispositivos de iluminación inteligente.

La solución debe permitir que los mensajes se **enruten de forma flexible** a los servicios correspondientes según un **patrón** en la clave de enrutamiento. Por ejemplo:

- Mensajes de temperatura deben llegar al servicio de **monitoreo climático**.
- Alertas de seguridad deben ser procesadas por el servicio de **seguridad**.
- Mensajes de dispositivos de iluminación deben ir al servicio de **automatización de luces**.

---

## Requerimiento

Se necesita un sistema de enrutamiento donde los mensajes sean dirigidos a diferentes colas según **patrones** en la clave de enrutamiento (routing key). Por ejemplo:

- `sensor.temperature.livingroom` → Servicio de monitoreo climático.
- `security.camera.garage` → Servicio de seguridad.
- `lights.kitchen.on` → Servicio de automatización de luces.

---

## Por qué utilizar el tipo de Exchange "Topic"

El **Exchange de tipo "Topic"** es ideal para este caso porque permite enrutar mensajes basados en **patrones de texto** dentro de la clave de enrutamiento. Los patrones permiten usar:

- `*` (comodín para una palabra).
- `#` (comodín para varias palabras).

Esto brinda **flexibilidad** y **escalabilidad**, ya que permite agregar nuevos tipos de dispositivos sin modificar la configuración existente.

---

## Flujo de Trabajo

1. **Productor**: Los dispositivos IoT envían mensajes al **Exchange Topic** usando claves de enrutamiento que describen el origen y tipo del evento. Por ejemplo:
    - `sensor.temperature.livingroom`
    - `security.camera.frontdoor`
    - `lights.bedroom.off`

2. **Exchange Topic**: RabbitMQ evalúa los patrones vinculados a las colas y enruta el mensaje según coincida con una **clave de enrutamiento**.

3. **Consumidores**:
    - **Servicio de Monitoreo Climático**: Escucha las claves `sensor.temperature.*`.
    - **Servicio de Seguridad**: Escucha las claves `security.#`.
    - **Servicio de Automatización de Luces**: Escucha las claves `lights.*.on` y `lights.*.off`.

---

## Ejemplo de Configuración

- **Exchange**: `iot_exchange` (tipo Topic).
- **Routing Keys** y **Colas Vinculadas**:
    - `sensor.temperature.*` → Cola: `climate_monitoring_queue`.
    - `security.#` → Cola: `security_queue`.
    - `lights.*.on` → Cola: `lights_on_queue`.
    - `lights.*.off` → Cola: `lights_off_queue`.

---

## Beneficios

1. **Enrutamiento flexible**: Permite enrutar mensajes usando patrones, adaptándose a una gran cantidad de dispositivos y eventos.
2. **Escalabilidad**: Es fácil agregar nuevas reglas de enrutamiento o tipos de dispositivos sin afectar las configuraciones existentes.
3. **Separación lógica**: Cada servicio recibe únicamente los mensajes relevantes según los patrones de enrutamiento.
4. **Eficiencia**: Los mensajes son dirigidos automáticamente a las colas adecuadas, reduciendo la carga en el productor.

---

## Resumen

El tipo de Exchange **Topic** en RabbitMQ es ideal cuando se necesita enrutar mensajes basados en **patrones** definidos en las claves de enrutamiento. Es especialmente útil en sistemas donde los eventos son variados y provienen de múltiples orígenes, como en una aplicación IoT de hogar inteligente.
