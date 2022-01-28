package com.wgf.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文章目录：2.1.2.1.1
 * @description: 直连交换机配置
 * @author: ken 😃
 * @create: 2022-01-28 11:24
 **/
@Configuration
public class DirectConfig {
    public static final String DIRECT_EXCHANGE = "direct_exchange";
    public static final String DIRECT_QUEUE  = "direct.queue";
    public static final String ROUTING_KEY  = "queue";

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue directQueue() {
        return new Queue(DIRECT_QUEUE);
    }

    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    /**
     * 绑定队列
     * @param directQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindDirectQueue(Queue directQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue).to(directExchange).with(ROUTING_KEY);
    }
}
