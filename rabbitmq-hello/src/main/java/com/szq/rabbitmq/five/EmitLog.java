package com.szq.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.szq.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 发消息 给 交换机
 * fauout
 * 无论routingkey 为多少，消息都会被两个消费者消费
 */
public class EmitLog {

    public static final  String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqUtils.getChannel();
//        channel.exchangeDeclare(EXCHANGE_NAME,"fauout");

        Scanner scanner=new Scanner(System.in);

        while (scanner.hasNext()){
            String message=scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);
        }

    }
}
