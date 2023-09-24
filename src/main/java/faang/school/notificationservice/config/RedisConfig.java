package faang.school.notificationservice.config;

import faang.school.notificationservice.messaging.listener.AchievementEventListener;
import faang.school.notificationservice.messaging.listener.ProfileViewEventListener;
import faang.school.notificationservice.messaging.listener.RecommendationReceivedEventListener;
import faang.school.notificationservice.messaging.listener.FollowerEventListener;
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
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannelName;
    @Value("${spring.data.redis.channel.profile_view}")
    private String channelName;
    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannelName;
    @Value("${spring.data.redis.channel.achievement}")
    private String achievementChannelName;

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
    public MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    public MessageListenerAdapter recommendationListener(RecommendationReceivedEventListener recommendationReceivedEventListener) {
        return new MessageListenerAdapter(recommendationReceivedEventListener);
    }

    @Bean
    public MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public MessageListenerAdapter achievementListener(AchievementEventListener achievementEventListener) {
        return new MessageListenerAdapter(achievementEventListener);
    }

    @Bean
    public ChannelTopic followerChannel() {
        return new ChannelTopic(followerChannelName);
    }

    @Bean
    public ChannelTopic profileViewTopic() {
        return new ChannelTopic(channelName);
    }

    @Bean
    public ChannelTopic recommendationChannel() {
        return new ChannelTopic(recommendationChannelName);
    }

    @Bean
    public ChannelTopic achievementChannel() {
        return new ChannelTopic(achievementChannelName);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter profileViewListener,
                                                 MessageListenerAdapter recommendationListener,
                                                 MessageListenerAdapter followerListener,
                                                 MessageListenerAdapter achievementListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(profileViewListener, profileViewTopic());
        container.addMessageListener(recommendationListener, recommendationChannel());
        container.addMessageListener(followerListener, followerChannel());
        container.addMessageListener(achievementListener, achievementChannel());
        return container;
    }
}