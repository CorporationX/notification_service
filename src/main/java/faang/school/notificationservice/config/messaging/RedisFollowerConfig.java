package faang.school.notificationservice.config.messaging;

import faang.school.notificationservice.messaging.FollowerListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisFollowerConfig {

    @Value("${spring.data.redis.channel.follower}")
    private String channel;

    @Bean
    public MessageListenerAdapter followerMessageListener(FollowerListener followerListener) {
        return new MessageListenerAdapter(followerListener);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(channel);
    }
}
