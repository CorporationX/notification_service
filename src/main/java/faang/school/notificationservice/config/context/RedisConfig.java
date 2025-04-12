package faang.school.notificationservice.config.context;

import faang.school.notificationservice.messaging.EventStartEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new EventStartEventListener());
    }

}
