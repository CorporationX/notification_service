package faang.school.notificationservice.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(
                redisProperties.getHost(),
                redisProperties.getPort()
        );

        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(List<Pair<MessageListenerAdapter, ChannelTopic>> requesters) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        requesters.forEach((requester) -> container.addMessageListener(requester.getFirst(), requester.getSecond()));

        return container;
    }
}
