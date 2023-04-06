package online.superh.kafka.nativeclient.consumer;

import org.apache.kafka.clients.consumer.*;
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
public class HelloKafkaConsumer {

    public static void main(String[] args) {
        //消费者设置
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"10.0.0.8:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        //设置群组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"idea1");
        //重要配置
        /*
        当 Kafka 中没有初始偏移量或当前偏移量在服务器中不存在（如，数据被删除了），该如何处理？
            earliest：自动重置偏移量到最早的偏移量。
            latest：默认，自动重置偏移量为最新的偏移量。
            none：如果消费组原来的（previous）偏移量不存在，则向消费者抛异常。
            anything：向消费者抛异常。
         */
        // properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        /*
            enable.auto.commit :默认值为 true，消费者会自动周期性地向服务器提交偏移
            auto.commit.interval.ms:如果设置了 enable.auto.commit 的值为 true， 则该值定义了消费者偏移量向 Kafka 提交的频率，默认 5s。
         */
        // properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);
        // properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,5);
        /*
            一次 poll 拉取数据返回消息的最大条数，默认是 500 条。
         */
        // properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,500);
        /*
            分配给消费者的分区侧率：
                （1）RangeAssignor
                （2）RoundRobinAssignor:类似轮训
         */
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,Collections.singletonList(RangeAssignor.class));
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
