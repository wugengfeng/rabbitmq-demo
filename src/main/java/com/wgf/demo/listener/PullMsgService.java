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
import java.util.Objects;

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
    public void pull(String queueName, Integer count) {

        while (true) {
            List<String> msgList = rabbitTemplate.execute(new ChannelCallback<List<String>>() {
                @Override
                public List<String> doInRabbit(Channel channel) throws Exception {
                    List<String> result   = new ArrayList<>();
                    long         start    = System.currentTimeMillis();
                    long         end;
                    GetResponse  response = null;
                    while (true) {
                        try {
                            end      = System.currentTimeMillis();
                            response = channel.basicGet(queueName, false);
                            if (Objects.isNull(response)) {
                                // 没有消息
                                log.info("没有消息");
                            }

                            // 获取消息
                            String msg = new String(response.getBody(), "utf-8");
                            result.add(msg);

                            if (((start - end) > 5000 && result.size() > 0) || result.size() >= 10) {
                                channel.basicAck(response.getEnvelope().getDeliveryTag(), true);
                                return result;
                            }
                        } catch (Exception e) {
                            channel.basicNack(response.getEnvelope().getDeliveryTag(), true, true);
                        }
                    }
                }
            });

            System.out.println(msgList.size());
        }
    }
}
