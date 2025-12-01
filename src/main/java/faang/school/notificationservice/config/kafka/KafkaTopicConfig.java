package faang.school.notificationservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${app.kafka.topics.post-published.name}")
    private String postPublishedTopicName;
    @Value("${app.kafka.topics.post-published.partitions}")
    private int postPublishedPartitionsAmount;
    @Value("${app.kafka.topics.post-published.replicas}")
    private int postPublishedReplicasAmount;

    @Bean
    public NewTopic postPublishedTopic() {
        return TopicBuilder.name(postPublishedTopicName)
                .partitions(postPublishedPartitionsAmount)
                .replicas(postPublishedReplicasAmount)
                .build();
    }
}
