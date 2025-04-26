package faang.school.notificationservice.config.context;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.FollowEventListener;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Value("${spring.data.redis.channel.follower}")
    private String followEventsTopic;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public ChannelTopic followEventTopic() {
        return new ChannelTopic(followEventsTopic);
    }

    @Bean
    public FollowEventListener followEventListener(UserServiceClient userServiceClient,
    NotificationService notificationService) {
        return new FollowEventListener(userServiceClient,notificationService);
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(FollowEventListener followEventListener) {
        return new MessageListenerAdapter(followEventListener, "onMessage");
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(
            JedisConnectionFactory jedisConnectionFactory, MessageListenerAdapter messageListenerAdapter,
            ChannelTopic followEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(messageListenerAdapter,followEventTopic);
        return container;
    }
}
