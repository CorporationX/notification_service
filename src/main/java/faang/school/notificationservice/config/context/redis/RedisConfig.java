package faang.school.notificationservice.config.context.redis;

import faang.school.notificationservice.listener.LikeEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.channel.like}")
    private String likeChannel;

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            @Qualifier("listenerLikeChannelAdapter") MessageListenerAdapter listenerLikeChannelAdapter,
            @Qualifier("likeTopic") ChannelTopic likeTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerLikeChannelAdapter, likeTopic);
        return container;
    }

    @Bean
    @Qualifier("listenerLikeChannelAdapter")
    public MessageListenerAdapter listenerLikeChannelAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    @Qualifier("likeTopic")
    public ChannelTopic likeTopic() {
        return new ChannelTopic(likeChannel);
    }
}