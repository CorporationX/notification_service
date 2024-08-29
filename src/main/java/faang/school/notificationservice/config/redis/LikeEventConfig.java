package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.LikeEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class LikeEventConfig {

    @Value("${spring.data.redis.channel.like}")
    private String likeTopic;

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(likeTopic);
    }

    @Bean
    MessageListenerAdapter likeMessageListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> likeRequester(MessageListenerAdapter likeMessageListener) {
        return Pair.of(likeMessageListener, likeTopic());
    }
}
