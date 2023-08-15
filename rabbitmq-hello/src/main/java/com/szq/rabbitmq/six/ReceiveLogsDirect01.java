package com.szq.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.szq.rabbitmq.utils.RabbitMqUtils;

public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明一个队列
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认情况消息存储到内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行消费共享；true可以多个消费者消费
         * 4.是否自动生删除 最后一个消费者端开连接以后 该队列是否自动删除 true自动删除 false不自动删除
         * 5.其他参数
         */
        channel.queueDeclare("console",false,false,false,null);

        /**
         * 队列的捆绑
         */
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");


        //消息的接收
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("console",true,deliverCallback,consumerTag -> {});
    }
}
