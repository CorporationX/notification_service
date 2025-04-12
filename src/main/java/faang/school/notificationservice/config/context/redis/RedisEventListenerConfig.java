package faang.school.notificationservice.config.context.redis;

import faang.school.notificationservice.listener.like.LikeEventListener;
import faang.school.notificationservice.listener.like.DeleteLikeEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisEventListenerConfig {

    @Value("${spring.data.redis.channel.like}")
    private String likeChannel;

    @Value("${spring.data.redis.channel.not-like}")
    private String notLikeChannel;

    @Bean
    ChannelTopic likeChannel() {
        return new ChannelTopic(likeChannel);
    }

    @Bean
    ChannelTopic notLikeChannel() {
        return new ChannelTopic(notLikeChannel);
    }

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener, "onMessage");
    }

    @Bean
    MessageListenerAdapter notLikeListener(DeleteLikeEventListener notLikeEventListener) {
        return new MessageListenerAdapter(notLikeEventListener, "onMessage");
    }

    @Bean
    RedisMessageListenerContainer redisContainer(
            JedisConnectionFactory jedisConnectionFactory,
            MessageListenerAdapter likeListener,
            MessageListenerAdapter notLikeListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(likeListener, likeChannel());
        container.addMessageListener(notLikeListener, notLikeChannel());
        return container;
    }
}
