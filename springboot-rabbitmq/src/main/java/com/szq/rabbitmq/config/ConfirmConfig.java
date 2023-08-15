package com.szq.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类  发布确认  （高级）
 */
@Configuration
public class ConfirmConfig {

    //交换机
    public static final String CONFIRM_EXCHANGE_NAME="confirm_exchange";
    //队列
    public static final String CONFIRM_QUEUE_NAME="confirm_queue";
    //RoutingKey
    public static final String CONFIRM_ROUTING_KEY="key1";

    //备换交换机
    public static final String BACKUP_EXCHANGE_NAME="confirm_exchange";

    //备份队列
    public static final String BACKUP_QUEUE_NAME="backup_queue";

    //警告队列
    public static final String WARNING_QUEUE_NAME="warning_queue";

    //声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        //连接备份交换机
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //绑定
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                        @Qualifier("confirmExchange") DirectExchange confirmExchange){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    //声明备份交换机
    @Bean("backupExchange")
    public DirectExchange backupExchange(){
        return new DirectExchange(BACKUP_EXCHANGE_NAME);
    }


    //备份队列
    @Bean("backupQueue")
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }


    @Bean
    public Binding backupQueueBindingbackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                                    @Qualifier("backupExchange") DirectExchange backupExchange){
    return BindingBuilder.bind(backupQueue).to(backupExchange).with("");
    }

    @Bean
    public Binding warningQueueBindingbackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                    @Qualifier("backupExchange") DirectExchange backupExchange){
        return BindingBuilder.bind(warningQueue).to(backupExchange).with("");
    }

}
