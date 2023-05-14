package faang.school.notificationservice.config.messaging;

import faang.school.notificationservice.messaging.FollowerListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisSubscriptionConfig {

    @Value("${spring.data.redis.channels.follower_channel.name}")
    private String channel;

    @Bean
    public MessageListenerAdapter messageListener(FollowerListener followerListener) {
        return new MessageListenerAdapter(followerListener);
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(channel);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(JedisConnectionFactory connectionFactory,
                                                 MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, topic());
        return container;
    }
}
