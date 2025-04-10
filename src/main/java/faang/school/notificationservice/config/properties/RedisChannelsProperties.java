package faang.school.notificationservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis.channel")
public record RedisChannelsProperties(
        String follower,
        String achievement,
        String recommendationReceived
) {
}
