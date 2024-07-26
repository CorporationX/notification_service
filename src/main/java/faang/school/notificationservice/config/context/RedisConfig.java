package faang.school.notificationservice.config.context;

import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.LikeEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.channel.comment-event-chanel}")
    private String commentEventChannelName;

    @Value("${spring.data.redis.channel.like_channel}")
    private String likeChannelName;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    MessageListenerAdapter commentEventMessageListenerAdapter(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    MessageListenerAdapter likeEventMessageListenerAdapter(LikeEventListener likeEventListener){
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic commentEventTopic() {
        return new ChannelTopic(commentEventChannelName);
    }

    @Bean
    public ChannelTopic likeChannelTopic() {
        return new ChannelTopic(likeChannelName);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter commentEventMessageListenerAdapter,
                                                                       MessageListenerAdapter likeEventMessageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        container.addMessageListener(commentEventMessageListenerAdapter, commentEventTopic());
        container.addMessageListener(likeEventMessageListenerAdapter, likeChannelTopic());

        return container;
    }
}
