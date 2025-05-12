package faang.school.notificationservice.config.context;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.messaging.AchievementEventListener;
import faang.school.notificationservice.messaging.FollowEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.List;

public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Value("${spring.data.redis.channel.follower}")
    private String followEventsTopic;

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementEventsTopic;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public FollowEventListener followEventListener(
            List<NotificationService> notifications,
            UserServiceClient userServiceClient,
            MessageBuilder<FollowEventDto> messageBuilder) {
        return new FollowEventListener(notifications, userServiceClient, messageBuilder);
    }

    @Bean
    public MessageListenerAdapter followListenerAdapter(FollowEventListener followEventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(followEventListener, "onMessage");
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer());
        return adapter;
    }

    @Bean
    public ChannelTopic followEventTopic() {
        return new ChannelTopic(followEventsTopic);
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(
            JedisConnectionFactory jedisConnectionFactory,
            MessageListenerAdapter followListenerAdapter,
            ChannelTopic followEventTopic,
            MessageListenerAdapter achievementListenerAdapter,
            ChannelTopic achievementEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(followListenerAdapter, followEventTopic);
        container.addMessageListener(achievementListenerAdapter,achievementEventTopic);

        return container;
    }

    @Bean
    public AchievementEventListener achievementEventListener(
            UserServiceClient userServiceClient,
            List<NotificationService> notifications) {
        return new AchievementEventListener(userServiceClient,notifications );
    }

    @Bean
    public MessageListenerAdapter achievementListenerAdapter(AchievementEventListener achievementEventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(achievementEventListener, "onMessage2");
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer());
        return adapter;
    }

    @Bean
    public ChannelTopic achievementEventTopic() {
        return new ChannelTopic(achievementEventsTopic);
    }
}
