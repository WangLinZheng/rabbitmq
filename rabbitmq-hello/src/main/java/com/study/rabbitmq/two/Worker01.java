package com.study.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.study.rabbitmq.utils.RabbitMqUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/09/13:32
 * @Description:
 *
 * 这是一个工作线程（相当于之前的消费者）
 */
public class Worker01 {
    //队列名
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //消息接收
        DeliverCallback deliverCallback = (consumerTag,message) ->
                System.out.println("接受消息内容 ：  "+new String(message.getBody()));

        //消息接收被取消是 执行下面内容
        CancelCallback cancelCallback = (consumerTag)->
                System.out.println(consumerTag+" 消费者取消消费接口回调逻辑");


        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者未成功消费的回调
         * 4、消费者取消消费的回调
         */
        System.out.println("C2等待接收消息……");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
