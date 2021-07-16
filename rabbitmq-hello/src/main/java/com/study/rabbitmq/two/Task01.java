package com.study.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.study.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/09/13:49
 * @Description:
 * 生产者 ： 发送大量消息
 */
public class Task01 {

    private static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {
        try(Channel channel= RabbitMqUtils.getChannel()) {

            /**
             * 声明队列
             *
             * 参数1、队列名称
             * 参数2、队列李面的消息是否持久化（磁盘） 默认消息存储在内存中
             * 参数3、该队列是否只供一个消费者进行消费 是否进行消息共享，true代表可以多个消费者消费，false只能一个消费者消费
             * 参数4、是否自动删除 最后一个消费者断开连接后 该队列是否自动删除  true 删除 false 不删除
             * 参数5、其他参数（延迟消息，死信消息）
             *
             */
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            //从控制台当中接受信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String message = scanner.next();
                //像队列中推送
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
                System.out.println("发送消息完成:"+message);
            }
        }
    }

}
