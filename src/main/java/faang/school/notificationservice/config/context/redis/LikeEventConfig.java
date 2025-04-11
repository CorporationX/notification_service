package faang.school.notificationservice.config.context.redis;

import faang.school.notificationservice.listener.LikeEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class LikeEventConfig {

    @Value("${spring.data.redis.channel.like}")
    private String likeChannel;

    @Bean
    ChannelTopic likeChannel() {
        return new ChannelTopic(likeChannel);
    }

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(
            JedisConnectionFactory jedisConnectionFactory,
            MessageListenerAdapter likeListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(likeListener, likeChannel());
        return container;
    }
}
