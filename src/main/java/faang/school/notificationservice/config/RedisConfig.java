package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.LikePostEventListener;
import faang.school.notificationservice.listener.impl.UserFollowerEventListener;
import faang.school.notificationservice.redis.EventStartEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        LikePostEventListener likePostEventListener,
                                                        UserFollowerEventListener userFollowerListener,
                                                        EventStartEventListener eventStartEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(likePostEventListener.getAdapter(), likePostEventListener.getTopic());
        container.addMessageListener(userFollowerListener.getAdapter(), userFollowerListener.getTopic());
        container.addMessageListener(eventStartEventListener.getAdapter(), eventStartEventListener.getTopic());
        return container;
    }
}
