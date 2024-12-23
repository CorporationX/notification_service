package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.LikeEventListener;
import faang.school.notificationservice.listener.RecommendationReceivedEventListener;
import faang.school.notificationservice.listener.SubscriptionEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final RecommendationReceivedEventListener recommendationReceivedEventListener;
    private final LikeEventListener likeEventListener;
    private final CommentEventListener commentEventListener;
    private final SubscriptionEventListener subscriptionEventListener;

    @Bean
    ChannelTopic recommendationTopic() {
        return new ChannelTopic(redisProperties.channel().recommendation());
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(redisProperties.channel().like());
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(redisProperties.channel().comment());
    }

    @Bean
    public ChannelTopic subscriptionTopic() {
        return new ChannelTopic(redisProperties.channel().subscriptionChannel());
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(recommendationReceivedEventListener, recommendationTopic());
        container.addMessageListener(likeEventListener, likeTopic());
        container.addMessageListener(commentEventListener, commentTopic());
        container.addMessageListener(subscriptionEventListener, subscriptionTopic());
        return container;
    }
}
