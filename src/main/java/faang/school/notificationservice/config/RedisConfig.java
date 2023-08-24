package faang.school.notificationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final MessageListener achievementEventListener;
    private final MessageListener followerEventListener;
    @Value("${spring.data.redis.channel.follower}")
    private String followerTopicName;
    @Value("${spring.data.redis.channel.achievement}")
    private String achievementTopicName;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter followerListenerAdapter = new MessageListenerAdapter(followerEventListener);
        MessageListenerAdapter achievementListenerAdapter = new MessageListenerAdapter(achievementEventListener);
        container.addMessageListener(followerListenerAdapter, followerChannelTopic());
        container.addMessageListener(achievementListenerAdapter, achievementChannelTopic());

        return container;
    }

    @Bean
    public ChannelTopic followerChannelTopic() {
        return new ChannelTopic(followerTopicName);
    }

    @Bean
    public ChannelTopic achievementChannelTopic() {
        return new ChannelTopic(achievementTopicName);
    }

//    @Bean
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource messageSource
//                = new ReloadableResourceBundleMessageSource();
//
//        messageSource.setBasename("classpath:messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }
}
