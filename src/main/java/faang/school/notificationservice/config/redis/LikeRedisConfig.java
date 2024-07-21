package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.like.LikeEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class LikeRedisConfig {

    @Bean
    public Pair<Topic, MessageListenerAdapter> getLikeListenerAdapterPair(
            @Value("${spring.data.redis.channel.like_post}") String channelTopicName,
            @Qualifier("likeMessageAdapter") MessageListenerAdapter messageListenerAdapter) {

        return Pair.of(new ChannelTopic(channelTopicName), messageListenerAdapter);
    }

    @Bean
    public MessageListenerAdapter likeMessageAdapter(LikeEventListener listener) {
        return new MessageListenerAdapter(listener);
    }
}
