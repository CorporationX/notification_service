package faang.school.notificationservice.config.context.comment;

import faang.school.notificationservice.subscriber.RedisCommentSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisCommentConfig {
    private final JedisConnectionFactory jedisConnectionFactory;
    @Value("${spring.data.redis.channel.calculations_channel}")
    private String channelCommentName;

    @Bean
    ChannelTopic commentChannel() {
        return new ChannelTopic("${channelCommentName}");
    }

    @Bean
    MessageListenerAdapter messageCommentListener() {
        return new MessageListenerAdapter(new RedisCommentSubscriber());
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(messageCommentListener(), commentChannel());
        return container;
    }
}
