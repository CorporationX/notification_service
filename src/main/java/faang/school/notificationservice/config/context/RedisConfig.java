package faang.school.notificationservice.config.context;

import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.listener.LikeEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {
    //    @Value("$(spring.data.redis.host)")
    private String redisHost = "localhost";

    //    @Value("$(spring.data.redis.port)")
    private int redisPort = 6379;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        JedisConnectionFactory factory = new JedisConnectionFactory(redisConfig);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new Jackson2JsonRedisSerializer<>(LikePostEvent.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(LikePostEvent.class));
        return template;
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("like_topic");
    }

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(likeEventListener);
        adapter.setSerializer(new Jackson2JsonRedisSerializer<>(LikePostEvent.class));
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListenerAdapter, topic());
        log.info("RedisMessageListenerContainer created and subscribed to topic: {}", topic().getTopic());
        return container;
    }
}
