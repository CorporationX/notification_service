package faang.school.notificationservice.config.context;

import faang.school.notificationservice.messaging.listener.EventStartEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    public static final String EVENT_START_TOPIC = "event-start";

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public MessageListenerAdapter messageListener(EventStartEventListener eventStartEventListener) {
        return new MessageListenerAdapter(eventStartEventListener);
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(EVENT_START_TOPIC);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            JedisConnectionFactory jedisConnectionFactory,
            MessageListenerAdapter listenerAdapter,
            ChannelTopic topic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(listenerAdapter, topic);
        return container;
    }
}
