package com.study.rabbitmq.springbootrabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *
 * 发布确认消费者
 */
@Component
@Slf4j
public class ConfirmConsumer {


    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    @RabbitListener(queues =CONFIRM_QUEUE_NAME)
    public void receiveMsg(Message message){
    String msg=new String(message.getBody());
    log.info("接受到队列 confirm.queue 消息:{}",msg);
 }
}