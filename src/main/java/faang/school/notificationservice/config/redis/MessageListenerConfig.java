package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.profileview.ProfileViewEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class MessageListenerConfig {

    @Value("${spring.data.redis.channel.profile-view}")
    private String profileTopic;

    @Bean
    MessageListenerAdapter profileViewMessageListenerAdapter(
            ProfileViewEventListener profileViewEventListener
    ) {
        return new MessageListenerAdapter(profileViewEventListener);
    }

    @Bean
    ChannelTopic profileTopic() {
        return new ChannelTopic(profileTopic);
    }
}
