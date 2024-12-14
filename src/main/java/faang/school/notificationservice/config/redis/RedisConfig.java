package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.SkillAcquiredEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.mentorship_accepted}")
    private String mentorshipAcceptedTopic;

    @Value("${spring.data.redis.channel.skill_acquired}")
    private String skillAcquiredTopic;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public MessageListenerAdapter mentorshipAcceptedListener(MentorshipAcceptedEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public ChannelTopic mentorshipAcceptedTopic() {
        return new ChannelTopic(mentorshipAcceptedTopic);
    }

    @Bean
    public MessageListenerAdapter skillAcquiredMessageListener(SkillAcquiredEventListener eventListener) {
        return new MessageListenerAdapter(eventListener);
    }

    @Bean
    public ChannelTopic skillAcquireTopic() {
        return new ChannelTopic(skillAcquiredTopic);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(JedisConnectionFactory redisConnectionFactory,
                                                 MessageListenerAdapter mentorshipAcceptedListener,
                                                 MessageListenerAdapter skillAcquiredMessageListener,
                                                 ChannelTopic skillAcquireTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(mentorshipAcceptedListener, mentorshipAcceptedTopic());
        container.addMessageListener(skillAcquiredMessageListener, skillAcquireTopic);
        return container;
    }
}
