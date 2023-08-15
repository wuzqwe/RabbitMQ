package com.szq.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {


    //注入
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        //注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }
    /**
     * 交换机确认回调方法
     * 1.发消息 交换机接收到  回调
     *    1.1 correlationData 保存回调消息的ID及相关信息
     *    1.2 交换机收到消息 true
     *    1.3 cause null
     * 2.发消息 交换机接收失败 回调
     *   2.1 correlationData 保存回调消息的ID及相关信息
     *   2.2 交换机收到消息 ack=false
     *   2.3 cause 失败的原因
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //
        String id=correlationData!=null ? correlationData.getId():"";
        if (ack){
            log.info("交换机已经收到Id为：{}的消息",id);
        }else {
            log.info("交换机还未收到Id为：{}的消息，由于原因：{}",id,cause);
        }
    }


    /**
     * 可以在消息传递过程中不可达到目的地时将消息返回给生产者
     * 只有不可达目的地的时候 才进行回退
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
            log.info("消息{}，被交换机{}退回，退回原因：{}，路由Key:{}",new String(returned.getMessage().getBody())
                                            ,returned.getExchange(),returned.getReplyText(),returned.getRoutingKey());


    }
}
