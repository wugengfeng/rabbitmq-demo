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
 * 文章目录 8.2
 * 优先级队列
 */
@Configuration
public class PriorityConfig {
    public static final String PRIORITY_QUEUE = "priority.queue";
    public static final String PRIORITY_EXCHANGE = "priority.queue";
    public static final String ROUTING_KEY = "priority";

    /**
     * 创建队列设置优先级
     */
    @Bean
    public Queue priorityQueue() {
        Map<String, Object> args = new HashMap<>(8);
        // 设置队列的优先级，取值[0~255] 建议[0~10], 如果有消息堆积则会按照优先级排序，越高的越快被消费
        args.put("x-max-priority", 10);
        return new Queue(PRIORITY_QUEUE, true, false, false, args);
    }

    /**
     * 创建交换机
     */
    @Bean
    public DirectExchange priorityExchange() {
        // 默认持久化
        return new DirectExchange(PRIORITY_EXCHANGE, true, false);
    }

    /**
     * 绑定队列
     *
     * @param directQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindPriorityQueue(Queue priorityQueue, DirectExchange priorityExchange) {
        return BindingBuilder.bind(priorityQueue).to(priorityExchange).with(ROUTING_KEY);
    }
}
