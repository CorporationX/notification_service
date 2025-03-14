package faang.school.notificationservice.config.context;

import faang.school.notificationservice.service.listener.LikeEventListener;
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
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    ChannelTopic likeTopic() {
        return new ChannelTopic("like_topic");
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter likeEventListener) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likeEventListener, likeTopic());
        return container;
    }

    @Value("${spring.data.redis.host}")
    private String redisHostName;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
       // redisConfig.setHostName(redisHostName);
       // redisConfig.setPort(redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Long> redisTemplate() {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
       // redisTemplate.setHashKeySerializer(new StringRedisSerializer());
       // redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
       // redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
       // redisTemplate.setEnableTransactionSupport(true);
       // redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
