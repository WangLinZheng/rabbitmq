package com.study.rabbitmq.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.study.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/10/11:41
 * @Description:
 */
public class DirectLog {


    //定义交换机名字
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);



        //从控制台当中接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //像队列中推送 指定routingKey info warning error
            channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes("UTF-8"));
            System.out.println("发送消息完成:"+message);
        }
    }
}
