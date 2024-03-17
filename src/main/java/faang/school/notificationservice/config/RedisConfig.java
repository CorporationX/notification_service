package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.FollowerEventListener;
import faang.school.notificationservice.listener.GoalCompletedEventListener;
import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
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
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.goal_completed}")
    private String goalCompletedChannelName;

    @Value("${spring.data.redis.channel.mentorship_accepted_channel}")
    private String mentorshipAcceptedChannel;

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannel;


    private final GoalCompletedEventListener goalCompletedEventListener;
    private final MentorshipAcceptedEventListener mentorshipAcceptedEventListener;
    private final FollowerEventListener followerEventListener;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(config);
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
    ChannelTopic goalCompletedChannel() {
        return new ChannelTopic(goalCompletedChannelName);
    }

    @Bean
    MessageListenerAdapter goalCompletedListener() {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    ChannelTopic mentorshipAcceptedChannel() {
        return new ChannelTopic(mentorshipAcceptedChannel);
    }

    @Bean
    MessageListenerAdapter mentorshipAcceptedListener() {
        return new MessageListenerAdapter(mentorshipAcceptedEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(goalCompletedListener(), goalCompletedChannel());
        container.addMessageListener(mentorshipAcceptedListener(), mentorshipAcceptedChannel());
        return container;
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerChannel);
    }

    @Bean
    MessageListenerAdapter followerListener() {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter followerListener) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerListener, followerTopic());
        return container;
    }
}