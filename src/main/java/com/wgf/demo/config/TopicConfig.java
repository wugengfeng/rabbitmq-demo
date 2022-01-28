package com.wgf.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文章目录：2.1.2.1.2
 *
 * @description: topic类型交换机配置
 * *表示一个单词
 * #表示任意数量（零个或多个）单词
 * @author: ken 😃
 * @create: 2022-01-28 10:21
 **/
@Configuration
public class TopicConfig {

    public static final String TOPIC_EXCHANGE = "topic_exchange";
    public static final String TOPIC_QUEUE_1  = "topic.queue1";
    public static final String TOPIC_QUEUE_2  = "topic.queue2";
    public static final String ROUTING_KEY    = "topic.test";

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE_1);
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE_2);
    }

    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * 绑定队列
     *
     * @param topicQueue1
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding bindTopicQueue1(Queue topicQueue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with(ROUTING_KEY);
    }

    /**
     * 绑定队列
     *
     * @param topicQueue1
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding bindTopicQueue2(Queue topicQueue2, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with("topic.*");
    }
}