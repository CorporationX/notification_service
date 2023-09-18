package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.AchievementMessageSubscriber;
import faang.school.notificationservice.listener.EventStartListener;
import faang.school.notificationservice.listener.LikeEventListener;
import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.channels.achievement.name}")
    private String userAchievementChannel;
    private final AchievementMessageSubscriber achievementMessageSubscriber;
    @Value("${spring.data.redis.channels.like_channel.name}")
    private String likeChannel;
    @Value("${spring.data.redis.channels.event_start_channel.name}")
    private String eventStartChannel;
    @Value("${spring.data.redis.channels.mentorship_accepted_channel.name}")
    private String mentorshipAcceptedChannel;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       LikeEventListener likeEventListener,
                                                                       EventStartListener eventStartListener,
                                                                       MentorshipAcceptedEventListener mentorshipAcceptedEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(achievementMessageSubscriber, new ChannelTopic(userAchievementChannel));
        container.addMessageListener(likeEventListener, new ChannelTopic(likeChannel));
        container.addMessageListener(eventStartListener, new ChannelTopic(eventStartChannel));
        container.addMessageListener(mentorshipAcceptedEventListener, new ChannelTopic(mentorshipAcceptedChannel));
        return container;
    }
}
