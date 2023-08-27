package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.FollowerEventListener;
import faang.school.notificationservice.listener.MentorshipEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final FollowerEventListener followerEventListener;
    private final MentorshipEventListener mentorshipEventListener;
    private final MessageListener achievementEventListener;

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.follower}")
    private String followerTopicName;
    @Value("${spring.data.redis.channels.mentorship}")
    private String mentorshipTopicName;
    @Value("${spring.data.redis.channels.achievement}")
    private String achievementTopicName;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        MessageListenerAdapter followerListenerAdapter = new MessageListenerAdapter(followerEventListener);
        container.addMessageListener(followerListenerAdapter, new ChannelTopic(followerTopicName));

        MessageListenerAdapter achievementListenerAdapter = new MessageListenerAdapter(achievementEventListener);
        container.addMessageListener(achievementListenerAdapter, new ChannelTopic(achievementTopicName));

        MessageListenerAdapter mentorshipEventMessageListenerAdapter = new MessageListenerAdapter(mentorshipEventListener);
        container.addMessageListener(mentorshipEventMessageListenerAdapter, new ChannelTopic(mentorshipTopicName));

        return container;
    }
}
