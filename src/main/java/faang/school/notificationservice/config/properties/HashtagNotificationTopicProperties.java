package faang.school.notificationservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.kafka.topic.hashtag-notification")
public record HashtagNotificationTopicProperties(
        String name,
        int partitions,
        int replicas
) {
}
