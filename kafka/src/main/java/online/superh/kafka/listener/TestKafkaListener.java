package online.superh.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import online.superh.kafka.common.KafkaTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-04-06 17:27
 */
@Slf4j
@Component
public class TestKafkaListener {
    //concurrency = 分区数提高消费能力
    // @KafkaListener(topics = KafkaTopic.SPRING_TEST_TOPIC,concurrency ="2")
    // public void onMessage1(ConsumerRecord<?, ?> consumerRecord) {
    //     Optional<?> optional = Optional.ofNullable(consumerRecord.value());
    //     if (optional.isPresent()) {
    //         Object msg = optional.get();
    //         log.info("线程：{}--消费message:{},topic:{},分区：{}，offset:{} ",Thread.currentThread().getName(),msg,consumerRecord.topic(),consumerRecord.partition(),consumerRecord.offset());
    //     }
    // }

    /*
        批量处理
     */
    @KafkaListener(topics = KafkaTopic.SPRING_TEST_TOPIC,concurrency ="2")
    public void onMessageBatch(List<ConsumerRecord<?, ?>> consumerRecords, Acknowledgment ack) {
        log.error("批量拉取数量：{}",consumerRecords.size());
        for (ConsumerRecord<?, ?> consumerRecord : consumerRecords) {
            log.info("线程：{}--消费message:{},topic:{},分区：{}，offset:{} ",Thread.currentThread().getName(),consumerRecord.value(),consumerRecord.topic(),consumerRecord.partition(),consumerRecord.offset());
        }
        // 手动提交offset
        ack.acknowledge();
        log.error("批量提交");
    }

    // @KafkaListener(topics = KafkaTopic.SPRING_TEST_TOPIC)
    // public void handler(String message){
    //     System.out.println("message = " + message);
    // }

    // @KafkaListener(topics = KafkaTopic.SPRING_TEST_TOPIC)
    // public void handlerByManual(ConsumerRecord<?, ?> consumerRecord, Acknowledgment ack){
    //     Object message = consumerRecord.value();
    //     System.out.println("message = " + message);
    //     //这里是只有消息为test的时候，才会确认提交
    //     if ("test".equals(message)){
    //         System.out.println("确认收到消息");
    //         //确认收到消息
    //         ack.acknowledge();
    //     }else {
    //         System.out.println("模拟发生故障");
    //     }
    // }

}
