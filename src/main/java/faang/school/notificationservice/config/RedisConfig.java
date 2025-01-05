package faang.school.notificationservice.config;

import faang.school.notificationservice.message.consumer.ProfileViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ProfileViewEventListener profileViewEventListener;

    @Value("${spring.data.redis.channel.profile-view-channel}")
    private String profileViewTopicName;

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);

        MessageListenerAdapter profileViewEventListenerAdapter
                = new MessageListenerAdapter(profileViewEventListener);
        ChannelTopic profileViewEventTopic = new ChannelTopic(profileViewTopicName);
        listenerContainer.addMessageListener(profileViewEventListenerAdapter, profileViewEventTopic);

        return listenerContainer;
    }
}
