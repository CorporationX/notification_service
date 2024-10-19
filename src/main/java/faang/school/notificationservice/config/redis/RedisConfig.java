package faang.school.notificationservice.config.redis;

import lombok.RequiredArgsConstructor;
import faang.school.notificationservice.listener.LikeEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
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
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(commentMessageListenerAdapter, commentTopic());
        container.addMessageListener(likeListener, likeEventTopic);
        return container;
    }
}
