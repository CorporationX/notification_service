package faang.school.notificationservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    private static final String POST_PUBLISHED_TOPIC_NAME = "post-published-events";
    private static final int POST_PUBLISHED_PARTITIONS_AMOUNT = 3;
    private static final int POST_PUBLISHED_REPLICAS_AMOUNT = 1;

    @Bean
    public NewTopic postPublishedTopic() {
        return TopicBuilder.name(POST_PUBLISHED_TOPIC_NAME)
                .partitions(POST_PUBLISHED_PARTITIONS_AMOUNT)
                .replicas(POST_PUBLISHED_REPLICAS_AMOUNT)
                .build();
    }
}
