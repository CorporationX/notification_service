package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.listener.LikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.post_like}")
    private String postLikeChannel;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config =
                new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public Jackson2JsonRedisSerializer<LikePostEvent> likeEventJsonSerializer() {
        return new Jackson2JsonRedisSerializer<>(LikePostEvent.class);
    }

    @Bean
    public MessageListenerAdapter likeEventListenerAdapter(LikeEventListener likeEventListener,
                                                           Jackson2JsonRedisSerializer<LikePostEvent> likeEventJsonSerializer) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(likeEventListener);
        adapter.setSerializer(likeEventJsonSerializer);
        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter likeEventListenerAdapter,
                                                        ChannelTopic likeEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(likeEventListenerAdapter, likeEventTopic);
        return container;
    }

    @Bean
    public ChannelTopic postLikeChannel() {
        return new ChannelTopic(postLikeChannel);
    }
}
