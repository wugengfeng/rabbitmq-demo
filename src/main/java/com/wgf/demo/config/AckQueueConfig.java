package com.wgf.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文章目录 5.2
 */
@Configuration
public class AckQueueConfig {
    public static final String ACK_QUEUE   = "ack.queue";
    public static final String ROUTING_KEY = "ack";

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue ackQueue() {
        return new Queue(ACK_QUEUE);
    }


    /**
     * 绑定队列
     *
     * @param directQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindAckQueue(Queue ackQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(ackQueue).to(directExchange).with(ROUTING_KEY);
    }
}
