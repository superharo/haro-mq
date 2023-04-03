package online.superh.rabbitmq.mqnative.ack.reply;

import com.rabbitmq.client.*;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-04-03 10:51
 */
public class ReplyConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换机
        channel.exchangeDeclare(ReplyExChange.EXCHANGE_NAME, BuiltinExchangeType.DIRECT,false);
        //声明 request 队列
        channel.queueDeclare(ReplyQueue.QUEUE_NAME,false,false,false,null);
        //队列 - 交换机 - 路由 绑定
        channel.queueBind(ReplyQueue.QUEUE_NAME,ReplyExChange.EXCHANGE_NAME,"reploy");
        System.out.println("wait for msg");
        Consumer consumer = new DefaultConsumer(channel){
          @Override
          public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
              String msg = new String(body, StandardCharsets.UTF_8);
              System.out.println("Received："+ consumerTag +"--->" + envelope.getRoutingKey() +"--->"+msg);
              //拿到 response 属性
              AMQP.BasicProperties responseProperties = new AMQP.BasicProperties().builder()
                      .replyTo(properties.getReplyTo())
                      .correlationId(properties.getCorrelationId())
                      .build();
              //同时发送 response  ok
              channel.basicPublish("",responseProperties.getReplyTo(),responseProperties,"ok".getBytes());
          }
        };
        //队列绑定消费者
        channel.basicConsume(ReplyQueue.QUEUE_NAME,true,consumer);
    }

}
