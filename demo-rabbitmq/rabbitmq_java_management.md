# Guía Práctica: Gestión de Exchanges, Queues y Mensajes en RabbitMQ usando Java

---

## Introducción

RabbitMQ es un sistema de mensajería basado en el protocolo **AMQP**. Para interactuar con RabbitMQ desde **Java**, utilizaremos la **librería oficial** `amqp-client`. Esta guía explica cómo:

1. **Crear exchanges y queues**.
2. **Vincular exchanges y queues usando routing keys**.
3. **Eliminar exchanges, queues y mensajes**.

---

## Configuración Inicial

### Dependencias

Asegúrate de agregar la librería **RabbitMQ Client** en tu archivo `pom.xml` si usas Maven:

```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.15.0</version> <!-- Usa la última versión estable -->
</dependency>
```

---

## Crear una Conexión con RabbitMQ

Antes de realizar cualquier operación, necesitas establecer una conexión y crear un canal.

```java
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

public class RabbitMQUtil {

    public static Connection createConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Host de RabbitMQ
        factory.setPort(5672);        // Puerto por defecto
        factory.setUsername("guest");
        factory.setPassword("guest");

        return factory.newConnection();
    }
}
```
---

## 1. Crear Exchanges, Queues y Routing Keys

### 1.1 Crear un Exchange y una Queue

El siguiente código crea un Exchange de tipo `direct`, una Queue, y las vincula usando una routing key.

```java
public class RabbitMQSetup {
    private static final String EXCHANGE_NAME = "my_direct_exchange";
    private static final String QUEUE_NAME = "my_queue";
    private static final String ROUTING_KEY = "my_routing_key";

    public static void main(String[] args) throws Exception {
        try (Connection connection = RabbitMQUtil.createConnection();
             Channel channel = connection.createChannel()) {

            // Declarar un Exchange
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
            System.out.println("Exchange creado: " + EXCHANGE_NAME);

            // Declarar una Queue
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println("Queue creada: " + QUEUE_NAME);

            // Vincular la Queue al Exchange con una Routing Key
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            System.out.println("Queue vinculada con Routing Key: " + ROUTING_KEY);
        }
    }
}
```

## 2. Eliminar Queues y Exchanges

### 2.1 Eliminar una Queue

```java
public class DeleteQueue {
    private static final String QUEUE_NAME = "my_queue";

    public static void main(String[] args) throws Exception {
        try (Connection connection = RabbitMQUtil.createConnection();
             Channel channel = connection.createChannel()) {

            // Eliminar la Queue
            channel.queueDelete(QUEUE_NAME);
            System.out.println("Queue eliminada: " + QUEUE_NAME);
        }
    }
}
```

### 2.2 Eliminar un Exchange
Para eliminar un Exchange, usamos el método `exchangeDelete`.

```java
public class DeleteExchange {
    private static final String EXCHANGE_NAME = "my_direct_exchange";

    public static void main(String[] args) throws Exception {
        try (Connection connection = RabbitMQUtil.createConnection();
             Channel channel = connection.createChannel()) {

            // Eliminar el Exchange
            channel.exchangeDelete(EXCHANGE_NAME);
            System.out.println("Exchange eliminado: " + EXCHANGE_NAME);
        }
    }
}
```

## 3. Eliminar Mensajes de una Queue

Para purgar los mensajes de una Queue, usamos el método queuePurge. Esto vacía todos los mensajes sin eliminar la Queue.

```java
public class PurgeQueue {
    private static final String QUEUE_NAME = "my_queue";

    public static void main(String[] args) throws Exception {
        try (Connection connection = RabbitMQUtil.createConnection();
             Channel channel = connection.createChannel()) {

            // Purgar los mensajes de la Queue
            channel.queuePurge(QUEUE_NAME);
            System.out.println("Mensajes eliminados de la Queue: " + QUEUE_NAME);
        }
    }
}
```

## 4. Enviar y Recibir Mensajes (Opcional)

### 4.1 Enviar Mensajes a un Exchange

```java
public class SendMessage {
    private static final String EXCHANGE_NAME = "my_direct_exchange";
    private static final String ROUTING_KEY = "my_routing_key";

    public static void main(String[] args) throws Exception {
        try (Connection connection = RabbitMQUtil.createConnection();
             Channel channel = connection.createChannel()) {

            String message = "Hola, RabbitMQ!";
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes());
            System.out.println("Mensaje enviado: " + message);
        }
    }
}
```

### 4.2 Recibir Mensajes de una Queue

```java
public class ReceiveMessage {
    private static final String QUEUE_NAME = "my_queue";

    public static void main(String[] args) throws Exception {
        try (Connection connection = RabbitMQUtil.createConnection();
             Channel channel = connection.createChannel()) {

            channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Mensaje recibido: " + message);
            }, consumerTag -> { });
        }
    }
}
```