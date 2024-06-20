package faang.school.notificationservice.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.channel")
@RequiredArgsConstructor
public class RedisConfiguration {
    private final MessageListener commentEventListener;
    private String comment;

    @Bean
    RedisMessageListenerContainer commentContainer() {
        var listenerAdapter = new MessageListenerAdapter(commentEventListener);
        var container = new RedisMessageListenerContainer();

        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(listenerAdapter, new ChannelTopic(comment));

        return container;
    }

    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }
}