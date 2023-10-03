package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.AchievementEventListener;
import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.GoalCompletedEventListener;
import faang.school.notificationservice.listener.LikeEventListener;
import faang.school.notificationservice.listener.ProfileViewEventListener;
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
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.viewProfileTopic}")
    private String viewProfileName;
    @Value("${spring.data.redis.channel.likeTopic}")
    private String likeChannelName;
    @Value("${spring.data.redis.channel.completed_goal}")
    private String goalCompletedTopic;
    @Value("${spring.data.redis.channel.achievementTopic}")
    private String achievementChannelName;
    @Value("${spring.data.redis.channel.comment_channel.name}")
    private String commentChannelName;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
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
    MessageListenerAdapter profileViewListener(ProfileViewEventListener profileViewEventListener) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    MessageListenerAdapter goalCompletedListener(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(likeChannelName);
    }

    @Bean
    public ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedTopic);
    }

    @Bean
    MessageListenerAdapter achievementListener(AchievementEventListener achievementEventListener) {
        return new MessageListenerAdapter(achievementEventListener);
    }

    @Bean
    public ChannelTopic viewProfileTopic() {
        return new ChannelTopic(viewProfileName);
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(achievementChannelName);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(commentChannelName);
    }

    @Bean
    MessageListenerAdapter commentListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter profileViewListener,
                                                 MessageListenerAdapter likeListener,
                                                 MessageListenerAdapter achievementListener,
                                                 MessageListenerAdapter goalCompletedListener,
                                                 MessageListenerAdapter commentListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.setTopicSerializer(new StringRedisSerializer());
        container.addMessageListener(profileViewListener, viewProfileTopic());
        container.addMessageListener(likeListener, likeTopic());
        container.addMessageListener(achievementListener, achievementTopic());
        container.addMessageListener(goalCompletedListener, goalCompletedTopic());
        container.addMessageListener(commentListener, commentTopic());
        return container;
    }
}
