package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.FollowerEventListener;
import faang.school.notificationservice.listener.LikeEventListener;
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
    public MessageListenerAdapter messageListenerFollowerAdapter(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public MessageListenerAdapter messageListenerLikeAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic followerChannelTopic() {
        return new ChannelTopic(properties.getChannels().get("follower").getName());
    }

    @Bean
    public ChannelTopic likeChannelTopic() {
        return new ChannelTopic(properties.getChannels().get("like").getName());
    }

    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messageListenerFollowerAdapter,
            MessageListenerAdapter messageListenerLikeAdapter,
            ChannelTopic followerChannelTopic,
            ChannelTopic likeChannelTopic) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(messageListenerFollowerAdapter, followerChannelTopic);
        container.addMessageListener(messageListenerLikeAdapter, likeChannelTopic);

        return container;
    }
}
