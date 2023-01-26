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
 * 文章目录：9.3
 * 惰性队列
 */
//@Configuration
public class LazyConfig {
    public static final String LAZY_QUEUE  = "lazy.queue";
    public static final String LAZY_EXCHANGE  = "lazy_exchange";
    public static final String ROUTING_KEY = "lazy";

    /**
     * 创建惰性队列
     *
     * @return
     */
    @Bean
    public Queue lazyQueue() {

        // 延迟交换机类型
        Map<String, Object> args = new HashMap<>(8);
        args.put("x-queue-mode", "lazy");
        return new Queue(LAZY_QUEUE, false, false, false, args);
    }

    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    public DirectExchange lazyExchange() {
        return new DirectExchange(LAZY_EXCHANGE);
    }


    /**
     * 队列绑定
     * @param directQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindLazyQueue(Queue lazyQueue, DirectExchange lazyExchange) {
        return BindingBuilder.bind(lazyQueue).to(lazyExchange).with(ROUTING_KEY);
    }
}
