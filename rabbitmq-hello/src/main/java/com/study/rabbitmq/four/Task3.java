package com.study.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.study.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/09/13:49
 * @Description:
 * 生产者 ： 持久化队列（数据保存在磁盘中）
 */
public class Task3 {

    public static final String DURABLE_QUEUE_NAME="durable_queue";

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
            //让消息队列持久化，重启mq后也会存在该队列
            boolean durable = true;
            channel.queueDeclare(DURABLE_QUEUE_NAME,durable,false,false,null);

            //开启发布确认
            channel.confirmSelect();


            //从控制台当中接受信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String message = scanner.next();
                //像队列中推送
                /**
                 * @exchange: 交换机设置为空，使用默认交换机
                 * @props ：（设置生产者发送消息为持久化消息（要求保存在磁盘上）） null 保存在内存中
                 *    MessageProperties.PERSISTENT_TEXT_PLAIN （要求保存在磁盘上）
                 */
                channel.basicPublish("",DURABLE_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN
                        ,message.getBytes());
                System.out.println("发送消息完成:"+message);
            }
        }
    }

}
