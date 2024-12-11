package org.example;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class HeadersPublisher {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String message = "Message for AC";

        /**
         * Args:
         * Queue Mobile: x-match=any; item1=mobile; item2=mob
         * Queue TV: x-match=any; item1=tv; item2=television
         * Queue AC: x-match=all; item1=mobile; item2=ac
         */
        Map<String, Object> headersArgs = new HashMap<String, Object>();
        headersArgs.put("item1", "mobile");
        headersArgs.put("item2", "television");

        BasicProperties basicProperties = new BasicProperties();
        basicProperties = basicProperties.builder().headers(headersArgs).build();

        channel.basicPublish("Headers-Exchange", "", basicProperties, message.getBytes(StandardCharsets.UTF_8));
        channel.close();
        connection.close();
    }
}