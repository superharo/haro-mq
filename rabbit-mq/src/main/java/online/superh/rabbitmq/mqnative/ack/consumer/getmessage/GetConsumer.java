package online.superh.rabbitmq.mqnative.ack.consumer.getmessage;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;
import online.superh.rabbitmq.mqnative.direct.producer.DirectProducer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: 拉取消息
 * @date: 2023-03-31 14:43
 */
public class GetConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换器
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //设置队列
        String queueName = "haroqueue001";
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定路由键
        String routeKey ="haro";
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routeKey);
        //无限循环拉取消息
        while(true){
            //不自动确认消息 false不自动确认
            GetResponse getResponse = channel.basicGet(queueName,false);
            if (getResponse != null){
                System.out.println("接收到："+getResponse.getEnvelope().getRoutingKey()+"---"+new String(getResponse.getBody()));
            }
            //手动确认 1-消息编号 2-是否批量确认
            channel.basicAck(0,true);
            Thread.sleep(1000);
        }
    }

}
