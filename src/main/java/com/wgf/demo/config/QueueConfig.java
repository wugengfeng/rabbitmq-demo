package com.wgf.demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    public static final String QUEUE_NAME = "test_queue";

    /**
     * 手动拉取消息测试队列
     */
    public static final String BASIC_GET = "basic_get";

    /**
     * 简单定义消息队列
     * @return
     */
    @Bean
    public Queue testQueue() {
        // 不指定交换机则使用默认交换机将消息转发到队列
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Queue basicGetQueue() {
        // 不指定交换机则使用默认交换机将消息转发到队列
        return QueueBuilder.durable(BASIC_GET).build();
    }
}
