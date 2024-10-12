package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.FollowerEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties properties;

    @Bean
    public MessageListenerAdapter messageListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public ChannelTopic channelTopic() {
        String channelName = properties.getChannels().get("follower").getName();
        return new ChannelTopic(channelName);
    }

    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messageListenerAdapter,
            ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, channelTopic);
        return container;
    }
}
