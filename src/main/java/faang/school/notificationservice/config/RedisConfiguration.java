package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.AchievementEventListener;
import faang.school.notificationservice.listener.FollowerEventListener;
import faang.school.notificationservice.listener.PostLikeEventListener;
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

    @Value("${spring.data.redis.channel.post-like}")
    public String postLikeChannelTopicName;

    @Value("${spring.data.redis.channel.follow-user}")
    private String followUserChannelTopicName;

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
    public MessageListenerAdapter followUserListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public MessageListenerAdapter postLikeListener(PostLikeEventListener postLikeEventListener) {
        return new MessageListenerAdapter(postLikeEventListener);
    }

    @Bean
    public ChannelTopic achievementChannel() {
        return new ChannelTopic(achievementChannelTopicName);
    }

    public ChannelTopic postLikeChannel() {
        return new ChannelTopic(postLikeChannelTopicName);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(AchievementEventListener achievementEventListener,
                                                        FollowerEventListener followerEventListener,
                                                        PostLikeEventListener postLikeEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(achievementListener(achievementEventListener), achievementChannel());
        container.addMessageListener(followUserListener(followerEventListener), new ChannelTopic(followUserChannelTopicName));
        container.addMessageListener(postLikeListener(postLikeEventListener), postLikeChannel());
        return container;
    }
}
