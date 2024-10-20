package faang.school.notificationservice.config.redis;

import lombok.RequiredArgsConstructor;
import faang.school.notificationservice.listener.LikeEventListener;
import faang.school.notificationservice.listener.EventStartEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.MessageListener;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.channels.comment-channel.name}")
    private String commentChannel;



    public JedisConnectionFactory jedisConnectionFactory() {
    MessageListenerAdapter eventListenerAdapter(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
    public ChannelTopic commentTopic() {
        return new ChannelTopic(commentChannel);
    }

    @Bean
    public MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean(value = "likeChannel")
    public ChannelTopic likeEventTopic(@Value("${spring.data.redis.like-channel.name}") String name) {
        return new ChannelTopic(name);
    }

    @Bean
    public MessageListenerAdapter commentMessageListenerAdapter(
            @Qualifier("commentEventListener") MessageListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public RedisMessageListenerContainer container(MessageListenerAdapter commentMessageListenerAdapter,
                                                   MessageListenerAdapter likeListener,
                                                   @Qualifier("likeEventTopic") ChannelTopic likeEventTopic) {
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                 MessageListenerAdapter eventListenerAdapter,
                                                 @Qualifier("eventTopic") ChannelTopic eventTopic) {
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(commentMessageListenerAdapter, commentTopic());
        container.addMessageListener(likeListener, likeEventTopic);
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(eventListenerAdapter, eventTopic);
}
}

    @Bean(value = "eventTopic")
    public ChannelTopic eventTopic(@Value("${spring.data.redis.channels.event-channel.name}") String topic) {
        return new ChannelTopic(toString());
    }
}