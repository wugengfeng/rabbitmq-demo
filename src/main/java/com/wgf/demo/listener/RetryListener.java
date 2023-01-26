package com.wgf.demo.listener;

import com.rabbitmq.client.Channel;
import com.wgf.demo.config.RetryConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文章目录 6.3
 * 消费重试
 */
@Slf4j
//@Component
public class RetryListener {
    private AtomicInteger count = new AtomicInteger(0);

    @RabbitListener(queues = RetryConfig.RETRY_QUEUE)
    public void listener(String message, Channel channel, Message messageEntity) throws IOException {
        log.info("消费次数：{}", count.incrementAndGet());
        // 必须抛出异常才能触发重试次数
        throw new RuntimeException();
    }
}
