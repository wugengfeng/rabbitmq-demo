package com.wgf.demo.listener;

import com.wgf.demo.config.DelayedConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 文章目录：7.5.2
 * 延迟队列监听器
 */
@Slf4j
@Component
public class DelayedListener {
    // 注释手动ack模式
    @RabbitListener(queues = DelayedConfig.DELAYED_QUEUE)
    public void listener(String msg) {
        log.info("延迟队列 {} 接收信息:{}", DelayedConfig.DELAYED_QUEUE, msg);
    }
}
