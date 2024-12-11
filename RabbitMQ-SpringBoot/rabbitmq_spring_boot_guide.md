# Guía Práctica: Gestión de Exchanges, Queues y Mensajes en RabbitMQ con Spring Boot

---

## Introducción

RabbitMQ es un broker de mensajes basado en **AMQP (Advanced Message Queuing Protocol)**. Spring Boot simplifica su integración gracias al módulo **Spring AMQP**.

En esta guía aprenderás a:

1. **Configurar RabbitMQ en Spring Boot**.
2. **Crear exchanges, queues y binding con routing keys**.
3. **Eliminar exchanges, queues y mensajes**.

---

## 1. Configuración Inicial

### Dependencias en `pom.xml`

Asegúrate de agregar las dependencias necesarias para **Spring Boot** y **Spring AMQP**:

```xml
<dependencies>
    <!-- Starter AMQP -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>

    <!-- Starter Web (para pruebas opcionales) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Starter Test (opcional para pruebas) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Configuración en `application.properties`
```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

## Configuración de Exchanges, Queues y Bindings
En Spring Boot, podemos definir exchanges, queues y sus bindings de manera declarativa.

### 2.1 Clase de Configuración

```java
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres de componentes
    private static final String EXCHANGE_NAME = "my_direct_exchange";
    private static final String QUEUE_NAME = "my_queue";
    private static final String ROUTING_KEY = "my_routing_key";

    // Declarar el Exchange
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // Declarar la Queue
    @Bean
    public Queue myQueue() {
        return new Queue(QUEUE_NAME, true); // durable=true
    }

    // Vincular la Queue al Exchange con una Routing Key
    @Bean
    public Binding binding(Queue myQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(myQueue).to(directExchange).with(ROUTING_KEY);
    }
}
```

## 3. Enviar Mensajes a una Queue

### 3.1 Productor (Sender)
Crea un Productor para enviar mensajes a la Queue a través del Exchange.

```java
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    private static final String EXCHANGE_NAME = "my_direct_exchange";
    private static final String ROUTING_KEY = "my_routing_key";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
        System.out.println("Mensaje enviado: " + message);
    }
}
```

## 4. Recibir Mensajes de una Queue
### 4.1 Consumidor (Listener)
Usa la anotación `@RabbitListener` para escuchar mensajes de la Queue.

```java
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQReceiver {

    private static final String QUEUE_NAME = "my_queue";

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Mensaje recibido: " + message);
    }
}
```

## 5. Eliminar Exchanges, Queues y Mensajes

### 5.1 Eliminar una Queue o un Exchange desde el Código

Podemos usar RabbitAdmin para realizar operaciones administrativas como eliminar queues y exchanges.

```java
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQAdminConfig {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    // Eliminar una Queue
    @Bean
    public void deleteQueue() {
        rabbitAdmin.deleteQueue("my_queue");
        System.out.println("Queue eliminada: my_queue");
    }

    // Eliminar un Exchange
    @Bean
    public void deleteExchange() {
        rabbitAdmin.deleteExchange("my_direct_exchange");
        System.out.println("Exchange eliminado: my_direct_exchange");
    }

    // Purgar mensajes de una Queue
    @Bean
    public void purgeQueue() {
        rabbitAdmin.purgeQueue("my_queue", false);
        System.out.println("Mensajes purgados de la Queue: my_queue");
    }
}
```

## 6. Ejemplo de Prueba

Puedes crear un controlador REST para probar el envío de mensajes.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQController {

    @Autowired
    private RabbitMQSender sender;

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        sender.sendMessage(message);
        return "Mensaje enviado: " + message;
    }
}
```

Lanza la aplicación con `mvn springboot:run` y prueba el envío de mensajes llamando a la siguiente URL:

```
http://localhost:8080/send?message=HolaRabbitMQ
```