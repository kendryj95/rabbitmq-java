# Caso de Uso: Sistema de Enrutamiento de Mensajes Basado en Metadatos para una Plataforma de Gestión de Pedidos

---

## Escenario

Una plataforma de **gestión de pedidos** necesita enrutar mensajes a diferentes microservicios dependiendo de **atributos específicos** incluidos en las cabeceras del mensaje. Los atributos pueden incluir:

1. **Tipo de cliente**: Regular, Premium, VIP.
2. **Ubicación**: Nacional, Internacional.
3. **Método de pago**: Tarjeta de crédito, PayPal, transferencia bancaria.

La solución debe enrutar mensajes de forma **dinámica** según las **cabeceras** sin depender de claves de enrutamiento.

---

## Requerimiento

Se requiere un sistema donde los mensajes sean dirigidos a diferentes colas según los **valores de las cabeceras** del mensaje, no según su cuerpo o claves predefinidas. Por ejemplo:

- Los pedidos de clientes **VIP** deben ser procesados por el **servicio prioritario**.
- Los pedidos **internacionales** deben ser manejados por el **servicio de logística internacional**.
- Los pedidos pagados con **PayPal** deben ser procesados por el servicio de **verificación de pagos externos**.

---

## Por qué utilizar el tipo de Exchange "Headers"

El **Exchange de tipo "Headers"** es ideal para este escenario porque enruta los mensajes basándose en las **cabeceras personalizadas** que el productor incluye en el mensaje. Permite:

- Uso de condiciones exactas o combinadas.
- Mayor flexibilidad que otros tipos de exchanges, ya que las cabeceras no están restringidas a un formato específico.

---

## Flujo de Trabajo

1. **Productor**: El sistema de gestión de pedidos publica un mensaje al **Exchange Headers** con **cabeceras personalizadas**. Ejemplo:
    - `{"customer_type": "VIP", "location": "international", "payment_method": "PayPal"}`

2. **Exchange Headers**: RabbitMQ enruta el mensaje a las colas vinculadas según las **condiciones configuradas** en las cabeceras.

3. **Consumidores**:
    - **Servicio Prioritario**: Procesa mensajes con `customer_type = VIP`.
    - **Servicio de Logística Internacional**: Procesa mensajes con `location = international`.
    - **Servicio de Pagos Externos**: Procesa mensajes con `payment_method = PayPal`.

---

## Ejemplo de Configuración

- **Exchange**: `orders_headers_exchange` (tipo Headers).
- **Cabeceras y Colas Vinculadas**:
    - `{"customer_type": "VIP"}` → Cola: `priority_orders_queue`.
    - `{"location": "international"}` → Cola: `international_shipping_queue`.
    - `{"payment_method": "PayPal"}` → Cola: `external_payments_queue`.

---

## Beneficios

1. **Enrutamiento flexible**: Los mensajes pueden ser enroutados usando **condiciones exactas o combinaciones** de valores en las cabeceras.
2. **Sin restricciones de formato**: No se requiere usar claves de enrutamiento específicas; las cabeceras pueden ser personalizadas según las necesidades.
3. **Procesamiento eficiente**: Cada servicio recibe únicamente los mensajes que coinciden con las cabeceras configuradas.
4. **Escalabilidad**: Es fácil agregar nuevas reglas de enrutamiento al definir condiciones en las cabeceras sin modificar la lógica del productor.

---

## Resumen

El tipo de Exchange **Headers** en RabbitMQ es ideal cuando se necesita enrutar mensajes basados en **atributos específicos** incluidos en las **cabeceras del mensaje**, proporcionando flexibilidad y control detallado sobre el enrutamiento. Es especialmente útil en sistemas complejos donde los mensajes deben ser procesados según metadatos personalizados.
