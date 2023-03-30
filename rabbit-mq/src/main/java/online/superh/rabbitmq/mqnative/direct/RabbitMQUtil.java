package online.superh.rabbitmq.mqnative.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: RabbitMQ工具类
 * @date: 2023-03-30 17:04
 */
public class RabbitMQUtil {

    public static Channel getChannel() throws IOException, TimeoutException {
        //初始化连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.0.0.8");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        return connection.createChannel();
    }

}
