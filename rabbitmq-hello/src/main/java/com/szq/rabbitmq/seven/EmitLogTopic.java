package com.szq.rabbitmq.seven;

import com.rabbitmq.client.Channel;
import com.szq.rabbitmq.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EmitLogTopic {

    //交换机的名称
    public static  final  String EXCHANG_NAME="topic_logs";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqUtils.getChannel();

        Map<String,String> map=new HashMap<>();

        map.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        map.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        map.put("quick.orange.fox"," 被队列 Q1 接收到");
        map.put("lazy.brown.fox","被队列 Q2 接收到");
        map.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        map.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        map.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        map.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");

        for (Map.Entry<String, String> stringEntry : map.entrySet()) {
            String routingKey = stringEntry.getKey();
            String message=stringEntry.getValue();
            channel.basicPublish(EXCHANG_NAME,routingKey,null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);
        }



    }
}
