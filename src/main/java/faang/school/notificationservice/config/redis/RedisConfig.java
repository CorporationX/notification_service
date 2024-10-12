package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.EventStartEventListener;
import faang.school.notificationservice.listener.GoalCompletedEventListener;
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
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.channel.goal}")
    private String goalCompletedTopic;

    @Value("${spring.data.redis.channel.event")
    private String eventTopic;

    @Bean
    MessageListenerAdapter goalListenerAdapter(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    MessageListenerAdapter eventListenerAdapter(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration =
                new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                 MessageListenerAdapter goalListenerAdapter,
                                                 MessageListenerAdapter eventListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(goalListenerAdapter, goalCompletedTopic());
        container.addMessageListener(eventListenerAdapter, eventTopic());
        return container;
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedTopic);
    }

    @Bean
    public ChannelTopic eventTopic() {
        return new ChannelTopic(eventTopic);
    }
}