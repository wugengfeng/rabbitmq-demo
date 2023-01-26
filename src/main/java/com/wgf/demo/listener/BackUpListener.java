package com.wgf.demo.listener;

import com.wgf.demo.config.BackUpConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 备份交换机
 */
@Slf4j
//@Component
public class BackUpListener {
    @RabbitListener(queues = BackUpConfig.QUEUE)
    public void listenerProduct(String msg) {
        log.info("商品队列接收消息：{}", msg);
    }

    @RabbitListener(queues = BackUpConfig.BACKUP_QUEUE)
    public void listenerBackup(String msg) {
        log.info("备份队列接收消息：{}", msg);
    }

    @RabbitListener(queues = BackUpConfig.WARING_QUEUE)
    public void listenerWaring(String msg) {
        log.info("报警队列接收消息：{}", msg);
    }
}
