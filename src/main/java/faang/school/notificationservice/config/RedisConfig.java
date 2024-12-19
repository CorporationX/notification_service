package faang.school.notificationservice.config;

import faang.school.notificationservice.message.consumer.PostCommentEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.channel.comment}")
    private String commentEventTopicName;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter postCommentListenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        container.addMessageListener(postCommentListenerAdapter, postCommentTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter postCommentListenerAdapter(PostCommentEventListener postCommentEventListener) {
        return new MessageListenerAdapter(postCommentEventListener);
    }

    @Bean
    public ChannelTopic postCommentTopic() {
        return new ChannelTopic(commentEventTopicName);
    }
}
