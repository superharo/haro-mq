package online.superh.rabbitmq.mqnative.ack.reply;

import com.rabbitmq.client.*;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: request - response
 * @date: 2023-04-03 10:31
 */
public class ReplyProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取 channel
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换器
        channel.exchangeDeclare(ReplyExChange.EXCHANGE_NAME, BuiltinExchangeType.DIRECT,false);
        //声明 response 队列
        String responseQueue = channel.queueDeclare().getQueue();
        System.out.println(responseQueue);
        String msgId = UUID.randomUUID().toString().replace("-","");
        //设置消息应答属性
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .replyTo(responseQueue)
                .messageId(msgId)
                .build();
        //声明消费者
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws UnsupportedEncodingException {
                String msg = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("Received："+ s +"--->" + envelope.getRoutingKey() +"--->"+msg);
            }
        };
        channel.basicConsume(responseQueue,true,consumer);
        //发送消息
        String msg = "test_reply";
        channel.basicPublish(ReplyExChange.EXCHANGE_NAME,"reploy",properties,msg.getBytes(StandardCharsets.UTF_8));
        System.out.println("producer: test_reply ok!");
    }

}
