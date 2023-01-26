package com.wgf.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文章目录 6.3
 * 重试需要把手动ack打开
 * MessageRecoverer#recover  当重试次数达到配置上限时的回调策略，可以投递到其他队列或者丢弃
 */
//@Configuration
public class RetryConfig {
    public static final String RETRY_EXCHANGE = "retry.queue";
    public static final String RETRY_QUEUE    = "retry.queue";
    public static final String ROUTING_KEY    = "retry";

    public static final String ERROR_QUEUE       = "retry.error";
    public static final String ERROR_ROUTING_KEY = "error";

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange(RETRY_EXCHANGE);
    }

    @Bean
    public Queue retryQueue() {
        return new Queue(RETRY_QUEUE);
    }

    @Bean
    public Binding bindRetryQueue(Queue retryQueue, DirectExchange retryExchange) {
        return BindingBuilder.bind(retryQueue).to(retryExchange).with(ROUTING_KEY);
    }

    /**
     * 异常队列
     *
     * @return
     */
    @Bean
    public Queue errorQueue() {
        return new Queue(ERROR_QUEUE);
    }

    /**
     * 交换机绑定异常队列
     *
     * @param errorQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindErrorQueue(Queue errorQueue, DirectExchange retryExchange) {
        return BindingBuilder.bind(errorQueue).to(retryExchange).with(ERROR_ROUTING_KEY);
    }


    /**
     * 调试时，配置打开
     * MessageRecoverer 的实现类。达到重试次数后将消息丢第到异常队列
     *
     * @return
     */
   // @Bean
    public MessageRecoverer republishMessageRecoverer() {
        // 重试次数上限将消息丢到异常队列
        return new RepublishMessageRecoverer(rabbitTemplate, RETRY_EXCHANGE, ERROR_ROUTING_KEY);
    }
}
