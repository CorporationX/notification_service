package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.EventStartEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    MessageListenerAdapter eventStartListener(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    ChannelTopic eventStartTopic() {
        return new ChannelTopic(redisProperties.getChannel().getEventStartEventChannel());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter eventStartListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(eventStartListener, eventStartTopic());
        return container;
    }

}
