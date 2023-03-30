package online.superh.rabbitmq.mqnative.direct.consumer;

import com.rabbitmq.client.*;
import online.superh.rabbitmq.mqnative.direct.producer.DirectProducer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: 一个连接 多个channel
 * @date: 2023-03-30 17:03
 */
public class NormalConsumer3 {

    public static class Consumer implements Runnable {

        final Connection connection;

        public Consumer(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                Channel  channel = connection.createChannel();
                //声明交换器
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                //设置队列
                String queueName = "haroqueue001";
                channel.queueDeclare(queueName,false,false,false,null);
                //绑定路由键
                String routeKey ="haro";
                channel.queueBind(queueName,DirectProducer.EXCHANGE_NAME,routeKey);
                //声明消费者
                com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws UnsupportedEncodingException {
                        String msg = new String(bytes, StandardCharsets.UTF_8);
                        System.out.println(Thread.currentThread().getName()+"Received："+ s +"--->" + envelope.getRoutingKey() +"--->"+msg);
                    }
                };
                System.out.println(Thread.currentThread().getName()+"waiting for msg ......");
                //消费者绑定队列消费 (自动提交)
                channel.basicConsume(queueName,true,consumer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) throws IOException, TimeoutException {
        //初始化连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.0.0.8");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        Connection connection = connectionFactory.newConnection();
        new Thread(new Consumer(connection)).start();
        new Thread(new Consumer(connection)).start();
        new Thread(new Consumer(connection)).start();
    }

}
