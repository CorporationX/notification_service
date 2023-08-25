package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.EventStartEventListener;
import faang.school.notificationservice.listener.FollowerEventListener;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RedisConfig {

    private final FollowerEventListener followerEventListener;
    private final EventStartEventListener eventStartEventListener;

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.channels.follower}")
    private String followerTopicName;
    @Value("${spring.data.redis.channels.event_start}")
    private String eventStartEventTopicName;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisMessageListenerContainer likePostMessageConsumerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        MessageListenerAdapter followerEventMessageListenerAdapter = new MessageListenerAdapter(followerEventListener);
        MessageListenerAdapter eventStartEventMessageListenerAdapter = new MessageListenerAdapter(eventStartEventListener);

        container.addMessageListener(followerEventMessageListenerAdapter, new ChannelTopic(followerTopicName));
        container.addMessageListener(eventStartEventMessageListenerAdapter, new ChannelTopic(eventStartEventTopicName));

        return container;
    }
}
