package com.wgf.demo.config;

import com.wgf.demo.config.callback.MsgConfirmCallback;
import com.wgf.demo.config.callback.MsgReturnCallback;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * redis配置
 */
@Configuration
public class RabbitmqConfig {
    @Autowired
    MsgConfirmCallback msgConfirmCallback;

    @Autowired
    MsgReturnCallback msgReturnCallback;

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        // connectionFactory包含yml配置文件配置信息
        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        //mandatory参数说明: 交换器无法根据自身类型和路由键找到一个符合条件的队列时的处理方式; true表示会调用Basic.Return命令返回给生产者; false表示丢弃消息
        template.setMandatory(true);

        // 消息从 producer 到 exchange 正常则会返回一个 confirmCallback
        template.setConfirmCallback(msgConfirmCallback);

        // 消息从 exchange–>queue 投递失败则会返回一个 returnCallback
        template.setReturnCallback(msgReturnCallback);

        return template;
    }
}
