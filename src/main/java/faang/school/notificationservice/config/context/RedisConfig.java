package faang.school.notificationservice.config.context;

import faang.school.notificationservice.listener.AchievementEventListener;
import lombok.AllArgsConstructor;
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
public class RedisConfig {

    @Value("${spring.data.redis.channel.achievement}")
    private final String achievementChannelName;

    @Bean
    MessageListenerAdapter achievementListener(AchievementEventListener listener){
        return new MessageListenerAdapter(listener);
    }
    @Bean
    ChannelTopic achievementTopic(){
        return new ChannelTopic(achievementChannelName);
    }
    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter achievementListener, JedisConnectionFactory jedisConnectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(achievementListener, achievementTopic());
        return container;
    }
}
