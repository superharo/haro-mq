package online.superh.rabbitmq.mqnative.ack.producer.back_excahnge;

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
 * @description: 备用机换消费者
 * @date: 2023-03-31 14:14
 */
public class Backup_Exchange_Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换器
        // channel.exchangeDeclare(online.superh.rabbitmq.mqnative.direct.producer.DirectProducer.EXCHANGE_BACK_NAME, BuiltinExchangeType.DIRECT);
        //设置队列
        String queueName = "haroqueue001_back_up";
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定路由键
        String routeKey ="haro_back";
        channel.queueBind(queueName, DirectProducer.EXCHANGE_BACK_NAME,routeKey);
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
        channel.basicConsume(queueName,true,consumer);
    }

}
