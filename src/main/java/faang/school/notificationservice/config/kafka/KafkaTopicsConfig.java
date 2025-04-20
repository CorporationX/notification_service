package faang.school.notificationservice.config.kafka;

import faang.school.notificationservice.config.properties.HashtagNotificationTopicProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {

    private final HashtagNotificationTopicProperties hashtagNotificationTopic;

    @Bean
    public NewTopic hashtagNotificationTopic() {
        return createTopic(hashtagNotificationTopic.getName(),
                hashtagNotificationTopic.getPartitions(),
                hashtagNotificationTopic.getReplicas());
    }

    private NewTopic createTopic(String name, int partitions, int replicas) {
        return TopicBuilder.name(name)
                .partitions(partitions)
                .replicas(replicas)
                .compact()
                .build();
    }
}
