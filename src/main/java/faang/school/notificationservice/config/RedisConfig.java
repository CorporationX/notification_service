package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.event.EventStartListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Bean
    public MessageListenerAdapter eventMessageListener(EventStartListener eventStartListener) {
        return new MessageListenerAdapter(eventStartListener);
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
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter eventStartEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(eventStartEventListener, eventTopic());
        return container;
    }
}
