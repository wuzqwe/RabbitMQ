package com.szq.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.szq.rabbitmq.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 消息在手动应答时是不丢失的、放回队列中重新消费
 */
public class Task2 {

    //队列名称
    public static final String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();


        //声明队列
        boolean durable=true;//表示让队列进行持久化

        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);

        //从控制台中输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //设置生产者发送消息为持久化消息（要求保存到磁盘上）
            /**
             * 发送一个消息
             * 1.发送到哪个交换机
             * 2.路由的Key值是哪个 本次是队列的名称
             * 3.消息是否持久化  其他参数
             * 4.发送消息的消息体
             */
            channel.basicPublish("",TASK_QUEUE_NAME,MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产则发出消息："+message);
        }
    }
}
