package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.RecommendationRequestedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RecommendationRequestedEventListener recommendationRequestedEventListener;

    @Bean
    public ChannelTopic recommendationRequestedTopic() {
        return new ChannelTopic("recommendation-requested");
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(recommendationRequestedEventListener, recommendationRequestedTopic());
        return container;
    }
}