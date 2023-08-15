package com.szq.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.szq.rabbitmq.utils.RabbitMqUtils;

public class ReceiveLogsDirect02 {

    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明一个队列
        channel.queueDeclare("disk",false,false,false,null);

        /**
         * 队列的捆绑
         */
        channel.queueBind("disk",EXCHANGE_NAME,"error");


        //消息的接收
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("ReceiveLogsDirect02控制台打印接收到的消息："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("disk",true,deliverCallback,consumerTag -> {});
    }
}
