package faang.school.notificationservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis.channel")
public record RedisChannelsProperties(
        String followerChannel,
        String achievementChannel,
        String recommendationReceivedChannel,
        String mentorshipAcceptedEventChannel,
        String skillAcquiredChannel
) {
}
