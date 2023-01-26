package com.wgf.demo.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// 发布消息
public class ProducerTest extends BasicTest{
    @Test
    @SneakyThrows
    public void send() {
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        String queueName = "test";
        String message = "Hello";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, null, message.getBytes());
    }
}
