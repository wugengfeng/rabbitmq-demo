package com.wgf.demo;

import com.wgf.demo.config.*;
import com.wgf.demo.listener.PullMsgService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
public class QeueuTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //@Autowired
    PullMsgService pullMsgService;

    @Test
    public void testCallback() {
        //correlationDataId相当于消息的唯一表示
        UUID correlationDataId = UUID.randomUUID();
        // 这里设置的id是给ConfirmCallback使用的
        CorrelationData correlationData = new CorrelationData(correlationDataId.toString());
        //发送MQ
        rabbitTemplate.convertAndSend(DirectConfig.DIRECT_EXCHANGE, "test-queue", "test", (message) -> {
            // 这里设置的id是给 ReturnCallback 使用的
            message.getMessageProperties().setMessageId(correlationData.getId());
            return message;
        }, correlationData);
    }


    /**
     * 发送持久化消息
     */
    @Test
    public void persistenceTest() {
        // 消息持久化
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            }
        };

        rabbitTemplate.convertAndSend(QueueConfig.QUEUE_NAME, "test".getBytes(), messagePostProcessor);
    }

    /**
     * 文章目录：3
     * 简单发送消息
     *
     * @throws InterruptedException
     */
    @Test
    public void simpleSend() throws InterruptedException {
        for (int i = 1; i <= 20; i++) {
            rabbitTemplate.convertAndSend(QueueConfig.QUEUE_NAME, i);
        }
    }


    /**
     * 文章目录：2.1.4.1.2
     * 手动 pull 消息数据准备
     *
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 2005; i++) {
            rabbitTemplate.convertAndSend(QueueConfig.BASIC_GET, i + "");
        }
    }


    /**
     * 文章目录：2.1.4.1.2
     * 手动拉取消息
     */
    @Test
    public void recive() {
        pullMsgService.pull(QueueConfig.BASIC_GET, 200, 1000, (list) -> System.out.println(list.size()));
    }


    /**
     * 文章目录：2.1.2.1.1
     * 直连交换机
     */
    @Test
    public void sendDirect() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            rabbitTemplate.convertAndSend(DirectConfig.DIRECT_EXCHANGE, DirectConfig.ROUTING_KEY, i + "");
        }
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * 文章目录：2.1.2.1.2
     * 主题交换机
     */
    @Test
    public void sendTopic() throws InterruptedException {
        rabbitTemplate.convertAndSend(TopicConfig.TOPIC_EXCHANGE, TopicConfig.ROUTING_KEY, TopicConfig.ROUTING_KEY);
        rabbitTemplate.convertAndSend(TopicConfig.TOPIC_EXCHANGE, "topic.hello", "topic.hello");
        rabbitTemplate.convertAndSend(TopicConfig.TOPIC_EXCHANGE, "topic.hi.hello", "topic.hi.hello");
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 文章目录：2.1.2.1.3
     * 扇形交换机
     */
    @Test
    public void sendFanout() throws InterruptedException {
        rabbitTemplate.convertAndSend(FanoutConfig.FANOUT_EXCHANGE, null, "test");
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 文章目录：2.1.2.1.4
     * 头交换机
     */
    @Test
    public void sendHeaders() throws InterruptedException {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("color", "black");
        SimpleMessageConverter messageConverter = new SimpleMessageConverter();
        Message message = messageConverter.toMessage("test1", messageProperties);
        rabbitTemplate.convertAndSend(HeadersConfig.HEADERS_EXCHANGE, null, message);

        messageProperties.setHeader("aging", "fast");
        message = messageConverter.toMessage("test2", messageProperties);
        rabbitTemplate.convertAndSend(HeadersConfig.HEADERS_EXCHANGE, null, message);
    }


    /**
     * 文章目录 3.2.2
     * 消息的ttl过期自动删除
     */
    @Test
    public void ttlTest() {
        rabbitTemplate.convertAndSend(QueueConfig.TTL_QUEUE, "test");
    }


    /**
     * 带有消息id的消息发送
     *
     * @param msgId      消息id
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param msg        消息
     */
    private void send(String msgId, String exchange, String routingKey, Object msg) {
        CorrelationData correlationData = new CorrelationData(msgId);
        //发送MQ
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, (message) -> {
            message.getMessageProperties().setMessageId(correlationData.getId());
            return message;
        }, correlationData);
    }


    /**
     * 文章目录 4
     * 消息异步发布确认
     */
    @Test
    public void callBackTest() {
        //correlationDataId相当于消息的唯一表示
        String msgId = UUID.randomUUID().toString();
        this.send(msgId, DirectConfig.DIRECT_EXCHANGE, DirectConfig.ROUTING_KEY, "test");
    }


    /**
     * 文章目录 5.2
     * 手动ack
     */
    @Test
    public void ackTest() {
        IntStream.range(0, 6).forEach(line -> {
            //correlationDataId相当于消息的唯一表示
            String msgId = UUID.randomUUID().toString();
            this.send(msgId, DirectConfig.DIRECT_EXCHANGE, AckQueueConfig.ROUTING_KEY, line);
        });
    }


    /**
     * 文章目录 6.3
     * ack重试次数
     * 需要打开重试配置
     */
    @Test
    public void retryTest() throws InterruptedException {
        this.rabbitTemplate.convertAndSend(RetryConfig.RETRY_EXCHANGE, RetryConfig.ROUTING_KEY, "test");
        TimeUnit.SECONDS.sleep(60);
    }


    /**
     * 死信队列： 消息TTL过期自动转入死信队列
     */
    @Test
    @SneakyThrows
    public void deadTtlTest() {
        IntStream.range(0, 10).forEach(line -> {
            int ttl = 5000;
            String msg = "delay:" + line;

            rabbitTemplate.convertAndSend(DeadConfig.NORMAL_EXCHANGE, DeadConfig.TTL_ROUTING_KEY, msg);
            log.info("发送一条时长: {} 毫秒信息给队列: {}  内容: {}", ttl, DeadConfig.TTL_QUEUE, msg);
        });
        TimeUnit.SECONDS.sleep(20);
    }


    /**
     * 死信队列：队列满了消息转入死信队列
     */
    @Test
    @SneakyThrows
    public void deadLengthTest() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(DeadConfig.NORMAL_EXCHANGE, DeadConfig.LENGTH_ROUTING_KEY, i + "");
        }
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * 死信队列：消息拒收转入死信队列
     */
    @Test
    @SneakyThrows
    public void deadNackTest() {
        for (int i = 0; i < 3; i++) {
            rabbitTemplate.convertAndSend(DeadConfig.NORMAL_EXCHANGE, DeadConfig.NACK_ROUTING_KEY, i + "");
        }
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * 原生延迟队列缺陷
     */
    @Test
    public void ttlMsgTest() throws InterruptedException {
        IntStream.range(0, 4).forEach(line -> {
            int ttl = 60000 - (line * 5000);
            String msg = "delay:" + line;

            rabbitTemplate.convertAndSend(TtlQueueConfig.TTL_EXCHANGE, TtlQueueConfig.TTL_ROUTING_KEY, msg, correlationData -> {
                // 动态设置消息ttl
                correlationData.getMessageProperties().setExpiration(String.valueOf(ttl));
                return correlationData;
            });


            log.info("发送一条时长: {} 毫秒信息给延迟队列: {}  内容: {}", ttl, TtlQueueConfig.TTL_QUEUE, msg);
        });
        TimeUnit.SECONDS.sleep(70);
    }

    /**
     * 延迟交换机
     */
    @Test
    public void delayedTest() throws InterruptedException {
        IntStream.range(0, 10).forEach(line -> {
            int delay = 60000 - (line * 5000);
            String msg = "delay:" + line;

            rabbitTemplate.convertAndSend(DelayedConfig.DELAYED_EXCHANGE, DelayedConfig.DELAYED_ROUTING_KEY, msg, correlationData -> {
                // 设置消息的延迟时间 单位ms
                correlationData.getMessageProperties().setDelay(delay);
                return correlationData;
            });

            log.info("发送一条时长: {} 毫秒信息给延迟队列: {}  内容: {}", delay, DelayedConfig.DELAYED_QUEUE, msg);
        });
        TimeUnit.SECONDS.sleep(70);
    }


    /**
     * 文章目录 8.2
     * 优先级队列
     */
    @Test
    public void priorityTest() {
        IntStream.range(1, 10).forEach(line -> {
            int temp = line;

            // 能被2整除的优先级最高
            if (line % 2 == 0) {
                temp = 10;
            }

            int priority = temp;
            String msg = "优先级:" + priority;

            rabbitTemplate.convertAndSend(PriorityConfig.PRIORITY_EXCHANGE, PriorityConfig.ROUTING_KEY, msg, correlationData -> {
                // 设置队列优先级
                correlationData.getMessageProperties().setPriority(priority);
                return correlationData;
            });

            log.info("发送一条优先级: {} 信息给队列: {}  内容: {}", priority, PriorityConfig.PRIORITY_QUEUE, msg);
        });
    }


    /**
     * 文章目录：9.3
     * 惰性队列
     */
    @Test
    public void lazyTest() {
        String msg = UUID.randomUUID().toString();
        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend(LazyConfig.LAZY_EXCHANGE, LazyConfig.ROUTING_KEY, msg);
        }
    }


    /**
     * 备份交换机
     */
    @Test
    @SneakyThrows
    public void backUpTest() {
        rabbitTemplate.convertAndSend(BackUpConfig.EXCHANGE, BackUpConfig.ROUTING_KEY, "正常商品消息");
        rabbitTemplate.convertAndSend(BackUpConfig.EXCHANGE, "test", "没有路由键的商品消息");
        TimeUnit.SECONDS.sleep(10);
    }
}
