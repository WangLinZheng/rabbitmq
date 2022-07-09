package com.study.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/09/11:36
 * @Description: 消费者： 接受消息
 */
public class Consumer {
    private final static String QUEUE_NAME = "hello";

    private final static String RABBIT_MQ_HOST = "116.62.223.215";

    private final static String RABBIT_MQ_USERNAME = "admin";

    private final static String RABBIT_MQ_PASSWORD = "admin";

    public static void main(String[] args) throws Exception {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP 连接RabbitMQ的队列
        factory.setHost(RABBIT_MQ_HOST);
        //设置用户名
        factory.setUsername(RABBIT_MQ_USERNAME);
        //设置密码
        factory.setPassword(RABBIT_MQ_PASSWORD);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        System.out.println("等待接收消息....");
        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println(message);
        };
        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者未成功消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
