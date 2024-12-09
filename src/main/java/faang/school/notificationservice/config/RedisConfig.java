package faang.school.notificationservice.config;

import faang.school.notificationservice.message.consumer.RecommendationReceivedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RecommendationReceivedEventListener recommendationListener;

    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannel;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);

        ChannelTopic recommendationTopic = new ChannelTopic(recommendationChannel);
        MessageListenerAdapter recommendationListenerAdapter = new MessageListenerAdapter(recommendationListener);
        listenerContainer.addMessageListener(recommendationListenerAdapter, recommendationTopic);

        return listenerContainer;
    }
}
