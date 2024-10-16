package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.service.listener.LikePostEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public ChannelTopic likePostTopic() {
        return new ChannelTopic(redisProperties.getChannels().getLikePostChannel());
    }

    @Bean
    public MessageListenerAdapter likePostEvent(LikePostEventListener likePostEventListener) {
        return new MessageListenerAdapter(likePostEventListener);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
                redisProperties.getHost(),
                redisProperties.getPort());

        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter likePostEvent) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likePostEvent, likePostTopic());
        return container;
    }
}
