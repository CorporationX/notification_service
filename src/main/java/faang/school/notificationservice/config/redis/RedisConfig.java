package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.EventStartEventListener;
import faang.school.notificationservice.listener.RecommendationRequestedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final EventStartEventListener eventStartEventListener;
    private final RecommendationRequestedEventListener recommendationRequestedEventListener;

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.event-starter}")
    private String eventStarter;
    @Value("${spring.data.redis.channel.recommendation-request-channel}")
    private String recommendationReqTopic;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter eventStartListenerAdapter,
                                                        MessageListenerAdapter recommendationRequestedEventListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(eventStartListenerAdapter, eventStarter());
        container.addMessageListener(recommendationRequestedEventListenerAdapter, recommendationRequestedEventTopic());

        return container;
    }

    @Bean
    public MessageListenerAdapter eventStartListenerAdapter() {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    public MessageListenerAdapter recommendationRequestedEventListenerAdapter() {
        return new MessageListenerAdapter(recommendationRequestedEventListener);
    }

    @Bean
    public ChannelTopic eventStarter() {
        return new ChannelTopic(eventStarter);
    }

    @Bean
    public ChannelTopic recommendationRequestedEventTopic() {
        return new ChannelTopic(recommendationReqTopic);
    }
}
