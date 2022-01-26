package com.wgf.demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    public static final String QUEUE_NAME = "test_queue";

    /**
     * 简单定义消息队列
     * @return
     */
    @Bean
    public Queue simpleQueue() {
        // 不指定交换机则使用默认交换机将消息转发到队列
        return QueueBuilder.durable("test_queue").build();
    }
}
