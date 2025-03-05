package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.MentorshipOfferedEventListener;
import faang.school.notificationservice.listener.RecommendationEventListener;
import faang.school.notificationservice.listener.UserProfileViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;
    private final Channels channels;
    private final RecommendationEventListener recommendationEventListener;
    private final UserProfileViewEventListener userProfileViewEventListener;
    private final MentorshipOfferedEventListener mentorshipOfferedEventListener;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(
                redisProperties.getHost(),
                redisProperties.getPort());
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        addMessageListenerInContainer(recommendationEventListener, channels.getRecommendationChannel(), container);
        addMessageListenerInContainer(userProfileViewEventListener, channels.getProfileView(), container);
        addMessageListenerInContainer(mentorshipOfferedEventListener, channels.getRecommendationMentorshipOffered(), container);
        return container;
    }

    private void addMessageListenerInContainer(MessageListener listenerAdapter,
                                               String topic,
                                               RedisMessageListenerContainer container) {
        container.addMessageListener(new MessageListenerAdapter(listenerAdapter), new ChannelTopic(topic));
    }
}
