package faang.school.notificationservice.messaging;

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
    private String hostName;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channel.like}")
    private String likeChannelTopic;

    @Bean
    JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(hostName, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    MessageListenerAdapter likeEventListener(LikeEventListenerV2 likeEventListenerV2) {
        return new MessageListenerAdapter(likeEventListenerV2);
    }
    @Bean
    ChannelTopic likeEventTopic() {
        return new ChannelTopic(likeChannelTopic);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter likeEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.addMessageListener(likeEventListener, likeEventTopic());
        return container;
    }
}
