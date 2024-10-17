package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.GoalCompletedEventListener;
import faang.school.notificationservice.listener.AchievementEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter goalCompletedEvent,
                                                 MessageListenerAdapter achievementEvent) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(goalCompletedEvent, goalCompletedEventTopic());
        container.addMessageListener(achievementEvent, achievementEventTopic());
        return container;
    }

    @Bean
    ChannelTopic goalCompletedEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().getGoalCompletedEvent());
    }

    @Bean
    public ChannelTopic achievementEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().getAchievementEvent());
    }

    @Bean
    MessageListenerAdapter goalCompletedEvent(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener);
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
