package faang.school.notificationservice.config.messaging;

import faang.school.notificationservice.messaging.AchievementListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisAchievementConfig {

    @Value("${spring.data.redis.channel.achievement}")
    private String channel;

    @Bean
    public MessageListenerAdapter achievementMessageListener(AchievementListener achievementListener) {
        return new MessageListenerAdapter(achievementListener);
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(channel);
    }

}
