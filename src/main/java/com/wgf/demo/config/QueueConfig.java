package com.wgf.demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class QueueConfig {
    public static final String QUEUE_NAME = "test_queue";
    public static final String TTL_QUEUE  = "ttl_queue";

    /**
     * 手动拉取消息测试队列
     */
    public static final String BASIC_GET = "basic_get";


    /**
     * 简单定义消息队列
     *
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


    /**
     * 文章目录：3.2.2
     * 测试消息的ttl
     *
     * @return
     */
    @Bean
    public Queue ttlQueue() {
        // 设置消息的ttl为5000ms
        Map<String, Object> arguments = new HashMap<>(8);
        arguments.put("x-message-ttl", 5000);
        arguments.put("x-expires", 10000);
        return new Queue("ttl_queue", true, false, false, arguments);
    }
}
