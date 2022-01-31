package com.wgf.demo.listener;

import com.wgf.demo.config.PriorityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 文章目录 8.2
 * 优先级队列监听
 */
@Slf4j
@Component
public class PriorityListener {
    /**
     * 要看到效果，必选让消息全部发送到队列后再进行监听消费，让消息有排序的时间，实时消费排序效果不明显
     *
     * @param msg
     */
    @RabbitListener(queues = PriorityConfig.PRIORITY_QUEUE)
    public void receive(String msg) {
        log.info("队列 {} 接收信息:{}", PriorityConfig.PRIORITY_QUEUE, msg);
    }
}
