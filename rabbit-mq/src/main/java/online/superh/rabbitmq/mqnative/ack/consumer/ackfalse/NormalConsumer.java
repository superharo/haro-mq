package online.superh.rabbitmq.mqnative.ack.consumer.ackfalse;

import com.rabbitmq.client.*;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;
import online.superh.rabbitmq.mqnative.direct.producer.DirectProducer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: 消费者
 * @date: 2023-03-30 17:03
 */
public class NormalConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换器
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //设置队列
        String queueName = "haroqueue001";
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定路由键
        String routeKey ="haro";
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routeKey);
        //声明消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String msg = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("Received："+ s +"--->" + envelope.getRoutingKey() +"--->"+msg);
                //确认消费
                // channel.basicAck(envelope.getDeliveryTag(), false);
                //拒绝消费
                /*
                    参数1： 消息
                    参数2： 是否应用于多消息
                    参数3： 是否重新放回队列，否则丢弃或者进入死信队列
                 */
                // channel.basicNack(envelope.getDeliveryTag(),false,false);
                /*
                    参数1： 消息
                    参数2： 是否重新放回队列，否则丢弃或者进入死信队列
                 */
                channel.basicReject(envelope.getDeliveryTag(),true);

            }
        };
        System.out.println("waiting for msg ......");
        //消费者绑定队列消费、关闭自动消费
        channel.basicConsume(queueName,false,consumer);
    }

}
