package com.study.rabbitmq.dead;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.study.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/09/10:33
 * @Description:
 * 死信队列场景
 * 生产者： 发消息，设置超时发送场景传入私信队列中
 */
public class Producer {

    /**
     * 普通交换机
     */
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "normal_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String routingKey ="zhangsan";

//        发消息
        for (int i = 0; i < 11; i++) {
            String msg = "hello world";
            //初次使用
            String message = msg + i;
            /**
             * 发送一个消息
             * 1.发送到那个交换机
             * 2.路由的 key 是哪个，本次是队列的名称
             * 3.其他的参数信息
             * 4.发送消息的消息体
             */

            //死信消息  设置TTL 过期时间 time to live 如果需要就放开注释填入参数props中
            AMQP.BasicProperties properties =
                    new AMQP.BasicProperties()
                    .builder().expiration("10000").build();

            channel.basicPublish(NORMAL_EXCHANGE,routingKey,properties,message.getBytes("UTF-8"));
            System.out.println("消息发送完毕");
        }



    }
}
