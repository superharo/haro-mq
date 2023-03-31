package online.superh.rabbitmq.mqnative.fanout.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;
import online.superh.rabbitmq.mqnative.fanout.FanoutExChange;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: Fanout交换器-生产者
 * @date: 2023-03-31 9:34
 */
@Slf4j
public class FanoutProducer {
    /*
     生产者 ---> Fanout 交换机
                            --->队列 --->消费者
                            --->队列 --->消费者
    */

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //设置交换器
        channel.exchangeDeclare(FanoutExChange.name, BuiltinExchangeType.FANOUT);
        //路由键
        String routeKey ="haro_fanout";
        //生产者-发布消息
        String msg = "Hello RabbitMQ Fanout";
        channel.basicPublish(FanoutExChange.name,routeKey,null,msg.getBytes());
        System.out.println("Send:"+msg);
        channel.close();
    }
}
