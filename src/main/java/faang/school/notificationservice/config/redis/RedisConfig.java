package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.CommentEventListener;
import lombok.RequiredArgsConstructor;
import faang.school.notificationservice.listener.EventStartListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final CommentEventListener listener;

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.channel.event}")
    private String eventTopic;
    @Value("${spring.data.redis.channel.comment}")
    private String commentTopic;

    @Bean
    public MessageListenerAdapter eventStartListenerAdapter(EventStartListener eventStartListener) {
        return new MessageListenerAdapter(eventStartListener);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter eventStartListenerAdapter) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(eventStartListenerAdapter, topic());
        container.addMessageListener(listener, commentTopic());
        return container;
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplateForSending(JedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(eventTopic);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(commentTopic);
    }
}
