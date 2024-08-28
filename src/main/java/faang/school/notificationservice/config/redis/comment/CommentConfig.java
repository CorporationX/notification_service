package faang.school.notificationservice.config.redis.comment;

import faang.school.notificationservice.listener.comment.CommentEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class CommentConfig {

    @Value("${spring.data.redis.channel.comment}")
    private String commentChannel;

    @Bean
    MessageListenerAdapter commentListenerAdapter(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> commentEvent(
            @Qualifier("commentListenerAdapter") MessageListenerAdapter commentListenerAdapter) {
        return Pair.of(commentListenerAdapter, new ChannelTopic(commentChannel));
    }
}
