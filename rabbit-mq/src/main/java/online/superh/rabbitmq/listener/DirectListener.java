package online.superh.rabbitmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-04-03 13:57
 */
@Slf4j
@Component
@RabbitListener(queues = {"test_direct_queue"})
public class DirectListener {

    @RabbitHandler
    public void handle(Map<String,Object> map){
        log.info("DirectListener-接收到：{}",map);
    }

}
