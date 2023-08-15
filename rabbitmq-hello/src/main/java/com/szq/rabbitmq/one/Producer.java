package com.szq.rabbitmq.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 生产者 ：发消息
 */
public class Producer {

    //队列名称                     ctrl+shift+u
    public static final String QUEUE_NAME="mirrior_hello";

    //发消息
    public static void main(String[] args) throws Exception{
        //创建一个连接工厂
        ConnectionFactory factory=new ConnectionFactory();
        //工厂IP连接RabbitMQ的队列
        factory.setHost("192.168.130.103");
        //用户名
        factory.setUsername("admin");
        //密码
        factory.setPassword("123");

        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认情况消息存储到内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行消费共享；true可以多个消费者消费
         * 4.是否自动生删除 最后一个消费者端开连接以后 该队列是否自动删除 true自动删除 false不自动删除
         * 5.其他参数
         */
        Map<String,Object> arguments=new HashMap<>();
        arguments.put("x-max-priority",10);//官方允许是0-255之间 此处设置10  允许优先级的范围0-10 不要设置过大 浪费cpu与内存
        channel.queueDeclare(QUEUE_NAME,true,false,false,arguments);

        //发消息
        for (int i=1;i<11;i++){
            String message="info"+i;
            if (i==5){
                AMQP.BasicProperties properties=new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        }
        String message="hello world";

        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2.路由的Key值是哪个 本次是队列的名称
         * 3.其他参数
         * 4.发送消息的消息体
         */

        System.out.println("消息发送完毕");

    }
}
