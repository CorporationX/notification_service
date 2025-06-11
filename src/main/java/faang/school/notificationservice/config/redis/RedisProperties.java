package faang.school.notificationservice.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("spring.data.redis")
public record RedisProperties(
        String host,
        int port,
        @NestedConfigurationProperty ChannelNames channelNames
) {
    public record ChannelNames(
            String achievement,
            String follower
    ) {
    }
}
