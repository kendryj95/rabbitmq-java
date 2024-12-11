# Caso de Uso: Sistema de Transmisión de Eventos en Tiempo Real para una Plataforma de Streaming

---

## Escenario

Una plataforma de **streaming de video en vivo** necesita un sistema eficiente para **transmitir eventos** en tiempo real a múltiples servicios y usuarios. Por ejemplo:

1. Notificar a **todos los espectadores** que un evento en vivo está por comenzar.
2. Informar a todos los microservicios involucrados (estadísticas, chat, publicidad) sobre un **nuevo estado del stream**.
3. Difundir mensajes de **mantenimiento programado** a todos los sistemas relacionados.

El requisito principal es que **todos los consumidores reciban el mismo mensaje simultáneamente**, sin importar si necesitan procesarlo de manera diferente.

---

## Requerimiento

Se debe transmitir **el mismo mensaje** a **todos los consumidores conectados** sin filtrar ni enrutar con claves específicas. Por ejemplo:

- Cuando un **evento en vivo** comienza, el mensaje debe llegar a:
    - El microservicio de estadísticas para registrar visualizaciones.
    - El servicio de chat en vivo para activar el canal.
    - El sistema de publicidad para iniciar campañas predefinidas.

---

## Por qué utilizar el tipo de Exchange "Fanout"

El **Exchange de tipo "Fanout"** es ideal para este escenario porque permite enviar el mensaje a **todas las colas vinculadas**, independientemente de la clave de enrutamiento (routing key). Esto garantiza que **todos los consumidores reciban el mismo mensaje al mismo tiempo**.

---

## Flujo de Trabajo

1. **Productor**: El servidor de streaming publica un mensaje al **Exchange Fanout** cuando ocurre un evento, como "transmisión iniciada" o "stream finalizado".

2. **Exchange Fanout**: El Exchange recibe el mensaje y lo reenvía a **todas las colas vinculadas**, sin importar las claves de enrutamiento.

3. **Consumidores**:
    - **Servicio de Estadísticas**: Procesa el mensaje para registrar el inicio de la transmisión.
    - **Servicio de Chat en Vivo**: Activa las funcionalidades de chat para los espectadores.
    - **Servicio de Publicidad**: Inicia anuncios o campañas durante la transmisión.
    - **Servicio de Notificaciones Push**: Envía alertas a todos los usuarios suscritos.

---

## Ejemplo de Configuración

- **Exchange**: `stream_events_exchange` (tipo Fanout).
- **Colas Vinculadas**:
    - `stats_queue` → Servicio de estadísticas.
    - `chat_queue` → Servicio de chat en vivo.
    - `ads_queue` → Servicio de publicidad.
    - `notifications_queue` → Servicio de notificaciones push.

---

## Beneficios

1. **Difusión simultánea**: Todos los consumidores reciben el mensaje al mismo tiempo, asegurando sincronización.
2. **Simplicidad**: No se necesita configurar claves de enrutamiento; basta con vincular las colas al Exchange.
3. **Eficiencia en la distribución**: El productor solo publica una vez, y RabbitMQ se encarga de transmitirlo a todas las colas.
4. **Escalabilidad**: Es fácil agregar nuevos servicios o colas para recibir el mensaje sin modificar la lógica existente.

---

## Resumen

El tipo de Exchange **Fanout** en RabbitMQ es ideal cuando se necesita **difundir un mismo mensaje a múltiples consumidores** simultáneamente, como en un sistema de transmisión de eventos en tiempo real. Garantiza una distribución eficiente y sincronizada del mensaje a todos los servicios conectados.
