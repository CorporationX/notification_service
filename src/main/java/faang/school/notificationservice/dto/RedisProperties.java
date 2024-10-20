package faang.school.notificationservice.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    private String host;
    private int port;
    private Channels channel;

    @Data
    public static class Channels {
        private String comment;
        private String like;
        private String achievement;
        private String follower;
    }
}
