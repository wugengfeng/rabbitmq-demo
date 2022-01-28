package com.wgf.demo;

import com.wgf.demo.config.*;
import com.wgf.demo.listener.PullMsgService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class QeueuTest {
    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    PullMsgService pullMsgService;

    /**
     * 文章目录：3
     * 简单发送消息
     * @throws InterruptedException
     */
    @Test
    public void simpleSend() throws InterruptedException {
        for (int i = 1; i <= 20; i++) {
            amqpTemplate.convertAndSend(QueueConfig.QUEUE_NAME, i);
        }
    }


    /**
     * 文章目录：2.1.4.1.2
     * 手动 pull 消息数据准备
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 2005; i++) {
            amqpTemplate.convertAndSend(QueueConfig.BASIC_GET, i + "");
        }
    }


    /**
     * 文章目录：2.1.4.1.2
     * 手动拉取消息
     */
    @Test
    public void recive() {
        pullMsgService.pull(QueueConfig.BASIC_GET, 200, 1000, (list) -> System.out.println(list.size()));
    }


    /**
     * 文章目录：2.1.2.1.1
     */
    @Test
    public void sendDirect() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            amqpTemplate.convertAndSend(DirectConfig.DIRECT_EXCHANGE, DirectConfig.ROUTING_KEY, i+ "");
        }
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * 文章目录：2.1.2.1.2
     */
    @Test
    public void sendTopic() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            amqpTemplate.convertAndSend(TopicConfig.TOPIC_EXCHANGE, TopicConfig.ROUTING_KEY, i+ "");
        }
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 文章目录：2.1.2.1.3
     */
    @Test
    public void sendFanout() throws InterruptedException {
        amqpTemplate.convertAndSend(FanoutConfig.FANOUT_EXCHANGE, null, "test");
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 文章目录：2.1.2.1.4
     */
    @Test
    public void sendHeaders() throws InterruptedException {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("color", "black");
        SimpleMessageConverter messageConverter = new SimpleMessageConverter();
        Message                message          = messageConverter.toMessage("test1", messageProperties);
        amqpTemplate.convertAndSend(HeadersConfig.HEADERS_EXCHANGE, null, message);

        messageProperties.setHeader("aging", "fast");
        message          = messageConverter.toMessage("test2", messageProperties);
        amqpTemplate.convertAndSend(HeadersConfig.HEADERS_EXCHANGE, null, message);
    }
}
