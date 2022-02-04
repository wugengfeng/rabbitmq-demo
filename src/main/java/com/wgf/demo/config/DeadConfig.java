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
@Configuration
public class DeadConfig {
    /**
     * 死信配置
     */
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String DEAD_QUEUE  = "dead.queue";
    public static final String DEAD_ROUTING_KEY   = "dead";

    /**
     * 正常交换机与队列配置
     */
    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String ORDER_QUEUE  = "order.queue";
    public static final String ORDER_ROUTING_KEY   = "order";

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
     * @return
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    /**
     *
     * 正常队列
     */
    @Bean
    public Queue orderQueue() {
        // 正常队列绑定死信队列
        Map<String, Object> args = new HashMap<>(8);
        // 设置TTL过期
        // args.put("x-message-ttl", 5000);

        // 设置队列最大长度
        // args.put("x-max-length", 4);

        // 声明死信队列路由键
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        // 声明死信队列交换机，消息异常投递到此交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        return new Queue(ORDER_QUEUE, true, false, false, args);
    }

    /**
     * 正常队列绑定交换机
     * @param orderQueue
     * @param orderExchange
     * @return
     */
    @Bean
    public Binding bingOrderQueue(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }


    /**
     * 文章目录 7.4
     */
    public static final String ORDER_QUEUE_2  = "order2.queue";
    public static final String ORDER_ROUTING_KEY_2   = "order2";
    
    @Bean
    public Queue order2Queue() {
        Map<String, Object> args = new HashMap<>(8);
        // 声明死信队列路由键
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        // 声明死信队列交换机，消息异常投递到此交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        return new Queue(ORDER_QUEUE_2, true, false, false, args);
    }


    /**
     * 正常队列绑定交换机
     * @param order2Queue
     * @param orderExchange
     * @return
     */
    @Bean
    public Binding bingOrder2Queue(Queue order2Queue, DirectExchange orderExchange) {
        return BindingBuilder.bind(order2Queue).to(orderExchange).with(ORDER_ROUTING_KEY_2);
    }
}
