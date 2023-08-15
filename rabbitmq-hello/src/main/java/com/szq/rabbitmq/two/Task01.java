package com.szq.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.szq.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 生产者  发送大量的消息
 */
public class Task01 {


    //队列名称
    public static final String QUEUE_NAME="hello";

    //发送大量的消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //队列的声明
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认情况消息存储到内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行消费共享；true可以多个消费者消费
         * 4.是否自动生删除 最后一个消费者端开连接以后 该队列是否自动删除 true自动删除 false不自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //从控制台当中接受消息
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String message = scanner.next();
            /**
             * 发送一个消息
             * 1.发送到哪个交换机
             * 2.路由的Key值是哪个 本次是队列的名称
             * 3.消息是否持久化  其他参数
             * 4.发送消息的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成："+message);
        }


    }
}
