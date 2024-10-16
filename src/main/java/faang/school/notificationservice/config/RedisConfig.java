package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.GoalCompletedEventListener;
import faang.school.notificationservice.listener.LikePostEventListener;
import faang.school.notificationservice.listener.ProjectFollowerEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${redis.channels.project-follower}")
    private String projectFollowerEventChannel;

    @Value("${redis.channels.like_post}")
    private String topicNameLikePost;

    @Value("${redis.channels.goal-completed}")
    private String goalCompletedEventChannel;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public ChannelTopic projectFollowerChannelTopic() {
        return new ChannelTopic(projectFollowerEventChannel);
    }

    @Bean
    public ChannelTopic likePostChannelTopic() {
        return new ChannelTopic(topicNameLikePost);
    }

    @Bean
    public ChannelTopic goalCompletedChannelTopic() {
        return new ChannelTopic(goalCompletedEventChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            LettuceConnectionFactory lettuceConnectionFactory,
            ProjectFollowerEventListener projectFollowerEventListener,
            LikePostEventListener likePostEventListener,
            GoalCompletedEventListener goalCompletedEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(projectFollowerEventListenerAdapter(projectFollowerEventListener), projectFollowerChannelTopic());
        container.addMessageListener(likePostEventListenerAdapter(likePostEventListener), likePostChannelTopic());
        container.addMessageListener(goalCompletedEventListenerAdapter(goalCompletedEventListener), goalCompletedChannelTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter projectFollowerEventListenerAdapter(ProjectFollowerEventListener projectFollowerEventListener) {
        return new MessageListenerAdapter(projectFollowerEventListener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter likePostEventListenerAdapter(LikePostEventListener likePostEventListener) {
        return new MessageListenerAdapter(likePostEventListener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter goalCompletedEventListenerAdapter(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener, "onMessage");
    }
}
