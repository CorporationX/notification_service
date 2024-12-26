package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.EventStartEventListener;
import faang.school.notificationservice.listener.LikeEventListener;
import faang.school.notificationservice.listener.RecommendationReceivedEventListener;
import faang.school.notificationservice.listener.EventStartEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final RecommendationReceivedEventListener recommendationReceivedEventListener;
    private final LikeEventListener likeEventListener;
    private final CommentEventListener commentEventListener;

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        log.info("Lettuce client for Redis is configured: host = {}, port = {}", redisProperties.getHost(), redisProperties.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    MessageListenerAdapter eventStartListener(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    ChannelTopic eventStartTopic() {
        return new ChannelTopic(redisProperties.getChannel().getEventStartEventChannel());
    }

    @Bean
    ChannelTopic recommendationTopic() {
        return new ChannelTopic(redisProperties.getChannel().getRecommendation());
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(redisProperties.getChannel().getLike());
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(redisProperties.getChannel().getComment());
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            LettuceConnectionFactory lettuceConnectionFactory,
            ChannelTopic recommendationTopic,
            ChannelTopic likeTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(recommendationReceivedEventListener, recommendationTopic);
        container.addMessageListener(likeEventListener, likeTopic);
        container.addMessageListener(commentEventListener, commentTopic());
        return container;
    }

    @Bean
    RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter eventStartListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(eventStartListener, eventStartTopic());
        return container;
    }
}
