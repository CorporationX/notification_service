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
    private String likeChannel;

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(likeChannel);
    }

    @Bean
    MessageListenerAdapter followerEventListener(LikeEventSubscriber likeEventSubscriber) {
        return new MessageListenerAdapter(likeEventSubscriber);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> followerRequester(MessageListenerAdapter followerEventListener) {
        return Pair.of(followerEventListener, topic());
    }
}
