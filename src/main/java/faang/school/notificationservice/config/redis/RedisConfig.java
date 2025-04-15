package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.config.properties.RedisChannelsProperties;
import faang.school.notificationservice.listener.FollowerEventListener;
import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
import faang.school.notificationservice.listener.RecommendationReceivedEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@EnableConfigurationProperties(RedisChannelsProperties.class)
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisChannelsProperties channelsProperties;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    MessageListenerAdapter recommendationReceivedEventListenerAdapter(RecommendationReceivedEventListener recommendationReceivedEventListener) {
        return new MessageListenerAdapter(recommendationReceivedEventListener);
    }

    @Bean
    MessageListenerAdapter followerEventListenerAdapter(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    MessageListenerAdapter mentorshipAcceptedEventListenerAdapter(MentorshipAcceptedEventListener mentorshipAcceptedEventListener) {
        return new MessageListenerAdapter(mentorshipAcceptedEventListener);
    }

    @Bean
    ChannelTopic recommendationReceivedEventTopic() {
        return new ChannelTopic(channelsProperties.recommendationReceivedChannel());
    }

    @Bean
    ChannelTopic mentorshipAcceptedTopic() {
        return new ChannelTopic(mentorshipAcceptedTopic);
    }

    @Bean
    ChannelTopic followerEventTopic() {
        return new ChannelTopic(channelsProperties.followerChannel());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(
            MessageListenerAdapter recommendationReceivedEventListenerAdapter,
            MessageListenerAdapter followerEventListenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recommendationReceivedEventListenerAdapter, recommendationReceivedEventTopic());
        container.addMessageListener(followerEventListenerAdapter, followerEventTopic());
        container.addMessageListener(recommendationReceivedEventListenerAdapter, mentorshipAcceptedTopic());
        return container;
    }
}
