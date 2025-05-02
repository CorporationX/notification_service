package faang.school.notificationservice.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis.listener.thread-pool")
@Getter @Setter
public class RedisThreadPoolProperties {

    private int coreSize;
    private int maxSize;
    private int queueCapacity;
    private String threadNamePrefix;
}
