package com.wgf.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 文章目录 7.5.2
 * 延迟队列配置
 */
//@Configuration
public class DelayedConfig {
    public static final String DELAYED_EXCHANGE = "delayed_exchange";
    public static final String DELAYED_QUEUE  = "delayed.queue";
    public static final String DELAYED_ROUTING_KEY   = "Delayed";

    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE);
    }

    @Bean
    public CustomExchange delayedExchange() {

        // 延迟交换机类型
        Map<String, Object> args = new HashMap<>(8);
        args.put("x-delayed-type", "direct");

        /**
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否需要持久化
         * 4.是否自动删除
         * 5.其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message",true, false, args);
    }

    /**
     * 延迟交换机绑定队列
     * @param delayedQueue
     * @param delayedExchange
     * @return
     */
    @Bean
    public Binding bindDelayedQueue(Queue delayedQueue, CustomExchange delayedExchange) {
         return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
