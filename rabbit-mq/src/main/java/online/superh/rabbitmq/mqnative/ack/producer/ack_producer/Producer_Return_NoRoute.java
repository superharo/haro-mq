package online.superh.rabbitmq.mqnative.ack.producer.ack_producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-03-31 11:54
 */
public class Producer_Return_NoRoute {
    /*
    mandatory = true

    Return Listener用于处理一些不可路由的消息。
        我们的消息生产者，通过指定一个Exchange和Routingkey，把消息送到某一个队列中去，然后我们的消费者监听队列，进行消费处理

        但是在某些情况下，如果我们在发送消息的时候，当前的exchange不存在或者指定的routingkey路由不到，这个时候如果我们需要监听这种不可达的消息，就要使用Return Listener
     */
    public static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //设置交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //消息确认
        channel.addReturnListener((i, s, s1, s2, basicProperties, bytes) -> {
            String msg = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("发布确认："+i+"---"+msg);
            System.out.println("失败信息-s："+s);
            System.out.println("交换机-s1："+s1);
            System.out.println("路由键-s2："+s2);
        });
        //路由键
        String routeKey ="KKKDDDDD";
        //发布消息
        String msg = "Hello RabbitMQ";
        //设置发布失败通知
        for (int i = 0; i < 3; i++) {
            /*
                mandatory:如果为true, 消息不能路由到指定的队列时，则会调用basic.return方法将消息返回给生产者,
                会触发addReturnListener注册的监听器；如果为false，则broker会直接将消息丢弃
             */
            channel.basicPublish(EXCHANGE_NAME,routeKey,true,null,msg.getBytes());
            System.out.println("Send:"+routeKey+":"+msg);
        }
    }

}
