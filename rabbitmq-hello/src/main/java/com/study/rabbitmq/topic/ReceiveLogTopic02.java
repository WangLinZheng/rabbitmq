package com.study.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.study.rabbitmq.utils.RabbitMqUtils;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/10/10:55
 * @Description:
 *
 *
 *   topic模式： 主题模式
 *      设置路由：
 *          RoutingKey : * 、#
 *              *代表一个任意单词，站位一个单体
 *              #代表多个任意单词
 *
 *              例： *.
 */
public class ReceiveLogTopic02 {

    //定义交换机名字
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        //获取连接信道
        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        //声明队列
        String queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        //将队列和交换机绑定
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");

        System.out.println("消费者Q2  等待接收消息……");



        //处理回调，该方法实际接收消息message.getBody. {}中处理回调信息，此处做个打印
        DeliverCallback deliverCallback = (consumerTag,message) ->{
//            ArrayList<String> list = new ArrayList<>();
            System.out.println(
             "消费者Q2  接收到消息： "+new String(message.getBody(),"UTF-8"));
            System.out.println("接受队列 ：" + queueName+"  ");
            System.out.println("路由绑定键 RoutingKey：" + message.getEnvelope().getRoutingKey()+"  ");
            System.out.println("绑定交换机 ：" + message.getEnvelope().getExchange()+"  ");
            System.out.println("回调 ：" + message.getEnvelope().getDeliveryTag());
            System.out.println("\n\n");
//            list.add(new String(message.getBody(),"UTF-8"));
//            System.out.println(list);
    };


        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者未成功消费的回调
         * 4、消费者取消消费的回调
         */
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
