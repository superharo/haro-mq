package online.superh.kafka.conf;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;

import javax.annotation.PostConstruct;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-04-06 17:30
 */
@Configuration
public class KafkaListenerConf {


    @Autowired
    private  KafkaTemplate<Object,Object> kafkaTemplate;
    @PostConstruct
    private void listener() {
        kafkaTemplate.setProducerListener(new ProducerListener<Object, Object>() {
            @Override
            public void onSuccess(ProducerRecord<Object, Object> producerRecord, RecordMetadata recordMetadata) {
                System.out.println("发送成功回调,message="+ recordMetadata.offset());
            }
            @Override
            public void onError(ProducerRecord<Object, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                System.out.println("发送失败回调,message="+ recordMetadata.offset());
                exception.printStackTrace();
            }
        });
    }

}
