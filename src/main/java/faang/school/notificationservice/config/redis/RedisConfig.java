package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.config.properties.RedisConfigurationProperties;
import faang.school.notificationservice.listener.GoalCompletedEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisConfigurationProperties redisConfigurationProperties;

    @Value("${spring.data.redis.channel.goalCompleted}")
    private String goalCompletedChannel;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisConfigurationProperties
                .getHost(), redisConfigurationProperties.getPort());
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    MessageListenerAdapter goalCompletedListener(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener, "onMessage");
    }

    @Bean
    ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedChannel);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter goalCompletedListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(goalCompletedListener, goalCompletedTopic());

        log.info("Redis listener container configured for channel: {}", goalCompletedChannel);
        return container;
    }
}
