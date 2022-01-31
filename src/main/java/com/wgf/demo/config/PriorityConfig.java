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
    public static final String PRIORITY_QUEUE  = "Priority.queue";
    public static final String ROUTING_KEY  = "Priority";

    @Bean
    public Queue priorityQueue() {
        Map<String, Object> args = new HashMap<>(8);
        // 设置队列的优先级，取值[0~255] 建议[0~10], 如果有消息堆积则会按照优先级排序，越高的越快被消费
        args.put("x-max-priority", 10);
        return new Queue(PRIORITY_QUEUE, true, false, false, args);
    }


    /**
     * 绑定队列
     * @param directQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindPriorityQueue(Queue priorityQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(priorityQueue).to(directExchange).with(ROUTING_KEY);
    }
}
