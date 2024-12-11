package com.kendryj95.rabbitmq.demo.app;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

@Service
public class RabbitConsumerService {

    /*@RabbitListener(queues = "Queue-1")
    public void getMessage(Person person) {
        System.out.printf("Hello %s!%n", person.getName());
    }*/

    @RabbitListener(queues = "Mobile")
    public void getMessage(byte[] message) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(message);
        ObjectInput in = new ObjectInputStream(bis);
        Person person = (Person) in.readObject();
        in.close();
        bis.close();
        System.out.printf("Hello %s!%n", person.getName());
    }
}
