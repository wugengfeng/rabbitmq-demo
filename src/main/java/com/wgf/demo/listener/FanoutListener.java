package com.wgf.demo.listener;

import com.wgf.demo.config.FanoutConfig;
import com.wgf.demo.config.TopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description: 文章目录：2.1.2.1.3
 * @author: ken 😃
 * @create: 2022-01-28 10:48
 **/
@Slf4j
//@Component
public class FanoutListener {
    @RabbitListener(queues = FanoutConfig.FANOUT_QUEUE_1)
    public void receive(String msg) {
        log.info("队列 {} 接收信息:{}", TopicConfig.TOPIC_QUEUE_1, msg);
    }

    @RabbitListener(queues = FanoutConfig.FANOUT_QUEUE_2)
    public void receive2(String msg) {
        log.info("队列 {} 接收信息:{}", TopicConfig.TOPIC_QUEUE_2, msg);
    }
}
