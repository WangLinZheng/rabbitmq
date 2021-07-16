package com.study.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.study.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/10/13:54
 * @Description:
 *
 * topic 主题模式  生产者
 */
public class TopicLog {

    //定义交换机名字
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);


        /**
         *          Q1-->绑定的是
         *           中间带 orange 带 3 个单词的字符串(*.orange.*)
         *          Q2-->绑定的是
         *          最后一个单词是 rabbit 的 3 个单词(*.*.rabbit)
         *          第一个单词是 lazy 的多个单词(lazy.#)
         *
         *          quick.orange.rabbit         被队列 Q1Q2 接收到
         *          lazy.orange.elephant        被队列 Q1Q2 接收到
         *          quick.orange.fox            被队列 Q1 接收到
         *          lazy.brown.fox              被队列 Q2 接收到
         *          lazy.pink.rabbit            虽然满足两个绑定但只被队列 Q2 接收一次
         *          quick.brown.fox             不匹配任何绑定不会被任何队列接收到会被丢弃
         *          quick.orange.male.rabbit    是四个单词不匹配任何绑定会被丢弃
         *          lazy.orange.male.rabbit     是四个单词但匹配 Q2
         */

        //创建消息集合
        Map<String,String> messageMap = new HashMap<String,String>();
        messageMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        messageMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        messageMap.put("quick.orange.fox","被队列 Q1 接收到");
        messageMap.put("lazy.brown.fox","被队列 Q2 接收到");
        messageMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        messageMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        messageMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        messageMap.put("lazy.orange.male.rabbit ","是四个单词但匹配 Q2");

        for (String routingKey: messageMap.keySet()
             ) {
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,messageMap.get(routingKey).getBytes("UTF-8"));
            System.out.println("生产者发送消息 ： "+ messageMap.get(routingKey));
        }






    }
}
