package online.superh.rabbitmq.mqnative.ack.consumer.ackfalse;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @version: 1.0
 * @author: haro
 * @description: Direct 类型交换机---生产者
 * @date: 2023-03-30 15:31
 */
public class DirectProducer {

    /*
        生产者
            ---> 消息+routing key
                 ---> exchange(根据消息的routing key找队列)
                       ---> routing key + queue
                            ---> consumer1
                                 consumer2

     */

    public static final String EXCHANGE_NAME = "direct_exchange";

    public static final String EXCHANGE_BACK_NAME = "direct_back_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //设置交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //路由键
        String routeKey ="haro";
        //发布消息
        String msg = "Hello RabbitMQ";
        channel.basicPublish(EXCHANGE_NAME,routeKey,null,msg.getBytes());
        System.out.println("Send:"+routeKey+":"+msg);
        channel.close();
     }

}
