package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.like.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MessageBuilderConfig {

    @Bean
    public Map<Class<?>, MessageBuilder<?>> messageBuilderMap(MessageBuilder<LikeEvent> likeMessageBuilder) {
        return Map.of(LikeEvent.class, likeMessageBuilder);
    }
}
