package faang.school.notificationservice.config.listener;

import faang.school.notificationservice.listeners.LikeEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class LikeListenerConfig {

    @Value("${spring.data.redis.channel.like}")
    private String topic;

    @Bean
    public MessageListenerAdapter likeListenerAdapter(LikeEventListener likeListener) {
        return new MessageListenerAdapter(likeListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> likeTopic(
            @Qualifier("likeListenerAdapter") MessageListenerAdapter likeAdapter) {
        return Pair.of(new ChannelTopic(topic), likeAdapter);
    }
}
