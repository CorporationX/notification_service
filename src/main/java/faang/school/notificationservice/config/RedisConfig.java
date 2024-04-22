package faang.school.notificationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.RecommendationRequestEventListener;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.channel.recommendationRequest}")
    private String recommendationRequestChannel;
    private final RecommendationRequestEventListener recommendationRequestEventListener;
    private final ObjectMapper objectMapper;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        System.out.println(port);
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(objectMapper, Object.class));
        return redisTemplate;
    }

    @Bean
    public ChannelTopic recommendationRequestTopic() {
        return new ChannelTopic(recommendationRequestChannel);
    }

    @Bean
    public MessageListenerAdapter recommendationRequestListener() {
        return new MessageListenerAdapter(recommendationRequestEventListener);
    }

    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.addMessageListener(recommendationRequestListener(), recommendationRequestTopic());
        return container;
    }

}
