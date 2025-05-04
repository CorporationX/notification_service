package faang.school.notificationservice.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.listener.thread-pool")
@Getter @Setter
public class RedisThreadPoolProperties {

    private Integer coreSize;
    private Integer maxSize;
    private Integer queueCapacity;
    private String threadNamePrefix;
}
