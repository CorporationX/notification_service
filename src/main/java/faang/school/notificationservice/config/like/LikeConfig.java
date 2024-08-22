package faang.school.notificationservice.config.like;

import faang.school.notificationservice.listener.like.LikeListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class LikeConfig {

    @Value("${spring.data.redis.channel.like}")
    private String likeChannel;

    @Bean
    MessageListenerAdapter likeListenerAdapter(LikeListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> like(
            @Qualifier("likeListenerAdapter") MessageListenerAdapter likeListenerAdapter) {
        return Pair.of(likeListenerAdapter, new ChannelTopic(likeChannel));
    }
}