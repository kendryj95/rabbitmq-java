package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Publisher {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        String message = "2nd message from RabbitMQ";
        String[] messages = {"First","Second","Third","Fourth"};

        for (String message :
                messages) {
            channel.basicPublish("", "Queue-1", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Message published");
        }
        channel.close();
        connection.close();
    }
}