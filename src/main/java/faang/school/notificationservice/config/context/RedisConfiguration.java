package faang.school.notificationservice.config.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.RecommendationRequestListener;
import java.util.HashMap;
import java.util.Map;

import faang.school.notificationservice.messaging.GoalCompletedEventListener;
import faang.school.notificationservice.messaging.RecommendationReceiveListener;
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
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.topic}")
    private String goalCompletedTopicName;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory,
                                                       ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return template;
    }

    @Bean
    public ChannelTopic recommendRequestTopic(@Value("{spring.data.redis.channel.recommendation}") String topic) {
        return new ChannelTopic(topic);
    }

    @Bean
    public ChannelTopic recommendationTopic(@Value("${spring.data.redis.channel.receive-recommendation}") String topic) {
        return new ChannelTopic(topic);
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedTopicName);
    }

    @Bean
    public MessageListenerAdapter recommendRequestListener(RecommendationRequestListener recommendRequestListener) {
        return new MessageListenerAdapter(recommendRequestListener);
    }

    @Bean
    public MessageListenerAdapter recommendationListener(RecommendationReceiveListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter goalCompletedListener(GoalCompletedEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            JedisConnectionFactory jedisConnectionFactory,
            Map<String, ChannelTopic> topics,
            Map<String, MessageListenerAdapter> listeners) {

        Map<MessageListenerAdapter, ChannelTopic> listenersAndTopics = new HashMap<>();
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);

        topics.forEach((topicBeanName, topic) -> {
            String listenerBeanName = topicBeanName.replace("Listener", "Topic");
            MessageListenerAdapter listener = listeners.get(listenerBeanName);
            listenersAndTopics.put(listener, topic);
        });

        listenersAndTopics.forEach(container::addMessageListener);

        return container;
    }
}
