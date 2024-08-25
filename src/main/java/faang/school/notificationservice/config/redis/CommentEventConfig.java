package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.CommentEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class CommentEventConfig {

    @Value("${spring.data.redis.channel.comment}")
    private String commentChannelName;

    @Bean
    public ChannelTopic commentEventTopic() {
        return new ChannelTopic(commentChannelName);
    }

    @Bean
    public MessageListenerAdapter commentMessageListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> commentRequester(MessageListenerAdapter commentMessageListener) {
        return Pair.of(commentMessageListener, commentEventTopic());
    }
}
