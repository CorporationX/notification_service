package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.FollowerEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.channel.follower}")
    private String followerEventListenerTopicName;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Autowired
    private FollowerEventListener followerEventListener;

    @Bean(name = "followerTopic")
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerEventListenerTopicName);
    }

    @Bean
    MessageListenerAdapter followerEventListenerAdapter() {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }
    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(followerEventListenerAdapter(), followerTopic());

        return container;
    }
}
