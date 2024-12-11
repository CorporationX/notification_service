package faang.school.notificationservice.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.message.consumer.PostLikeEventListener;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Data
@RequiredArgsConstructor
public class RedisConfig {

    public final static String REDIS_PUBLISHER_NAME = "redisPublisher";

    @Value("${spring.data.redis.channel.like}")
    private String likeEventTopicName;

    @Value("${spring.data.redis.channel.failed-sms-message}")
    private String failedSmsMessageTopicName;

    @Bean
    public MessageListenerAdapter likeEventListenerAdapter(PostLikeEventListener postLikeEventListener) {
        return new MessageListenerAdapter(postLikeEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter likeEventListenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(likeEventListenerAdapter, likeEventTopic());
        return container;
    }

    @Bean
    public ChannelTopic likeEventTopic() {
        return new ChannelTopic(likeEventTopicName);
    }

    @Bean
    public ChannelTopic failedSmsMessageTopic() {
        return new ChannelTopic(failedSmsMessageTopicName);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
