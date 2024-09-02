package faang.school.notificationservice.config.redis.listener;

import faang.school.notificationservice.listeners.CommentEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class CommentEventListenerConfig {

    @Value("${spring.data.redis.channel.comment_channel}")
    private String topicName;

    @Bean
    public MessageListenerAdapter commentEventListenerAdapter(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(topicName);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> profilePiclistenerTopicPair(
            @Qualifier("commentEventListenerAdapter") MessageListenerAdapter commentEventListenerAdapter,
            @Qualifier("commentTopic") ChannelTopic commentTopic) {

        return Pair.of(commentTopic, commentEventListenerAdapter);
    }
}
