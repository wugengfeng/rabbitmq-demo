package com.wgf.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文章目录：2.1.2.1.3
 * @description: 扇形交换机配置
 * @author: ken 😃
 * @create: 2022-01-28 11:24
 **/
//@Configuration
public class FanoutConfig {
    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    public static final String FANOUT_QUEUE_1  = "fanout.queue_1";
    public static final String FANOUT_QUEUE_2  = "fanout.queue_2";

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue fanoutQueue1() {
        return new Queue(FANOUT_QUEUE_1);
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue fanoutQueue2() {
        return new Queue(FANOUT_QUEUE_2);
    }

    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    /**
     * 绑定队列
     * @param fanoutQueue1
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding bindFanoutQueue1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
        // 不需要 routing_key
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    /**
     * 绑定队列
     * @param fanoutQueue2
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding bindFanoutQueue(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        // 不需要 routing_key
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
