package faang.school.notificationservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    private int port;
    private String host;
    private Channel channel;

    @PostConstruct
    public void logProperties() {
        log.info("Redis Properties Loaded: host={}, port={}, recommendationRequestChannel={}",
                host, port, channel != null ? channel.getRecommendationRequestChannel() : "null");
    }

    @Data
    public static class Channel {
        private String recommendationRequestChannel;
    }
}
