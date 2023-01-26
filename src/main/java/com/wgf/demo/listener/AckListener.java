package com.wgf.demo.listener;

import com.rabbitmq.client.Channel;
import com.wgf.demo.config.AckQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 文章目录 5.2
 * 手动ack确认消息消费
 */
@Slf4j
//@Component
public class AckListener {

    @RabbitListener(queues = AckQueueConfig.ACK_QUEUE)
    public void listener(String message, Channel channel, Message messageEntity) {
        int num = Integer.valueOf(message);

        try {
            // 让值大于5抛异常进行Nack, 消息就会重回队列头部进行下次消费，这里的代码num>5会是一个死循环消费
            if (num >= 5) {
                throw new RuntimeException();
            }

            log.info("队列：{} 接收到消息：{}", AckQueueConfig.ACK_QUEUE, message);

            /**
             * 消息消费确认
             * 如果客户端在线没有签收这条Message，则此消息进入Unacked状态，此时监听器阻塞等待消息确认，不推送新Message
             * 如果待消息确认并且客户端下线，下次客户端上线重新推送上次Unacked状态Message
             */
            channel.basicAck(messageEntity.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("消费消息异常", e);

            /**
             * 第一个参数deliveryTag：发布的每一条消息都会获得一个唯一的deliveryTag，deliveryTag在channel范围内是唯一的
             * 第二个参数multiple：批量确认标志。如果值为true，包含本条消息在内的、所有比该消息deliveryTag值小的 消息都被拒绝了（除了已经被 ack 的以外）;如果值为false，只拒绝本条消息
             * 第三个参数requeue：表示如何处理这条消息，如果值为true，则重新放入RabbitMQ的发送队列，如果值为false，则通知RabbitMQ销毁这条消息
             */
            try {
                channel.basicNack(messageEntity.getMessageProperties().getDeliveryTag(), false,true);
            } catch (IOException ex) {
                // TODO 重试逻辑或业务补偿（发往死信队列或Redis等）
                log.error("nack失败", ex);
            }
        }
    }
}
