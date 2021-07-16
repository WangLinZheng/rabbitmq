package com.study.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.study.rabbitmq.utils.RabbitMqUtils;
import com.study.rabbitmq.utils.SleepUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: kuzz
 * @Date: 2021/07/09/14:55
 * @Description:
 */
public class Work05 {

//    private static final String DURABLE_QUEUE_NAME="durable_queue";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1 等待接收消息处理时间较短");
        //消息消费的时候如何处理消息

        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message= new String(delivery.getBody());
            SleepUtils.sleep(1);
            System.out.println("接收到消息:"+message);
            /**
             * 1.消息标记 tag
             * 2.是否批量应答未应答消息
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };
        //采用手动应答
//        boolean autoAck=false;
        /**
         * 消费者消费消息
         * 1.消费哪个队列.名称
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者未成功消费的回调
         * 4、消费者取消消费的回调
         */
        channel.basicConsume(Task3.DURABLE_QUEUE_NAME, false,deliverCallback,(consumerTag)->
                System.out.println(consumerTag+"消费者取消消费接口回调逻辑"));
    }

}
