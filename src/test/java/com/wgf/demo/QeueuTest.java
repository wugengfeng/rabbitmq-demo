package com.wgf.demo;

import com.wgf.demo.config.QueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class QeueuTest {
    @Autowired
    AmqpTemplate amqpTemplate;

    @Test
    public void simpleSend() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            amqpTemplate.convertAndSend(QueueConfig.QUEUE_NAME, i);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
