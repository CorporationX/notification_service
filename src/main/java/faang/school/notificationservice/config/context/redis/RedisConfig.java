package faang.school.notificationservice.config.context.redis;

import faang.school.notificationservice.listener.AchievementEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Evgenii Malkov
 */
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementChannelTopic;

    @Bean
    RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    MessageListenerAdapter achievementListener(AchievementEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    ChannelTopic achievementChannelTopic() {
        return new ChannelTopic(achievementChannelTopic);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setValueSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter achievementListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(achievementListener, achievementChannelTopic());
        return container;
    }

}
