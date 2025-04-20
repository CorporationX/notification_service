package faang.school.notificationservice.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class HashtagNotificationTopicProperties {

    @Value("${spring.kafka.topics.hashtag-notification.name}")
    private String name;

    @Value("${spring.kafka.topics.hashtag-notification.partitions}")
    private int partitions;

    @Value("${spring.kafka.topics.hashtag-notification.replicas}")
    private int replicas;
}
