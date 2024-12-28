package faang.school.notificationservice.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(String host, int port, Channel channel) {

    public record Channel(String subscriptionChannel,
                          String achievement,
                          String recommendation,
                          String like,
                          String comment,
                          String goal,
                          String mentorshipAcceptedChannel) {
    }
}