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
 *  死信队列 ： 消费者 模拟拒绝消息传入死信队列中
 *
 *   //处理回调，该方法实际接收消息message.getBody. {}中处理回调信息，此处做个打印
 *         DeliverCallback deliverCallback = (consumerTag, message) ->{
 *             String msg = new String(message.getBody(), "UTF-8");
 *             if (msg.equals("info5")){
 *                 System.out.println("消费者04接受消息 ： "+msg +": 该消息被拒绝");
 *                 //拒绝消息，是否放回队列
 *                 channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
 *             }else {
 *                 System.out.println("消费者04接受消息 ： "+msg);
 *                 channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
 *             }
 */
public class Consumer04 {

    //普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明死信和普通交换机，类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        //设置过期时间 毫秒 一般是生产方指定
//        arguments.put("x-message-ttl",10000);
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信的RoutingKey
        arguments.put("x-dead-letter-routing-key","lisi");

        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);


        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);


        //交换机和队列绑定
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        System.out.println("等待接收消息……");

        //处理回调，该方法实际接收消息message.getBody. {}中处理回调信息，此处做个打印
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            String msg = new String(message.getBody(), "UTF-8");
            String refusedWord = "hello world5";
            if (msg.equals(refusedWord)){
                System.out.println("消费者04接受消息 ： "+msg +": 该消息被拒绝");
                //拒绝消息，是否放回队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("消费者04接受消息 ： "+msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }

    };

      //接受消息的队列
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,consumerTag -> {});
    }
}
