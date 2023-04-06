package online.superh.kafka.nativeclient.consumer.single;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @version: 1.0
 * @author: haro
 * @description: 独立消费者
 * @date: 2023-04-04 14:24
 */
public class SingleKafkaConsumer {

    public static void main(String[] args) {
        //消费者设置
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"10.0.0.8:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        //独立消费者（适用于分区固定，消费者固定）
        KafkaConsumer<String,String> kafkaConsumer = new KafkaConsumer<>(properties);
        List<TopicPartition> topicPartitionList = new ArrayList<>();
        //获取主题分区信息
        List<PartitionInfo> partitionInfoList = kafkaConsumer.partitionsFor("test");
        if (null != partitionInfoList){
            for (PartitionInfo partitionInfo :partitionInfoList){
                topicPartitionList.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
            }
        }
        //不订阅分区，独立消费者消费那些分区
        kafkaConsumer.assign(topicPartitionList);
        try {
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
