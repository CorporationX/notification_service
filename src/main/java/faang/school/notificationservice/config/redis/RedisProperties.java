package faang.school.notificationservice.config.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RedisProperties {

    @Value("${spring.data.redis.channel.event-participation}")
    private String eventParticipationChannel;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;
}
