package com.szq.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.szq.rabbitmq.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 * 1.单个确认   使用的时间   比较哪种确认方式是最好的
 * 2.批量确认
 * 3.异步批量确认
 */
public class ConfirmMessage {

    //批量发消息的个数
    public static final int MESSAGE_COUNT=1000;

    public static void main(String[] args) throws Exception {
        //1.单个确认
        //publishMessageIndividually(); //发布1000个单独确认消息，耗时658ms
        //2.批量确认
        //publishMessageBatch();//   发布1000个批量确认消息，耗时94ms   无法确认消息哪里出错
        //3.异步批量确认
        publishMessageAsync();  //发布1000个异步批量确认消息，耗时67ms
    }

    //单个确认
    public static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String messge=i+"";
            channel.basicPublish("",queueName,null,messge.getBytes());
            //单个消息就马上进行发布确认
            boolean flag=channel.waitForConfirms();
            if (flag){
                System.out.println("消息发送成功");
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息，耗时"+(end-begin)+"ms");
    }

    //批量发送确认
    public static void publishMessageBatch() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();


        //批量确认消息大小
        int batchSize=100;

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message=i+"";
            channel.basicPublish("",queueName,null,message.getBytes());

            //判断达到100条消息的时候 批量确认一次
            if (i%batchSize==0){
                //发布确认
                channel.waitForConfirms();
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个批量确认消息，耗时"+(end-begin)+"ms");
    }


    //异步发布确认
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表 适用于高并发的情况
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms=new ConcurrentSkipListMap<>();

        //开始时间
        long begin = System.currentTimeMillis();
        //准备消息的监听器 监听哪些消息成功了 哪些消息失败了
        //ConfirmCallback ackCallback, ConfirmCallback nackCallback
        //消息确认回调函数
        ConfirmCallback ackCallback=(deliveryTag,multiple)->{
            if (multiple){
                //2.删除到已经确认的消息  剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认消息："+deliveryTag);
        };
        //消息确认失败回调函数
        /**
         * 1.消息的标记
         * 2.是否批量确认
         */
        ConfirmCallback nackCallback=(deliveryTag,multiple)->{
            //3.打印一下未确认的消息都有哪些
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认消息："+message+"::::未确认的消息tag: "+deliveryTag);
        };
        /**
         * 1.监听哪些消息成功了
         * 2.监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback,nackCallback);//异步通知

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message="消息"+i;
            channel.basicPublish("",queueName,null,message.getBytes());
            //1:此处记录下所有要发送的消息  消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个异步批量确认消息，耗时"+(end-begin)+"ms");
    }
}
