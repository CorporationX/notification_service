package faang.school.notificationservice.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.listener.CustomListener;
import faang.school.notificationservice.listener.RedisListenerRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;
    private final List<AbstractEventListener> listeners;
    private final List<CustomListener> listListeners;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(
                redisProperties.getHost(),
                redisProperties.getPort());
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return template;
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisListenerRegistrationService registrationService) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        listeners.forEach(listener ->
                registrationService.registerListener(container, listener, listener.getTopicName()));
        listListeners.forEach(listener ->
                registrationService.registerListener(container, listener, listener.getTopicName()));
        return container;
    }
}
