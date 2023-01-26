package com.wgf.demo.listener;

import com.wgf.demo.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class TtlListener {
    @RabbitListener(queues = TtlQueueConfig.DEAD_QUEUE)
    public void receive(String msg) {
        log.info("死信队列 {} 接收信息:{}", TtlQueueConfig.DEAD_QUEUE, msg);
    }
}
