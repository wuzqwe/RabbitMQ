package com.szq.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.szq.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者
 *
 * 死信队列实战
 */
public class Consumer02 {


    //死信队列的名称
    public static final  String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();


        System.out.println("等待接收消息......");

        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("Consumer02接收的消息是："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE,true, deliverCallback,consumerTag -> {});
    }

}
