package com.wgf.demo.listener;

import com.rabbitmq.client.Channel;
import com.wgf.demo.config.DeadConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
//@Component
public class DeadListener {
    /**
     * 监听死信队列
     */
    @RabbitListener(queues = DeadConfig.DEAD_QUEUE)
    public void deadListener(String message, Channel channel, Message messageEntity) throws IOException {
        log.info("死信队列接受到死信：{}", message);
        channel.basicAck(messageEntity.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * NACK 监听
     */
    @SneakyThrows
    @RabbitListener(queues = DeadConfig.NACK_QUEUE)
    public void listener(String message, Channel channel, Message messageEntity) throws IOException {
        // 拒收消息
        log.info("队列：{} 拒收消息：{}", DeadConfig.TTL_QUEUE, message);
        // 两种决绝方式都能重新投递死信队列
        channel.basicNack(messageEntity.getMessageProperties().getDeliveryTag(), false, false);
        // channel.basicReject(messageEntity.getMessageProperties().getDeliveryTag(), false);
    }
}
