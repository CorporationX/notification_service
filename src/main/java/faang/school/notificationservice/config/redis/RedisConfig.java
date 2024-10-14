package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.RecommendationRequestedEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.channel.recommendation-request}")
    private String recommendationRequestChannel;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public ChannelTopic recommendationRequestTopic() {
        return new ChannelTopic(recommendationRequestChannel);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RecommendationRequestedEventListener recRequestedListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recRequestedListener, recommendationRequestTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter recRequestedListener(RecommendationRequestedEventListener recRequestedEventListener) {
        return new MessageListenerAdapter(recRequestedEventListener);
    }

}
