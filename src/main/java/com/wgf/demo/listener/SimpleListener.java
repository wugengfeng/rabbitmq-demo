package com.wgf.demo.listener;

import com.wgf.demo.config.QueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class SimpleListener {

    @RabbitListener(queues = QueueConfig.QUEUE_NAME)
    public void receive(String msg) {
      log.info("{}:{}", "receive", msg);
    }

    /*@RabbitListener(queues = QueueConfig.QUEUE_NAME)
    public void receive2(String msg) {
        log.info("{}:{}", "receive2", msg);
    }

    @RabbitListener(queues = QueueConfig.QUEUE_NAME)
    public void receive3(String msg) {
        log.info("{}:{}", "receive3", msg);
    }*/
}
