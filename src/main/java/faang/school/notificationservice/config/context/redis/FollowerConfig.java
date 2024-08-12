package faang.school.notificationservice.config.context.redis;

import faang.school.notificationservice.subscriber.FollowerEventSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class FollowerConfig {

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannel;

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(followerChannel);
    }

    @Bean
    MessageListenerAdapter followerEventListener(FollowerEventSubscriber followerEventSubscriber) {
        return new MessageListenerAdapter(followerEventSubscriber);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> followerRequester(MessageListenerAdapter followerEventListener) {
        return Pair.of(followerEventListener, topic());
    }
}
