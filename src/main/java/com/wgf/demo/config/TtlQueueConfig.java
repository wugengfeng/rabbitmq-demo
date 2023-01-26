package com.wgf.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 原生延迟队列
 */
//@Configuration
public class TtlQueueConfig {
    /**
     * 死信队列
     */
    public static final String DEAD_EXCHANGE = "dead_ttl_exchange";
    public static final String DEAD_QUEUE = "dead.ttl.overdue.queue";
    public static final String DEAD_ROUTING_KEY = "overdue";

    /**
     * 正常队列，发送消息时，在消息设置TTL过期
     */
    public static final String TTL_EXCHANGE = "ttl_exchange";
    public static final String TTL_QUEUE = "ttl.queue";
    public static final String TTL_ROUTING_KEY = "ttl";

    /**
     * 创建死信交换机
     */
    @Bean
    public DirectExchange deadTtlExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    /**
     * 创建死信队列，正常队列TTL过期将死信转发到死信队列
     */
    @Bean
    public Queue deadTtlOverdueQueue() {
        return new Queue(DEAD_QUEUE);
    }

    /**
     * 绑定死信队列
     */
    @Bean
    public Binding bindDeadTtlOverdueQueue(Queue deadTtlOverdueQueue, DirectExchange deadTtlExchange) {
        return BindingBuilder.bind(deadTtlOverdueQueue).to(deadTtlExchange).with(DEAD_ROUTING_KEY);
    }

    /**
     * 创建正常交换机
     */
    @Bean
    public DirectExchange ttlExchange() {
        return new DirectExchange(TTL_EXCHANGE);
    }

    /**
     * 创建正常队列
     */
    @Bean
    public Queue ttlQueue() {
        // 正常队列绑定死信队列
        Map<String, Object> args = new HashMap<>(8);
        // 声明死信队列交换机，消息异常投递到此交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 声明死信队列路由键
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        return new Queue(TTL_QUEUE, true, false, false, args);
    }

    /**
     * 正常队列绑定交换机
     */
    @Bean
    public Binding bindTtlQueue(Queue ttlQueue, DirectExchange ttlExchange) {
        return BindingBuilder.bind(ttlQueue).to(ttlExchange).with(TTL_ROUTING_KEY);
    }
}
