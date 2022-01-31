package com.wgf.demo.listener;

import com.wgf.demo.config.DeadConfig;
import com.wgf.demo.config.DirectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 文章目录： 7.4
 * 基于死信队列实现的延迟队列监听
 */
@Slf4j
@Component
public class DelayListener {
    
    // 注释手动ack模式
    @RabbitListener(queues = DeadConfig.DEAD_QUEUE)
    public void listener(String msg) {
        log.info("死信队列 {} 接收信息:{}", DeadConfig.ORDER_QUEUE_2, msg);
    }
}
