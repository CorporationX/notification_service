package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.AchievementEventListener;
import faang.school.notificationservice.listener.LikePostEventListener;
import faang.school.notificationservice.listener.goal.GoalCompletedEventListener;
import faang.school.notificationservice.listener.follower.FollowerMessageListener;
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
    public RedisMessageListenerContainer redisContainer(List<Pair<MessageListenerAdapter, ChannelTopic>> requesters, JedisConnectionFactory jedisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        requesters.forEach(
                (requester) -> container.addMessageListener(requester.getFirst(), requester.getSecond())
        );

        return container;
    }

    @Bean
    public List<Pair<MessageListenerAdapter, ChannelTopic>> requesters(
            MessageListenerAdapter followerMessageListener,
            ChannelTopic followerTopic,
            MessageListenerAdapter goalCompletedMessageListener,
            ChannelTopic goalCompletedEventTopic,
            MessageListenerAdapter likePostMessageListener,
            ChannelTopic likePostTopic,
            MessageListenerAdapter achievementEvent,
            ChannelTopic achievementEventTopic)
    {
        return List.of(
                Pair.of(followerMessageListener, followerTopic),
                Pair.of(goalCompletedMessageListener, goalCompletedEventTopic),
                Pair.of(likePostMessageListener, likePostTopic),
                Pair.of(achievementEvent, achievementEventTopic)
        );
    }

    @Bean
    ChannelTopic goalCompletedEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().getGoalCompletedEvent());
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(redisProperties.getChannels().getFollower());
    }

    @Bean
    public ChannelTopic likePostTopic() {
        return new ChannelTopic(redisProperties.getChannels().getLikePostChannel());
    }

    @Bean
    public ChannelTopic achievementEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().getAchievementEvent());
    }

    @Bean
    MessageListenerAdapter goalCompletedMessageListener(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    public MessageListenerAdapter followerMessageListener(FollowerMessageListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public MessageListenerAdapter likePostMessageListener(LikePostEventListener likePostEventListener) {
        return new MessageListenerAdapter(likePostEventListener);
    }

    @Bean
    public MessageListenerAdapter achievementEvent(AchievementEventListener achievementEventListener) {
        return new MessageListenerAdapter(achievementEventListener);
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }
}
