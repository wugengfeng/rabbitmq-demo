package com.wgf.demo.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class BasicTest {
    protected ConnectionFactory connectionFactory;
    protected Connection connection;
    protected Channel channel;

    @BeforeEach
    public void init() throws IOException, TimeoutException {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("rabbitmq");
        connectionFactory.setPassword("wgf123");
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
    }
}
