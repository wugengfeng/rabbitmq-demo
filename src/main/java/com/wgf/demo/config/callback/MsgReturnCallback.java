package com.wgf.demo.config.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


/**
 * 文章目录：4
 * 可以确认消息从EXchange路由到Queue失败
 * 如果正常投递到队列，不会回调ReturnCallback
 */
@Slf4j
@Component
public class MsgReturnCallback implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        // 获取消息id
        // String   messageId = message.getMessageProperties().getMessageId();
        //log.info("ReturnCallback unroutable messages, message = {} , replyCode = {} , replyText = {} , exchange = {} , routingKey = {} ", message, replyCode, replyText, exchange, routingKey);
    }
}
