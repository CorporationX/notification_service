package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.event.EventStartEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfiguration {

    @Value("${spring.data.redis.channel.event-start-channel}")
    private String channelEventStartName;

    @Bean
    public MessageListenerAdapter eventStartListener(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    public ChannelTopic eventStartTopic() {
        return new ChannelTopic(channelEventStartName);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MessageListenerAdapter eventStartListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(eventStartListener, eventStartTopic());
        return container;
    }
}