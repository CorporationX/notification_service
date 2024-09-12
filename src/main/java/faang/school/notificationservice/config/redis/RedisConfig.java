package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.AchievementEventListener;
import faang.school.notificationservice.listener.LikePostEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementChannelTopic;

    @Value("${spring.data.redis.channel.post-like}")
    private String postLikeEventChannelTopic;

    @Bean
    MessageListenerAdapter achievementListener(AchievementEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    MessageListenerAdapter postLikeEventListener(LikePostEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    ChannelTopic achievementChannelTopic() {
        return new ChannelTopic(achievementChannelTopic);
    }

    @Bean
    ChannelTopic postLikeEventChannelTopic() {
        return new ChannelTopic(postLikeEventChannelTopic);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter achievementListener,
                                                                       MessageListenerAdapter postLikeEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(achievementListener, achievementChannelTopic());
        container.addMessageListener(postLikeEventListener, postLikeEventChannelTopic());
        return container;
    }
}
