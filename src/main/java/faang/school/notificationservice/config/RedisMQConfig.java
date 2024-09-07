package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.like.LikeEventListener;
import faang.school.notificationservice.listener.achievement.AchievementListener;
import faang.school.notificationservice.listener.follower.FollowerListener;
import faang.school.notificationservice.listener.project.ProjectFollowerEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisMQConfig {
    private final RedisProperties redisProperties;
    @Value("${spring.data.redis.channel.achievement}")
    private String achievementTopicName;
    @Value("${spring.data.redis.channel.follower}")
    private String followerTopicName;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.connection_factory}")
    private String factoryType;
    @Value("${spring.data.redis.channel.project_follower}")
    private String projectFollowerTopicName;

    @Bean
    @Lazy
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration =
                new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    @Lazy
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisConnectionFactory connectionFactory() {
        return switch (factoryType) {
            case "jedis" -> jedisConnectionFactory();
            case "lettuce" -> lettuceConnectionFactory();
            default -> throw new IllegalStateException("Unexpected value: " + factoryType);
        };
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(achievementTopicName);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followerTopicName);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public MessageListenerAdapter achievementListenerAdapter(AchievementListener achievementListener) {
        return new MessageListenerAdapter(achievementListener);
    }

    @Bean
    public MessageListenerAdapter followerListenerAdapter(FollowerListener followerListener) {
        return new MessageListenerAdapter(followerListener);
    }

    @Bean
    public ChannelTopic likeEventTopic() {
        return new ChannelTopic(redisProperties.getChannel().getLike());
    }

    @Bean
    public MessageListenerAdapter redisLikeEventListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    ChannelTopic projectFollowerTopic() {
        return new ChannelTopic(projectFollowerTopicName);
    }

    @Bean
    MessageListenerAdapter projectFollowerListener(ProjectFollowerEventListener projectFollowerEventListener) {
        return new MessageListenerAdapter(projectFollowerEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(
            MessageListenerAdapter achievementListenerAdapter,
            MessageListenerAdapter followerListenerAdapter,
            MessageListenerAdapter redisLikeEventListener,
            MessageListenerAdapter projectFollowerListener) {
        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();

        container.setConnectionFactory(jedisConnectionFactory());

        container.addMessageListener(achievementListenerAdapter, achievementTopic());
        container.addMessageListener(followerListenerAdapter, followerTopic());
        container.addMessageListener(redisLikeEventListener, likeEventTopic());
        container.addMessageListener(projectFollowerListener, projectFollowerTopic());

        return container;
    }
}
