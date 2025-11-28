package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.FollowerEventListener;
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

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.follower}")
    private String followerEventsTopic;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            JedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter followerListenerAdapter,
            ChannelTopic followerTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(followerListenerAdapter, followerTopic);
        return container;
    }

    @Bean
    public MessageListenerAdapter followerListenerAdapter(FollowerEventListener eventListener) {
        return new MessageListenerAdapter(eventListener);
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followerEventsTopic);
    }
}
