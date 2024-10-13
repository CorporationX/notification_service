package faang.school.notificationservice.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.redis.channel")
public class RedisChannelNameParams {
    private String follower;
    private String achievement;
    private String comment;
}
