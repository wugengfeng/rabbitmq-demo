package com.wgf.demo.listener;

import com.wgf.demo.config.QueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 文章目录 3
 */
@Slf4j
@Component
public class SimpleListener {

    @RabbitListener(queues = QueueConfig.QUEUE_NAME)
    public void receive(String msg) {
      log.info("{}:{}", "receive", msg);
    }
}
