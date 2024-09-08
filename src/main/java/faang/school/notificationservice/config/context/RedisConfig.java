package faang.school.notificationservice.config.context;

import faang.school.notificationservice.listener.AchievementEventListener;
import faang.school.notificationservice.messaging.LikeEventListenerV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
//@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementChannelName;
    @Value("${spring.data.redis.channel.like}")
    private String likeChannelTopic;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public MessageListenerAdapter achievementListener(AchievementEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter likeEventListener(LikeEventListenerV2 likeEventListenerV2) {
        return new MessageListenerAdapter(likeEventListenerV2);
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(achievementChannelName);
    }

    @Bean
    public ChannelTopic likeEventTopic() {
        return new ChannelTopic(likeChannelTopic);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter achievementListener,
                                                        JedisConnectionFactory redisConnectionFactory,
                                                        MessageListenerAdapter likeEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(achievementListener, achievementTopic());
        container.addMessageListener(likeEventListener, likeEventTopic());
        return container;
    }
}
