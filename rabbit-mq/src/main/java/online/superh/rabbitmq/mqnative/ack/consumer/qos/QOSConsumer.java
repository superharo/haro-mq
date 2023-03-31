package online.superh.rabbitmq.mqnative.ack.consumer.qos;

import com.rabbitmq.client.*;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;
import online.superh.rabbitmq.mqnative.direct.producer.DirectProducer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-03-31 14:55
 */
public class QOSConsumer {

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
                System.out.println("Received："+ s +"--->" + envelope.getRoutingKey() +"--->"+msg+"----tag:"+envelope.getDeliveryTag());
                //单条确认 1-从哪一条确认 2-是否开启批量确认
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        System.out.println("waiting for msg ......");
        //1---预取 3条,处理完才能继续处理  2---是否将设置应用于整个频道，而不是每个消费者
        channel.basicQos(3,true);
        //消费者绑定队列消费 (关闭自动提交)
        channel.basicConsume(queueName,false,consumer);
    }
}
