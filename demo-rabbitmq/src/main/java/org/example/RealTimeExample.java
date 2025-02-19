package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RealTimeExample {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        JSONObject json = new JSONObject();
        json.put("from_date", "01-jan-2024");
        json.put("to_date", "31-dec-2024");
        json.put("email", "test@gmail.com");
        json.put("query", "select * from data");

        channel.basicPublish("", "Queue-1", null, json.toString().getBytes(StandardCharsets.UTF_8));
        channel.close();
        connection.close();
    }
}