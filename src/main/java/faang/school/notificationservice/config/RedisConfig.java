package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.RecommendationRequestedEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final RecommendationRequestedEventListener recommendationRequestedEventListener;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        log.info("Creating JedisConnectionFactory with host: {} and port: {}",
                redisProperties.getHost(),
                redisProperties.getPort());
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    ChannelTopic recommendationRequestTopic() {
        String topicName = redisProperties.getChannel().getRecommendationRequestChannel();
        log.info("Creating ChannelTopic for recommendation request: {}", topicName);
        return new ChannelTopic(topicName);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            JedisConnectionFactory jedisConnectionFactory,
            ChannelTopic recommendationRequestTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);

        log.info("Registering RecommendationRequestedEventListener on topic: {}", recommendationRequestTopic.getTopic());
        container.addMessageListener(recommendationRequestedEventListener, recommendationRequestTopic);

        container.setErrorHandler(t -> log.error("Redis listener error: {}", t.getMessage(), t));

        return container;
    }
}
