package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.EventStartEventListener;
import faang.school.notificationservice.listener.LikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.channels.comment-channel.name}")
    private String commentChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    MessageListenerAdapter eventListenerAdapter(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
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
                                                   MessageListenerAdapter eventListenerAdapter,
                                                   @Qualifier("eventTopic") ChannelTopic eventTopic,
                                                   @Qualifier("likeChannel") ChannelTopic likeEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(commentMessageListenerAdapter, commentTopic());
        container.addMessageListener(likeListener, likeEventTopic);
        container.addMessageListener(eventListenerAdapter, eventTopic);
        return container;
    }

    @Bean(value = "eventTopic")
    public ChannelTopic eventTopic(@Value("${spring.data.redis.channels.event-channel.name}") String topic) {
        return new ChannelTopic(toString());
    }
}