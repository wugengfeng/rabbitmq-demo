package com.wgf.demo.listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @description: 手动拉取消息
 * @author: ken 😃
 * @create: 2022-01-27 15:14
 **/
@Slf4j
@Component
public class PullMsgService {

    @Autowired
    AmqpAdmin rabbitAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;


    /**
     * @param queueName
     * @param count
     */
    public void pull(String queueName, Integer count, Consumer<List<String>> consumer) {

        rabbitTemplate.execute(new ChannelCallback<Object>() {

            @Override
            public Object doInRabbit(Channel channel) throws Exception {
                List<String> msgList      = new ArrayList<>();
                long         start        = System.currentTimeMillis();
                long         end;
                GetResponse  response;
                GetResponse  lastResponse = null;

                while (true) {
                    try {
                        end = System.currentTimeMillis();
                        response = channel.basicGet(queueName, false);

                        if (response == null) {
                            if (msgList.size() == 0) {
                                log.info("没有消息");
                                TimeUnit.SECONDS.sleep(5);
                                start = System.currentTimeMillis();
                                continue;
                            }
                        } else {
                            String msg = new String(response.getBody(), "utf-8");
                            lastResponse = response;
                            msgList.add(msg);
                        }

                        if (((end -start) > 5000 && msgList.size() > 0) || msgList.size() >= 10) {
                            consumer.accept(msgList);
                            channel.basicAck(lastResponse.getEnvelope().getDeliveryTag(), true);
                            start = System.currentTimeMillis();
                            msgList.clear();
                        }

                    } catch (Exception e) {
                        channel.basicNack(lastResponse.getEnvelope().getDeliveryTag(), true, true);
                    }
                }
            }
        });
    }
}
