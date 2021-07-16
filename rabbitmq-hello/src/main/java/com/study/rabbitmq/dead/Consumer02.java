package com.study.rabbitmq.dead;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.study.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/12/14:35
 * @Description:
 *
 *  死信队列 ： 消费者 专门接受死信队列消息
 */
public class Consumer02 {


    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

       // 已经声明过可以不用再次声明

        //声明死信交换机，类型为direct
//        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //声明死信队列  已经声明过可以不用再次声明
//        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //交换机和队列绑定
//        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        System.out.println("等待接收消息……");

        //处理回调，该方法实际接收消息message.getBody. {}中处理回调信息，此处做个打印
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("接收死信队列消息02 接收到消息： "+new String(message.getBody(),"UTF-8"));
        };

      //接受消息的队列
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag -> {});
    }
}
