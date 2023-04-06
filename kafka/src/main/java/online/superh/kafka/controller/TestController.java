package online.superh.kafka.controller;

import online.superh.kafka.common.KafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-04-06 17:25
 */
@RestController
public class TestController {


    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;

    @GetMapping("/send/{msg}")
    public String sendMessage(@PathVariable("msg") String msg) {
        kafkaTemplate.send(KafkaTopic.SPRING_TEST_TOPIC, msg);
        return "发送成功";
    }


}
