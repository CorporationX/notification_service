package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import faang.school.notificationservice.listener.MentorshipAcceptedListener;
import faang.school.notificationservice.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
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
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig =
                new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        JedisConnectionFactory factory = new JedisConnectionFactory(redisConfig);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(MentorshipAcceptedEvent.class));
        return template;
    }

    @Bean
    public List<ChannelTopic> eventTopics() {
        List<ChannelTopic> topics = redisProperties.getTopics().values().stream()
                .map(ChannelTopic::new)
                .toList();

        log.info("Subscribed to Redis topics: {}",
                topics.stream().map(ChannelTopic::getTopic).toList());

        return topics;
    }

    @Bean
    MessageListenerAdapter mentorshipAcceptEventListener(MentorshipAcceptedListener mentorshipAcceptedListener){
        return new MessageListenerAdapter(mentorshipAcceptedListener);
    }

    RedisMessageListenerContainer redisContainer(MessageListenerAdapter mentorshipAcceptEventListener){
        RedisMessageListenerContainer redisContainer = new RedisMessageListenerContainer();
        redisContainer.setConnectionFactory(jedisConnectionFactory());
        redisContainer.addMessageListener(mentorshipAcceptEventListener,eventTopics());
        return redisContainer;
    }
}
