package com.study.rabbitmq.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.study.rabbitmq.utils.RabbitMqUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/10/11:28
 * @Description: 消费者01
 */
public class ReceiveLogsDirect01 {
    //定义交换机名字
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        //获取连接信道
        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT);

        //声明指定队列
        channel.queueDeclare("console",false,false,false,null);
        //将队列和交换机绑定
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");

        System.out.println("消费者info   等待接收消息……");

        //处理回调，该方法实际接收消息message.getBody. {}中处理回调信息，此处做个打印
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("消费者info接收到消息： "+new String(message.getBody(),"UTF-8"));
        };


        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者未成功消费的回调
         * 4、消费者取消消费的回调
         */
        channel.basicConsume("console",true,deliverCallback,consumerTag -> {});
    }
}
