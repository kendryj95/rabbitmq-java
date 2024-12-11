package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerJSON {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            JSONObject jsonObject = new JSONObject(message);
            System.out.println("FROM DATE = " + jsonObject.get("from_date"));
            System.out.println("TO DATE = " + jsonObject.get("to_date"));
            System.out.println("QUERY = " + jsonObject.get("query"));
            System.out.println("EMAIL = " + jsonObject.get("email"));
        };

        channel.basicConsume("Queue-1", true, deliverCallback, consumerTag -> {});
    }
}