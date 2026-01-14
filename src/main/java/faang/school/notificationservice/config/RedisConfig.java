package faang.school.notificationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.FollowerEventListener;
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
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.follower}")
    private String followerEventsTopic;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            JedisConnectionFactory redisConnectionFactory,
            ObjectMapper objectMapper
    ) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(
                new GenericJackson2JsonRedisSerializer(objectMapper)
        );
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            JedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter followerListenerAdapter,
            ChannelTopic followerTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(followerListenerAdapter, followerTopic);
        return container;
    }

    @Bean
    public MessageListenerAdapter followerListenerAdapter(FollowerEventListener eventListener) {
        return new MessageListenerAdapter(eventListener);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followerEventsTopic);
    }
}
