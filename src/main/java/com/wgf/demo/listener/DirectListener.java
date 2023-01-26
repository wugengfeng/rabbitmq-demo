package com.wgf.demo.listener;

import com.wgf.demo.config.DirectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description: 文章目录：2.1.2.1.1
 * @author: ken 😃
 * @create: 2022-01-28 10:48
 **/
@Slf4j
//@Component
public class DirectListener {
    @RabbitListener(queues = DirectConfig.DIRECT_QUEUE)
    public void receive(String msg) {
        log.info("队列 {} 接收信息:{}", DirectConfig.DIRECT_QUEUE, msg);
    }
}
