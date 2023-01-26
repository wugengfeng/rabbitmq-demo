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
 * 文章目录：7
 * 死信队列配置
 */
//@Configuration
public class DeadConfig {

    /**
     * 死信配置
     */
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String DEAD_QUEUE = "dead.queue";
    public static final String DEAD_ROUTING_KEY = "dead";

    /**
     * 正常交换机与队列配置
     */
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    /**
     * 有TTL过期的队列
     */
    public static final String TTL_QUEUE = "dead.ttl.queue";
    public static final String TTL_ROUTING_KEY = "ttl";

    /**
     * 有长度的队列
     */
    public static final String LENGTH_QUEUE = "dead.length.queue";
    public static final String LENGTH_ROUTING_KEY = "length";

    /**
     * 拒绝队列
     */
    public static final String NACK_QUEUE = "dead.nack.queue";
    public static final String NACK_ROUTING_KEY = "nack";

    /**
     * 创建死信交换机
     *
     * @return
     */
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    /**
     * 创建死信队列
     *
     * @return
     */
    @Bean
    public Queue deadQueue() {
        return new Queue(DEAD_QUEUE);
    }

    /**
     * 绑定死信队列
     *
     * @param directQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindDeadtQueue(Queue deadQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(DEAD_ROUTING_KEY);
    }

    /**
     * 正常交换机
     *
     * @return
     */
    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    /**
     * TTL过期队列
     */
    @Bean
    public Queue deadTtlQueue() {
        // 正常队列绑定死信队列
        Map<String, Object> args = new HashMap<>(8);
        // 设置TTL过期
        args.put("x-message-ttl", 10000);

        // 声明死信队列路由键
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        // 声明死信队列交换机，消息异常投递到此交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        return new Queue(TTL_QUEUE, true, false, false, args);
    }

    /**
     * 有长度限制的队列
     * @return
     */
    @Bean
    public Queue deadLengthQueue() {
        // 正常队列绑定死信队列
        Map<String, Object> args = new HashMap<>(8);

        // 设置队列最大长度
        args.put("x-max-length", 4);

        // 声明死信队列路由键
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        // 声明死信队列交换机，消息异常投递到此交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        return new Queue(LENGTH_QUEUE, true, false, false, args);
    }

    /**
     * 拒绝队列，监听时NACK
     * @return
     */
    @Bean
    public Queue deadNackQueue() {
        // 正常队列绑定死信队列
        Map<String, Object> args = new HashMap<>(8);
        // 声明死信队列路由键
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        // 声明死信队列交换机，消息异常投递到此交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        return new Queue(NACK_QUEUE, true, false, false, args);
    }

    /**
     * TTL队列绑定交换机
     *
     * @param deadTtlQueue
     * @param normalExchange
     * @return
     */
    @Bean
    public Binding bingDeadTtlQueue(Queue deadTtlQueue, DirectExchange normalExchange) {
        return BindingBuilder.bind(deadTtlQueue).to(normalExchange).with(TTL_ROUTING_KEY);
    }

    /**
     * LENGTH队列绑定交换机
     * @param deadLengthQueue
     * @param normalExchange
     * @return
     */
    @Bean
    public Binding bingDeadLengthQueue(Queue deadLengthQueue, DirectExchange normalExchange) {
        return BindingBuilder.bind(deadLengthQueue).to(normalExchange).with(LENGTH_ROUTING_KEY);
    }

    /**
     * NACK队列绑定交换机
     * @param deadNackQueue
     * @param normalExchange
     * @return
     */
    @Bean
    public Binding bingDeadNackQueue(Queue deadNackQueue, DirectExchange normalExchange) {
        return BindingBuilder.bind(deadNackQueue).to(normalExchange).with(NACK_ROUTING_KEY);
    }
}
