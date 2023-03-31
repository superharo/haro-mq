package online.superh.rabbitmq.mqnative.topic.consumer;

import com.rabbitmq.client.*;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;
import online.superh.rabbitmq.mqnative.topic.TopicExChange;
import online.superh.rabbitmq.mqnative.topic.TopicQueue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: topic交换器-消费者
 * @date: 2023-03-31 11:11
 */
public class TopicConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换器
        channel.exchangeDeclare(TopicExChange.name, BuiltinExchangeType.TOPIC);
        //设置队列
        channel.queueDeclare(TopicQueue.HARO_TEST_TOPIC_QUEUE_NAME,false,false,false,null);
        //绑定路由键
        String routeKey ="haro_topic.*";
        channel.queueBind(TopicQueue.HARO_TEST_TOPIC_QUEUE_NAME,TopicExChange.name,routeKey);
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
        //消息发送后立即被认为已经传送成功
        channel.basicConsume(TopicQueue.HARO_TEST_TOPIC_QUEUE_NAME,true,consumer);
    }
    
}
