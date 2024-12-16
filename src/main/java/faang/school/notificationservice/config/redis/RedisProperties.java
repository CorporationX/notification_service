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
    private int port;

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannel;

    @Value("${spring.data.redis.channel.unfollow}")
    private String unfollowChannel;

    @Value("${spring.data.redis.channel.follower_project}")
    private String followerProjectChannel;

    @Value("${spring.data.redis.channel.unfollow_project}")
    private String unfollowProjectChannel;
}
