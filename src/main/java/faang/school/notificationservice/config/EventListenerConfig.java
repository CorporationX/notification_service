package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.UserDto.PreferredContact;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class EventListenerConfig {

    @Bean(name = "messageBuilderMap")
    public Map<Class<?>, MessageBuilder<?>> messageBuilderMap(List<MessageBuilder<?>> messageBuilders) {
        return messageBuilders.stream()
                .collect(Collectors.toMap(MessageBuilder::getInstance, Function.identity()));
    }

    @Bean(name = "notificationServiceMap")
    public Map<PreferredContact, NotificationService> notificationServiceMap(List<NotificationService> notificationServices) {
        return notificationServices.stream()
                .collect(Collectors.toMap(NotificationService::getPreferredContact, Function.identity()));
    }
}