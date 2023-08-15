package com.szq.rabbitmq.seven;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.szq.rabbitmq.utils.RabbitMqUtils;

/**
 * 声明主题交换机 及相关队列
 *
 * 消费者C1
 */
public class ReceiveLogsTopic1 {

    //交换机的名称
    public static  final  String EXCHANG_NAME="topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANG_NAME,"topic");
        //声明队列
        String queueName="Q1";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANG_NAME,"*.orange.*");
        System.out.println("等待接收消息....");

        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("ReceiveLogsTopic1："+new String(message.getBody(),"UTF-8"));
            System.out.println("接收队列："+queueName+" 绑定键："+message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
