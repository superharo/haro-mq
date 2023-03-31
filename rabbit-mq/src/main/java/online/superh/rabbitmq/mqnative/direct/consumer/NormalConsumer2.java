package online.superh.rabbitmq.mqnative.direct.consumer;

import com.rabbitmq.client.*;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;
import online.superh.rabbitmq.mqnative.direct.producer.DirectProducer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: 消费者
 * @date: 2023-03-30 17:03
 */
public class NormalConsumer2 {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换器
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //设置队列
        String queueName2 = "haroqueue002";
        channel.queueDeclare(queueName2,false,false,false,null);
        //绑定路由键
        String routeKey ="haro";
        channel.queueBind(queueName2,DirectProducer.EXCHANGE_NAME,routeKey);
        //声明消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws UnsupportedEncodingException {
                String msg = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("Received："+ s +"--->" + envelope.getRoutingKey() +"--->"+msg);
            }
        };
        System.out.println("waiting for msg ......");
        //消费者绑定队列消费 (自动提交)
        channel.basicConsume(queueName2,true,consumer);
    }

}
