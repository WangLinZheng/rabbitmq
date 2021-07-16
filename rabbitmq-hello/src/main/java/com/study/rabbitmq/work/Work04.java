package com.study.rabbitmq.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.study.rabbitmq.utils.RabbitMqUtils;
import com.study.rabbitmq.utils.SleepUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/09/15:08
 * @Description:
 */
public class Work04 {
    private static final String ACK_QUEUE_NAME="ack_queue";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2 等待接收消息处理时间较长");
        //消息消费的时候如何处理消息

        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message= new String(delivery.getBody());
            SleepUtils.sleep(10);
            System.out.println("接收到消息:"+message);
            /**
             * 1.消息标记 tag
             * 2.是否批量应答未应答消息
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };
//        采用手动应答
        boolean autoAck=false;
        /**
         * 设置预取值
         *  0 默认
         *  1 设置不公平分发，谁效率快谁消费，不做等待
         *  2 设置预取值，至少消费N条数据
         */
        int prefetchCount = 3;
        channel.basicQos(prefetchCount);

        /**
         * 消费者消费消息
         * 1.消费哪个队列.名称
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者未成功消费的回调
         * 4、消费者取消消费的回调
         */
        channel.basicConsume(ACK_QUEUE_NAME, autoAck,deliverCallback,(consumerTag)->
                System.out.println(consumerTag+"消费者取消消费接口回调逻辑"));
    }

}

