package faang.school.notificationservice.config.context.redis;

import faang.school.notificationservice.subscriber.LikeEventSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class LikeConfig {

    @Value("${spring.data.redis.channel.like}")
    private String likeTopic;

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(likeTopic);
    }

    @Bean
    MessageListenerAdapter likeEventListener(LikeEventSubscriber likeEventSubscriber) {
        return new MessageListenerAdapter(likeEventSubscriber);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> likeRequester(MessageListenerAdapter likeEventListener) {
        return Pair.of(likeEventListener, likeTopic());
    }
}
