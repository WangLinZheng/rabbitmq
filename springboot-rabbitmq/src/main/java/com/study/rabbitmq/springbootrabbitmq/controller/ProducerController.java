package com.study.rabbitmq.springbootrabbitmq.controller;

import com.study.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import com.study.rabbitmq.springbootrabbitmq.config.MyCallBack;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/13/9:58
 * @Description:
 */
@RestController
@RequestMapping("/confirm")
@Slf4j
public class ProducerController {

//    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    private final RabbitTemplate rabbitTemplate;

    public ProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @ApiOperation("消息队列发送消息")
    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable @ApiParam("消息内容") String message){
        //指定消息 id 为 1
        CorrelationData correlationData1=new CorrelationData("1");
        String routingKey="key1";

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                routingKey,
                message+routingKey,
                correlationData1);
        log.info(routingKey+"发送消息内容:{}",message);

        CorrelationData correlationData2=new CorrelationData("2");
        routingKey="key2";

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,routingKey+1,message+routingKey,correlationData2);
        log.info(routingKey+"发送消息内容:{}",message);
    }
}