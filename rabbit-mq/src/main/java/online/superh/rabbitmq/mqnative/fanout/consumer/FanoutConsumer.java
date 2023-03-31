package online.superh.rabbitmq.mqnative.fanout.consumer;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;
import online.superh.rabbitmq.mqnative.fanout.FanoutExChange;
import online.superh.rabbitmq.mqnative.fanout.FanoutQueue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: Fanout交换器-消费者
 * @date: 2023-03-31 9:34
 */
@Slf4j
public class FanoutConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换器
        channel.exchangeDeclare(FanoutExChange.name, BuiltinExchangeType.FANOUT);
        //设置队列
        channel.queueDeclare(FanoutQueue.HARO_FANOUT_TEST_QUEUE,false,false,false,null);
        //绑定路由键
        String routeKey ="haro_fanout";
        channel.queueBind(FanoutQueue.HARO_FANOUT_TEST_QUEUE,FanoutExChange.name,routeKey);
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
        channel.basicConsume(FanoutQueue.HARO_FANOUT_TEST_QUEUE,true,consumer);
    }

}
