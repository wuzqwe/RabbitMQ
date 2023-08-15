package com.szq.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.szq.rabbitmq.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

/**
 * 死信队列 之
 * 生产者代码
 *
 */
public class Producer {
    public static final String NORMAL_EXCHANGE="normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //延迟消息 死信消息  设置TTL时间 time to live 单位是ms
        AMQP.BasicProperties properties=new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 1; i < 11; i++) {
            String message="info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes(StandardCharsets.UTF_8));
        }

    }
}
