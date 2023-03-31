package online.superh.rabbitmq.mqnative.ack.producer.back_excahnge;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import online.superh.rabbitmq.mqnative.RabbitMQUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static online.superh.rabbitmq.mqnative.direct.producer.DirectProducer.EXCHANGE_BACK_NAME;

/**
 * @version: 1.0
 * @author: haro
 * @description: 备用交换机
 * @date: 2023-03-30 15:31
 */
public class DirectProducer {

    /*
        备用交换机的优先级大于 不可路由
     */

    public static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        //声明备用交换器
        Map<String, Object> params = new HashMap<>();
        params.put("alternate-exchange", EXCHANGE_BACK_NAME);
        //设置交换器 及 备用交换器 （不可重复声明）
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT,false,false,params);
        //备用交换器声明
        channel.exchangeDeclare(EXCHANGE_BACK_NAME, BuiltinExchangeType.DIRECT,false,false,null);
        //路由键
        String routeKey ="haro";
        //发布消息
        String msg = "Hello RabbitMQ";
        channel.basicPublish(EXCHANGE_NAME,routeKey,null,msg.getBytes());
        System.out.println("Send:"+routeKey+":"+msg);
        //路由键
        String routeKey2 ="haro_back";
        //发布消息
        String msg2 = "Hello RabbitMQ _ back";
        channel.basicPublish(EXCHANGE_NAME,routeKey2,null,msg2.getBytes());
        System.out.println("Send:"+routeKey2+":"+msg2);
        channel.close();
     }

}
