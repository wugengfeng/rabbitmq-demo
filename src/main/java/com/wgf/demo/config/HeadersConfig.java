package com.wgf.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 文章目录：2.1.2.1.4
 * @description: 直连交换机配置
 * @author: ken 😃
 * @create: 2022-01-28 11:24
 **/
//@Configuration
public class HeadersConfig {
    public static final String HEADERS_EXCHANGE = "headers_exchange";
    public static final String HEADERS_QUEUE_1  = "headers.queue1";
    public static final String HEADERS_QUEUE_2  = "headers.queue2";

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue headersQueue1() {
        return new Queue(HEADERS_QUEUE_1);
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue headersQueue2() {
        return new Queue(HEADERS_QUEUE_2);
    }

    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    /**
     * 绑定队列
     * @param directQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindHeadersQueue1(Queue headersQueue1, HeadersExchange headersExchange) {
        // 消息头属性
        Map<String,Object> headerMap = new HashMap<>(8);
        headerMap.put("color", "black");
        headerMap.put("aging", "fast");
        // whereAny 其中一项匹配即可
        return BindingBuilder.bind(headersQueue1).to(headersExchange).whereAny(headerMap).match();
    }

    /**
     * 绑定队列
     * @param directQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding bindHeadersQueue2(Queue headersQueue2, HeadersExchange headersExchange) {
        // 消息头属性
        Map<String,Object> headerMap = new HashMap<>(8);
        headerMap.put("color", "black");
        headerMap.put("aging", "fast");
        // whereAll 完全匹配
        return BindingBuilder.bind(headersQueue2).to(headersExchange).whereAll(headerMap).match();
    }
}
