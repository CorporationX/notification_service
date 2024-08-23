package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.AchievementEventListener;
import faang.school.notificationservice.listener.MentorshipOfferedEventListener;
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
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    public String redisHost;

    @Value("${spring.data.redis.port}")
    public int redisPort;

    @Value("${spring.data.redis.channel.achievement}")
    public String achievementChannelTopicName;

    @Value("${spring.data.redis.channel.mentorship_offered}")
    private String mentorshipOfferedChannelName;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public MessageListenerAdapter achievementListener(AchievementEventListener achievementEventListener) {
        return new MessageListenerAdapter(achievementEventListener);
    }

    @Bean
    public MessageListenerAdapter mentorshipOfferedListener(MentorshipOfferedEventListener mentorshipOfferedEventListener) {
        return new MessageListenerAdapter(mentorshipOfferedEventListener);
    }

    @Bean
    public ChannelTopic achievementChannel() {
        return new ChannelTopic(achievementChannelTopicName);
    }

    @Bean
    public ChannelTopic mentorshipOfferedChannelTopic() {
        return new ChannelTopic(mentorshipOfferedChannelName);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter achievementListener,
                                                        MessageListenerAdapter mentorshipOfferedListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(achievementListener, achievementChannel());
        container.addMessageListener(mentorshipOfferedListener, mentorshipOfferedChannelTopic());
        return container;
    }
}
