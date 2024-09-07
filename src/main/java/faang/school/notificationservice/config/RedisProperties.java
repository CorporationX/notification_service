package faang.school.notificationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties("spring.data.redis")
public class RedisProperties {
    private String host;
    private int port;
    private Channel channel;

    @Data
    static class Channel{
        private String follower;
        private String achievement;
        private String like;
    }
}
