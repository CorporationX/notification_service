package faang.school.notificationservice.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class MessageListenerConfig {

    @Value("${spring.data.redis.channel.profile}")
    private String profileTopic;

    @Bean
    MessageListenerAdapter ProfileViewMessageListenerAdapter(

    )
}
