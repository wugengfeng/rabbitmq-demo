package com.wgf.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 备份交换机配置
 */
//@Component
public class BackUpConfig {
    /**
     * 备份交换机配置
     */
    public static final String BACKUP_EXCHANGE = "backup";
    public static final String BACKUP_QUEUE    = "backup.queue";
    public static final String WARING_QUEUE    = "waring.queue";

    /**
     * 备份队列
     *
     * @return
     */
    @Bean
    public Queue backupQueue() {
        return new Queue(BACKUP_QUEUE);
    }

    /**
     * 警告队列
     *
     * @return
     */
    @Bean
    public Queue waringQueue() {
        return new Queue(WARING_QUEUE);
    }

    /**
     * 备份交换机
     *
     * @return
     */
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    /**
     * 备份交换机绑定报警队列
     *
     * @param fanoutQueue1
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding bindWaringQueue(Queue waringQueue, FanoutExchange backupExchange) {
        // 不需要 routing_key
        return BindingBuilder.bind(waringQueue).to(backupExchange);
    }

    /**
     * 备份交换机绑定报警队列
     *
     * @param fanoutQueue1
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding bindBackupQueue(Queue backupQueue, FanoutExchange backupExchange) {
        // 不需要 routing_key
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    /**
     * 正常交换机队列配置
     */
    public static final String EXCHANGE    = "product_exchange";
    public static final String QUEUE       = "product.queue";
    public static final String ROUTING_KEY = "product";

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue productQueue() {
        return new Queue(QUEUE);
    }

    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    public DirectExchange productExchange() {
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange(EXCHANGE)
                .durable(true)
                // 指定交换机的备份交换机
                .withArgument("alternate-exchange", BACKUP_EXCHANGE);
        return exchangeBuilder.build();
    }

    /**
     * 正常交换机绑定商品队列
     * @param productQueue
     * @param productExchange
     * @return
     */
    @Bean
    public Binding bindProductQueue(Queue productQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productQueue).to(productExchange).with(ROUTING_KEY);
    }
}
