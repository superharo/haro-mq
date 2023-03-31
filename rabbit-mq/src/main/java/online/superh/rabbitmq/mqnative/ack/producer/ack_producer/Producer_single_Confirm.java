package online.superh.rabbitmq.mqnative.ack.producer.ack_producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-03-31 11:54
 */
public class Producer_single_Confirm {
    /*
        消息的确认，是指生产者投递消息后，如果Broker收到消息，则会给我们生产这一个应答。
     */
    public static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtil.getChannel();
        //设置交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //confirm_确认
        //4 指定我们的消息投递模式: 消息的确认模式
         channel.confirmSelect();
        //路由键
        String routeKey = "KKKDDDDD";
        //发布消息
        String msg = "Hello RabbitMQ";
        //设置发布失败通知
        for (int i = 0; i < 3; i++) {
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, msg.getBytes());
            System.out.println(channel.waitForConfirms());
            // 简单确认
            if (channel.waitForConfirms()){
                System.out.println("Send:" + routeKey + ":" + msg);
            }else {
                System.out.println("Send Failed");
            }
        }
    }

}
