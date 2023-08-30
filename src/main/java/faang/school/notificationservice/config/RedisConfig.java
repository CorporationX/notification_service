package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.event.CommentEventListener;
import faang.school.notificationservice.listener.event.EventStartListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.event_channel.name}")
    private String eventChannel;

    @Value("${spring.data.redis.channels.comment_channels.name}")
    private String CommentChannel;

    @Bean(name = "eventMessageListenerAdapter")
    public MessageListenerAdapter eventMessageListener(EventStartListener eventStartListener) {
        return new MessageListenerAdapter(eventStartListener);
    }

    @Bean(name = "commentMessageListenerAdapter")
    public MessageListenerAdapter commentMessageListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Crated redis connection factory with host: {}, port: {}", host, port);
        return new JedisConnectionFactory();
    }

    @Bean
    ChannelTopic eventTopic() {
        return new ChannelTopic(eventChannel);
    }

    @Bean
    ChannelTopic commentTopic() {
        return new ChannelTopic(CommentChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(@Qualifier("eventMessageListenerAdapter")
                                                            MessageListenerAdapter eventStartEventListener,
                                                        @Qualifier("commentMessageListenerAdapter")
                                                        MessageListenerAdapter commentEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(eventStartEventListener, eventTopic());
        container.addMessageListener(commentEventListener, commentTopic());
        return container;
    }
}
