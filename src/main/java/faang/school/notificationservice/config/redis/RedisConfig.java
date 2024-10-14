package faang.school.notificationservice.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class RedisConfig {

    @Bean(name = "followerTopic")
    ChannelTopic followerTopic(@Value("${spring.data.redis..channel.follower}") String topicName) {
        return new ChannelTopic(topicName);
    }
}
