package faang.school.notificationservice.config;

import faang.school.notificationservice.messaging.GoalCompletedEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class RedisConfig {

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
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory сonnectionFactory,
                                                       ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(сonnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return template;
    }

    @Bean
    public MessageListenerAdapter recommendationListener(RecommendationReceiveListener receiveListener) {
        return new MessageListenerAdapter(receiveListener);
    }

    @Bean
    public MessageListenerAdapter goalCompletedEventListener(GoalCompletedEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    ChannelTopic recommendationTopic(@Value("${spring.data.redis.channel.receive-recommendation}") String topic) {
        return new ChannelTopic(topic);
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedTopicName);
    }


    @Bean
    public RedisMessageListenerContainer redisContainer(JedisConnectionFactory connectionFactory,
                                                        MessageListenerAdapter goalCompletedEventListener,
                                                        ChannelTopic goalCompletedTopic,
                                                        MessageListenerAdapter recommendationListener,
                                                        ChannelTopic recommendationTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(goalCompletedEventListener, goalCompletedTopic);
        container.addMessageListener(recommendationListener, recommendationTopic);
        return container;
    }
}
