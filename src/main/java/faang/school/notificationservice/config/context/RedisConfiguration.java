package faang.school.notificationservice.config.context;

import faang.school.notificationservice.listener.RecommendationRequestListener;
import java.util.HashMap;
import java.util.Map;
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

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public ChannelTopic recommendRequestTopic(@Value("{spring.data.redis.channel.recommendation}") String topicName) {
        return new ChannelTopic(topicName);
    }

    @Bean
    public MessageListenerAdapter recommendRequestListener(RecommendationRequestListener recommendRequestListener) {
        return new MessageListenerAdapter(recommendRequestListener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            Map<String, ChannelTopic> topics,
            Map<String, MessageListenerAdapter> listeners) {

        Map<MessageListenerAdapter, ChannelTopic> listenersAndTopics = new HashMap<>();
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        topics.forEach((topicBeanName, topic) -> {
            String listenerBeanName = topicBeanName.replace("Listener", "Topic");
            MessageListenerAdapter listener = listeners.get(listenerBeanName);
            listenersAndTopics.put(listener, topic);
        });

        listenersAndTopics.forEach(container::addMessageListener);

        return container;
    }
}
