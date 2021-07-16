package com.study.rabbitmq.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.study.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/13/11:33
 * @Description:
 *
 * 报警队列消费者
 */
@Slf4j
@Component
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveD(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("收到报警队列信息{}{}",msg,"   该消息原通道失效");
    }
}
