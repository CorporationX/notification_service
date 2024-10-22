package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.AchievementEventListener;
import faang.school.notificationservice.listener.comment.NewCommentEventListener;
import faang.school.notificationservice.listener.follower.FollowerEventListener;
import faang.school.notificationservice.listener.goal.GoalCompletedEventListener;
import faang.school.notificationservice.listener.like.LikePostEventListener;
import faang.school.notificationservice.listener.AchievementEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

import java.util.List;

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
    public RedisMessageListenerContainer redisContainer(List<Pair<MessageListenerAdapter, ChannelTopic>> requesters,
                                                        JedisConnectionFactory jedisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        requesters.forEach(
                (requester) -> container.addMessageListener(requester.getFirst(), requester.getSecond())
        );

        return container;
    }

    @Bean
    ChannelTopic goalCompletedEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().getGoalCompletedEvent());
    }

    @Bean
    MessageListenerAdapter goalCompletedMessageListener(GoalCompletedEventListener goalCompletedEventListener) {
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

    @Bean
    public ChannelTopic achievementEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().getAchievementEvent());
    }

    @Bean
    public MessageListenerAdapter achievementEvent(AchievementEventListener achievementEventListener) {
        return new MessageListenerAdapter(achievementEventListener);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> followerPair(MessageListenerAdapter followerMessageListener,
                                                                   ChannelTopic followerTopic) {
        return Pair.of(followerMessageListener, followerTopic);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> goalCompletedPair(MessageListenerAdapter goalCompletedMessageListener,
                                                                        ChannelTopic goalCompletedEventTopic) {
        return Pair.of(goalCompletedMessageListener, goalCompletedEventTopic);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> likePostPair(MessageListenerAdapter likePostMessageListener,
                                                                   ChannelTopic likePostTopic) {
        return Pair.of(likePostMessageListener, likePostTopic);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> achievementPair(MessageListenerAdapter newCommentMessageListener,
                                                                     ChannelTopic newCommentEventTopic) {
        return Pair.of(newCommentMessageListener, newCommentEventTopic);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> newCommentPair(MessageListenerAdapter achievementEventListener,
                                                                     ChannelTopic achievementEventTopic) {
        return Pair.of(achievementEventListener, achievementEventTopic);
    }
}
