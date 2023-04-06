package online.superh.kafka.conf;

import online.superh.kafka.common.KafkaTopic;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-04-06 17:23
 */
@Configuration
public class TopicConf {

    @Bean
    public NewTopic springTestTopic(){
        //构造主题
        return TopicBuilder.name(KafkaTopic.SPRING_TEST_TOPIC).build();

    }

}
