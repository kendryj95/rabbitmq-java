package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicPublisher {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String message = "Message for Mobile and AC";

        /**
         * Route keys bindings:
         * Queue Mobile: *.mobile.*
         * Queue TV: *.tv.*
         * Queue AC: #.ac
         */
        channel.basicPublish("Topic-Exchange", "tv.mobile.ac", null, message.getBytes(StandardCharsets.UTF_8));
        channel.close();
        connection.close();
    }
}