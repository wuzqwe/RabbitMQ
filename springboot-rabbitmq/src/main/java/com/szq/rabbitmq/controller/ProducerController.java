package com.szq.rabbitmq.controller;

import com.szq.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 开始发消息 测试确认
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {


    @Resource
    private RabbitTemplate rabbitTemplate;

    //发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        CorrelationData correlationData1=new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData1);
        log.info("发消息的内容：{}",message+"KEY1");

        CorrelationData correlationData2=new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,ConfirmConfig.CONFIRM_ROUTING_KEY+"2",message,correlationData2);
        log.info("发消息的内容：{}",message+"KEY2");
    }

}
