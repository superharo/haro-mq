package online.superh.kafka.nativeclient.producer.async;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @version: 1.0
 * @author: haro
 * @description: 异步带回调的发送
 * @date: 2023-04-04 15:18
 */
public class AsyncKafkaProducer {

    public static void main(String[] args) {
        //设置
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"10.0.0.8:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        try {
            ProducerRecord<String, String> record;
            for (int i = 0; i < 4; i++) {
                //key 值决定分区，采用负载均衡（默认分区器）
                // record = new ProducerRecord<String,String>("test",String.valueOf(i),"haro");
                record = new ProducerRecord<String,String>("test","haro");
                //传入回调函数异步拉取数据
              kafkaProducer.send(record,(recordMetadata,exception)->{
                    if (null != exception) {
                        exception.printStackTrace();
                    }
                    if (null != recordMetadata) {
                        System.out.println("partition:"+recordMetadata.partition()+"   offset:"+recordMetadata.offset());
                    }
                });
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            kafkaProducer.close();
        }
    }

}
