package com.wgf.demo.listener;

import com.rabbitmq.client.Channel;
import com.wgf.demo.config.DeadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DeadListener {

    @RabbitListener(queues = DeadConfig.ORDER_QUEUE)
    public void listener(String message, Channel channel, Message messageEntity) throws IOException {
        // 拒收消息
        log.info("队列：{} 拒收消息：{}", DeadConfig.ORDER_QUEUE, message);
        // 两种决绝方式都能重新投递死信队列
        channel.basicNack(messageEntity.getMessageProperties().getDeliveryTag(), false, false);
        // channel.basicReject(messageEntity.getMessageProperties().getDeliveryTag(), false);
    }

}
