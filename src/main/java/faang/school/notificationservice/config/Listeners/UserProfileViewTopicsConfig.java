package faang.school.notificationservice.config.Listeners;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class UserProfileViewTopicsConfig {
    @Value("${user-profile-viewed.topic-name}")
    private String listenTopicName;

    @Value("${user-profile-viewed-DeadLetterQueue.topic-name}")
    private String dlqTopicName;

    @Value("${user-profile-viewed-DeadLetterQueue.partitions}")
    private int dlqPartitions;

    @Value("${user-profile-viewed-DeadLetterQueue.replicas}")
    private short dlqReplicas;
}
