package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.EventStartListener;
import faang.school.notificationservice.config.serializer.GenericJacksonConfig;
import faang.school.notificationservice.listener.MentorshipOfferedListener;
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
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final Map<MessageListenerAdapter, ChannelTopic> adaptersTopics = new HashMap<>();

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisProperties.getHost(),
                redisProperties.getPort());
        if (StringUtils.hasText(redisProperties.getPassword())) {
            configuration.setPassword(redisProperties.getPassword());
        }
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setHashKeySerializer(stringSerializer);
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.setDefaultSerializer(stringSerializer);
        return template;
    }

    @Bean
    public ChannelTopic eventStartTopic(@Value("${spring.redis.topics.name.event-start-topic}") String topicName) {
        return new ChannelTopic(topicName);
    }

    @Bean
    public MessageListenerAdapter eventStartAdapter(EventStartListener eventStartListener,
                                                    ChannelTopic eventStartTopic) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(eventStartListener, "onMessage");
        adaptersTopics.put(adapter, eventStartTopic);
        return adapter;
    }

    @Bean
    public ChannelTopic mentorshipOfferedTopic(@Value("${spring.redis.topics.name.mentorship-offered}") String topicName) {
        return new ChannelTopic(topicName);
    }

    @Bean
    public MessageListenerAdapter mentorshipOfferedAdapter(MentorshipOfferedListener mentorshipOfferedListener, ChannelTopic mentorshipOfferedTopic) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(mentorshipOfferedListener, "onMessage");
        adaptersTopics.put(adapter, mentorshipOfferedTopic);
        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        adaptersTopics.forEach(container::addMessageListener);
        return container;
    }
}