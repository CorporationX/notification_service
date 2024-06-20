package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.RecommendationRequestedEventListener;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Setter
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfig {
    private int port;
    private String host;
    private Channels channel;
    private final RecommendationRequestedEventListener recommendationRequestedEventListener;
    private final CommentEventListener commentEventListener;

    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        var recommendationRequestListenerAdapter = new MessageListenerAdapter(recommendationRequestedEventListener);
        var commentListenerAdapter =  new MessageListenerAdapter(commentEventListener);
        var container = new RedisMessageListenerContainer();

        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recommendationRequestListenerAdapter, createTopic(channel.getRecommendation()));
        container.addMessageListener(commentListenerAdapter, createTopic(channel.getRecommendation()));

        return container;
    }

    private ChannelTopic createTopic(String topicName) {
        return new ChannelTopic(topicName);
    }

    @Data
    private static class Channels {
        private String recommendation;
        private String comment;
    }
}
