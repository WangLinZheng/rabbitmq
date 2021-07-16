package com.study.rabbitmq.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {



    @Autowired
    private RabbitTemplate rabbitTemplate;


    @PostConstruct
    public void init(){
        //依赖注入 rabbitTemplate 之后再设置它的回调对象  吧当前类注入 this指向
        rabbitTemplate.setConfirmCallback(this);
        /**
         * true：
         * 交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         * 如果发现消息无法进行路由，则直接丢弃
         */
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(this);
    }


   /**
    * 交换机不管是否收到消息的一个回调方法
    * CorrelationData
    * 消息相关数据
    * ack
    * 交换机是否收到消息
    * cause
    * 失败原因
    */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
         String id=correlationData != null ? correlationData.getId() : "";
         if(ack){
         log.info("交换机已经收到 id 为:{}的消息",id);
         }else{
          log.info("交换机还未收到 id 为:{}消息,由于原因:{}",id,cause);
     }
    }


    /**
     * 回退消息
     * 通过设置 mandatory 参数可以在当消息传递过程中不可达目的地时将消息返回给生产者。
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息:{}被服务器退回，退回原因:{}, 交换机是:{}, 路由 key:{}",
                new String(message.getBody()),replyText, exchange, routingKey);
    }
}