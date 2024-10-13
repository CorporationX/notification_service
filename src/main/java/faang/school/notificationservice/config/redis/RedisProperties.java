package faang.school.notificationservice.config.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    private int port;
    private String host;
    private Channels channels;

    @Data
    public static class Channels {
        private ChannelConfig follower;
        private ChannelConfig achievement;
    }

    @Data
    public static class ChannelConfig {
        private String name;
    }
}
