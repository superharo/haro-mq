package online.superh.kafka.nativeclient.consumer.group;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-04-04 14:24
 */
public class KafkaConsumerGroupA02 {

    public static void main(String[] args) {
        //消费者设置
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"10.0.0.8:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        //设置群组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"groupA");
        KafkaConsumer<String,String> kafkaConsumer = new KafkaConsumer<>(properties);
        try {
            //订阅主题
            kafkaConsumer.subscribe(Collections.singletonList("test"));
            while (true){
                //拉取消息
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format("topic: %s,分区: %d,偏移量：%d,key: %s,value: %s%n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value()));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            kafkaConsumer.close();
        }
    }

}
