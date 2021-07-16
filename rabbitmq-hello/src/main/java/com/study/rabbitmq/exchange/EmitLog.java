package com.study.rabbitmq.exchange;

import com.rabbitmq.client.Channel;
import com.study.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/10/11:07
 * @Description:
 *
 *  生产者 -》发送消息到交换机
 *
 */
public class EmitLog {

    //定义交换机名字
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");



        //从控制台当中接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //像队列中推送
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
            System.out.println("发送消息完成:"+message);
        }
    }
}
