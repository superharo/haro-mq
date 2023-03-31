package online.superh.rabbitmq.mqnative.ack.consumer.deadExchange;

import com.rabbitmq.client.*;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: 死信消费者
 * @date: 2023-03-30 17:03
 */
public class NormalConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws UnsupportedEncodingException {
                String msg = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("dead-queue Received："+ s +"--->" + envelope.getRoutingKey() +"--->"+msg);
            }
        };
        System.out.println("waiting for msg ......");
        //消费者绑定队列消费 (自动提交)
        channel.basicConsume( "dead-queue",true,consumer);
    }

}
