package online.superh.rabbitmq.mqnative.topic.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;
import online.superh.rabbitmq.mqnative.topic.TopicExChange;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: Topic交换器生产者
 * @date: 2023-03-31 11:12
 */
public class TopicProducer {

    /*
        生产者+routekey
                        ---> Topic交换机，根据routekey匹配队列
                                                            ---> routekey.*(匹配一个) + queue1
                                                            ---> routekey.#(匹配多个) + queue2

     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //设置交换器
        channel.exchangeDeclare(TopicExChange.name, BuiltinExchangeType.TOPIC);
        //路由键
        String routeKey ="haro_topic_aaaa";
        //生产者-发布消息
        String msg = "Hello RabbitMQ Topic";
        channel.basicPublish(TopicExChange.name,routeKey,null,msg.getBytes());
        System.out.println("Send:"+msg);
        channel.close();
    }
}
