package faang.school.notificationservice.config.context;

import faang.school.notificationservice.listener.FollowerEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class FollowerEventConfig {

    @Value("${spring.data.redis.channel.follower}")
    private String followerChannelName;

    @Bean
    public ChannelTopic followerEventTopic() {
        return new ChannelTopic(followerChannelName);
    }

    @Bean
    public MessageListenerAdapter followerMessageListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> followerRequester(MessageListenerAdapter followerMessageListener) {
        return Pair.of(followerMessageListener, followerEventTopic());
    }
}