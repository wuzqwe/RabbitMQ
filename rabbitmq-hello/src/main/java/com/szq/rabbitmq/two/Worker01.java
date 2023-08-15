package com.szq.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.szq.rabbitmq.utils.RabbitMqUtils;

/**
 * 这是一个工作线程（相当于之前消费者）
 */
public class Worker01 {

    //队列的名称
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //消息的接收
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println("接收到的消息："+new String(message.getBody()));
        };

        //消息接收时被取消时 执行下面的内容
        CancelCallback cancelCallback=consumerTag->{
            System.out.println(consumerTag+"消费者取消消费接口回调逻辑");
        };

        /**
         * 消费者接收消息
         * 1.消费哪个队列
         * 2.消费成功后是否要自动应答 true代表的自动应答 false代表手动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        System.out.println("C2等待接收消息....");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
