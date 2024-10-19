package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.comment.NewCommentEventListener;
import faang.school.notificationservice.listener.follower.FollowerEventListener;
import faang.school.notificationservice.listener.goal.GoalCompletedEventListener;
import faang.school.notificationservice.listener.like.LikePostEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter goalCompletedMessageListener,
                                                        MessageListenerAdapter newCommentMessageListener,
                                                        MessageListenerAdapter followerMessageListener,
                                                        MessageListenerAdapter likePostMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(goalCompletedMessageListener, goalCompletedEventTopic());
        container.addMessageListener(newCommentMessageListener, newCommentEventTopic());
        container.addMessageListener(followerMessageListener, followerTopic());
        container.addMessageListener(likePostMessageListener, likePostTopic());

        return container;
    }

    @Bean
    public ChannelTopic goalCompletedEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().getGoalCompletedEvent());
    }

    @Bean
    public MessageListenerAdapter goalCompletedMessageListener(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    public ChannelTopic newCommentEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().getNewComment());
    }

    @Bean
    public MessageListenerAdapter newCommentMessageListener(NewCommentEventListener newCommentEventListener) {
        return new MessageListenerAdapter(newCommentEventListener);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(redisProperties.getChannels().getFollower());
    }

    @Bean
    public MessageListenerAdapter followerMessageListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public ChannelTopic likePostTopic() {
        return new ChannelTopic(redisProperties.getChannels().getLikePostChannel());
    }

    @Bean
    public MessageListenerAdapter likePostMessageListener(LikePostEventListener likePostEventListener) {
        return new MessageListenerAdapter(likePostEventListener);
    }
}
