package com.study.rabbitmq.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
 * 生产者： 发消息  权重发送消息，测试的时候需要先断开消费者发送消息
 */
public class Producer {

    private final static String RABBIT_MQ_HOST = "116.62.223.215";

    private final static String RABBIT_MQ_USERNAME = "admin";

    private final static String RABBIT_MQ_PASSWORD = "admin";

    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP 连接RabbitMQ的队列
        factory.setHost(RABBIT_MQ_HOST);
        //设置用户名
        factory.setUsername(RABBIT_MQ_USERNAME);
        //设置密码
        factory.setPassword(RABBIT_MQ_PASSWORD);
//        创建连接
        Connection connection = factory.newConnection();
//        获取信道
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列
         *
         * 参数1、队列名称
         * 参数2、队列李面的消息是否持久化（磁盘） 默认消息存储在内存中
         * 参数3、该队列是否只供一个消费者进行消费 是否进行消息共享，true代表可以多个消费者消费，false只能一个消费者消费
         * 参数4、是否自动删除 最后一个消费者断开连接后 该队列是否自动删除  true 删除 false 不删除
         * 参数5、其他参数（延迟消息，死信消息,权重级别设置）
         *
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10);  // 官方允许0-255 此处设置优化级范围为0-10 设置过大会朗威cpu和内存
        channel.queueDeclare(QUEUE_NAME,false,false,false,arguments);



        /**
         * 发送一个消息
         * 1.发送到那个交换机
         * 2.路由的 key 是哪个，本次是队列的名称
         * 3.其他的参数信息
         * 4.发送消息的消息体
         *
         */
        for (int i = 1; i < 11; i++) {
//        发消息
            String message = "hello world" + i;
            if (i == 5){
                AMQP.BasicProperties properties =
                        new AMQP.BasicProperties()
                        .builder() //构建模式
                        .priority(5) //设置优先级
                        .build();  //构建
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }
            else {
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }

        System.out.println("消息发送完毕");

    }
}
