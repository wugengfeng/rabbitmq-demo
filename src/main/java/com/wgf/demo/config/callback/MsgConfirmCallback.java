package com.wgf.demo.config.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


/**
 * 文章目录：4
 *
 * @Author wgf
 * @Version 1.0
 * 描述：消息可靠性配置 消息投递到交换机ACK确认回调。
 * 如果消息成功投递到交换机，则ack为true
 */
@Slf4j
@Component
public class MsgConfirmCallback implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 如果发送到交换器都没有成功（比如说删除了交换器），ack 返回值为 false
        // 如果发送到交换器成功，但是没有匹配的队列（比如说取消了绑定），ack 返回值为还是 true （这是一个坑，需要注意）

        // 获取消息id
        //String messageId = correlationData.getId();
        //log.info("ConfirmCallback , correlationData = {} , ack = {} , cause = {} ", correlationData, ack, cause);
    }
}
