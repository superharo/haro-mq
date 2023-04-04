package online.superh.kafka.nativeclient.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @version: 1.0
 * @author: haro
 * @description: 发送并忘记
 * @date: 2023-04-04 14:06
 */

public class HelloKafkaProducer {
    public static void main(String[] args) {
        //设置
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"10.0.0.8:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //更多参数配置
        /*
            ack机制：
                0：生产者发送过来的数据，不需要等数据落盘应答。
                1：生产者发送过来的数据，Leader 收到数据后应答。
                -1（all）：生产者发送过来的数据，Leader+和 isr 队列.里面的所有节点收齐数据后应答。默认值是-1，-1 和 all 是等价的。
         */
        // properties.put(ProducerConfig.ACKS_CONFIG,-1);
        /*
            缓冲区一批数据最大值，默认 16k。适当增加该值，可以提高吞吐量，但是如果该值设置太大，会导致数据传输延迟增加。
         */
        // properties.put(ProducerConfig.BATCH_SIZE_CONFIG,)
        /*
            如果数据迟迟未达到 batch.size，sender 等待 linger.time之后就会发送数据。单位 ms，默认值是 0ms，表示没有延迟。生产环境建议该值大小为 5-100ms 之间。
         */
        // properties.put(ProducerConfig.LINGER_MS_CONFIG,5);
        /*
            控制发送请求的最大大小,和kafka主机最大大小相同
         */
        // properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,1*1024*1024);
        /*
            1.当消息发送出现错误的时候，系统会重发消息。retries表示重试次数。默认是 int 最大值，2147483647。
              如果设置了重试，还想保证消息的有序性，需要设置 MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION=1
              否则在重试此失败消息的时候，其他的消息可能发送成功了
            2.retry.backoff.ms 两次重试之间的时间间隔，默认是 100ms。
         */
        // properties.put(ProducerConfig.RETRIES_CONFIG,);
        // properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,);
        /*
            max.in.flight.requests.per.connection 允许最多没有返回 ack 的次数，默认为 5，开启幂等性要保证该值是 1-5 的数字。
         */
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        try {
            ProducerRecord<String, String> record;
            for (int i = 120; i < 140; i++) {
                //key 值决定分区，采用负载均衡（默认分区器）
                // record = new ProducerRecord<String,String>("test",String.valueOf(i),"haro");
                //指定分区
                record = new ProducerRecord<String,String>("test",2,String.valueOf(i),"haro"+i);
                kafkaProducer.send(record);//发送并忘记（会有重试）
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            kafkaProducer.close();
        }
    }

}
