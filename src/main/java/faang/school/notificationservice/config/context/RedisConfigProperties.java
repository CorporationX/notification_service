package faang.school.notificationservice.config.context;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedisConfigProperties {
    private String host;
    private int port;
    private String channelFollower;
}
