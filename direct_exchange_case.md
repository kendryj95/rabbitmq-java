# Caso de Uso: Sistema de Notificaciones Personalizadas en una Plataforma E-commerce

---

## Escenario

Una plataforma de **e-commerce** necesita un sistema de notificaciones eficiente para informar a los usuarios sobre **diferentes eventos**, como:

1. Confirmación de compra.
2. Actualización de estado del pedido (en camino, entregado).
3. Alertas de ofertas especiales según las preferencias del cliente.

La solución requiere enviar notificaciones a **múltiples microservicios** encargados de diferentes canales (email, SMS y notificaciones push).

---

## Requerimiento

Se debe asegurar que los mensajes lleguen **solamente al consumidor correcto**, según el **tipo de evento** o **canal** asociado. Por ejemplo:

- Una notificación de **compra confirmada** debe ir únicamente al servicio de envío de correos electrónicos.
- Una notificación de **pedido en camino** debe ser procesada por el microservicio de **SMS**.

---

## Por qué utilizar el tipo de Exchange "Direct"

El **Exchange de tipo "Direct"** es ideal para este escenario porque permite enrutar los mensajes **basados en una clave de enrutamiento exacta** (routing key). Esto asegura que cada mensaje llegue **únicamente al consumidor que necesita procesarlo**, evitando duplicidad y garantizando eficiencia.

---

## Flujo de Trabajo

1. **Productor**: El sistema de e-commerce publica un mensaje al **Exchange Direct** indicando el tipo de notificación. Por ejemplo:
    - `routing_key = "email.confirmation"` para correos de confirmación.
    - `routing_key = "sms.status_update"` para actualizaciones por SMS.

2. **Exchange Direct**: RabbitMQ analiza la clave de enrutamiento y la compara con las **colas** vinculadas.

3. **Consumidores**:
    - **Servicio de Email**: Escucha la cola con `binding_key = "email.confirmation"`.
    - **Servicio de SMS**: Escucha la cola con `binding_key = "sms.status_update"`.
    - **Servicio de Notificaciones Push**: Escucha otras claves según el evento.

---

## Ejemplo de Configuración

- **Exchange**: `notifications_exchange` (tipo Direct).
- **Routing Keys**:
    - `email.confirmation` → Cola: `email_queue`
    - `sms.status_update` → Cola: `sms_queue`
    - `push.offers` → Cola: `push_notifications_queue`

---

## Beneficios

1. **Enrutamiento preciso**: Cada mensaje es dirigido al consumidor adecuado, lo que evita sobrecarga y procesamiento innecesario.
2. **Escalabilidad**: Permite agregar nuevos tipos de notificaciones fácilmente al vincular nuevas colas con claves de enrutamiento específicas.
3. **Separación de responsabilidades**: Los microservicios (email, SMS, push) procesan únicamente los mensajes que les competen.

---

## Resumen

El tipo de Exchange **Direct** en RabbitMQ es ideal para este caso porque garantiza un enrutamiento **exacto** basado en **claves de enrutamiento**, logrando una distribución eficiente y ordenada de las notificaciones en el sistema de e-commerce.
