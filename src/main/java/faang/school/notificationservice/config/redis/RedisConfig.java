package faang.school.notificationservice.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.channels.comment-channel.name}")
    private String commentChannel;

    @Value("${spring.data.redis.channel.follower}")
    private String followerEventListenerTopicName;

    @Autowired
    private FollowerEventListener followerEventListener;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean(name = "followerTopic")
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerEventListenerTopicName);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(commentChannel);
    }

    @Bean
    public MessageListenerAdapter commentMessageListenerAdapter(
            @Qualifier("commentEventListener") MessageListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    MessageListenerAdapter followerEventListenerAdapter() {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public RedisMessageListenerContainer container(MessageListenerAdapter commentMessageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(commentMessageListenerAdapter, commentTopic());
        container.addMessageListener(followerEventListenerAdapter, followerTopic());
        return container;
    }
}