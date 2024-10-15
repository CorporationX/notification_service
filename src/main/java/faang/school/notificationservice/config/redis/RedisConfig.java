package faang.school.notificationservice.config.redis;

import faang.school.notificationservice.listener.LikeEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean(value = "likeChannel")
    ChannelTopic likeEventTopic(@Value("${spring.data.redis.like-channel.name}") String name) {
        return new ChannelTopic(name);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter likeListener,
                                                 @Qualifier("likeEventTopic") ChannelTopic likeEventTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likeListener, likeEventTopic);
        return container;
    }
}
