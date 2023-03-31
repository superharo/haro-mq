package online.superh.rabbitmq.mqnative.ack.consumer.deadExchange;

import com.rabbitmq.client.*;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;
import online.superh.rabbitmq.mqnative.direct.producer.DirectProducer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-03-31 17:46
 */
public class DLXConsumer {

    public static final String DEAD_EXCAHNGE = "dead_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明交换器
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCAHNGE, BuiltinExchangeType.DIRECT);
        //声明死信队列
        String deadQueue = "dead-queue";
        // 1-queuename 2-持久化 3-是否排外的 4-自动删除 5-配置产数
        channel.queueDeclare(deadQueue, false, false, false, null);
        //死信队列绑定死信交换机与 routingkey
        channel.queueBind(deadQueue, DEAD_EXCAHNGE, "dead");
        //正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        //正常队列设置死信交换机 参数 key 是固定值
        params.put("x-dead-letter-exchange", DEAD_EXCAHNGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "dead");
        //设置队列并绑定死信
        String queueName = "haroqueue001";
        channel.queueDeclare(queueName,false,false,false,params);
        //绑定路由键
        String routeKey ="haro";
        channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routeKey);
        //声明消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String msg = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("Received："+ s +"--->" + envelope.getRoutingKey() +"--->"+msg);
                //拒绝后计入死信队列
                channel.basicReject(envelope.getDeliveryTag(),false);
                // channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        System.out.println("waiting for msg ......");
        //消费者绑定队列消费 (关闭自动提交)
        channel.basicConsume(queueName,false,consumer);
    }
}
