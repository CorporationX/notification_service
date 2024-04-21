package faang.school.notificationservice.config.context;

import faang.school.notificationservice.listener.GoalCompletedEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    String host;
    @Value("${spring.data.redis.port}")
    int port;
    @Value("{spring.data.redis.channel.goal-completed}")
    private String goalCompletedChannelName;

    @Bean
    MessageListenerAdapter goalCompletedListener(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener);
    }

    @Bean
    ChannelTopic goalCompletedTopic() {
        return new ChannelTopic(goalCompletedChannelName);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter goalCompletedListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(goalCompletedListener, goalCompletedTopic());
        return container;
    }
}
