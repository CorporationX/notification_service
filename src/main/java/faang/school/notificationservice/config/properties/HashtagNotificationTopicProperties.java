package faang.school.notificationservice.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class HashtagNotificationTopicProperties {

    @Value("${spring.data.kafka.topic.hashtag-notification.name}")
    private String name;

    @Value("${spring.data.kafka.topic.hashtag-notification.partitions}")
    private int partitions;

    @Value("${spring.data.kafka.topic.hashtag-notification.replicas}")
    private int replicas;
}
