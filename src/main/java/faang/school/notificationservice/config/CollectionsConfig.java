package faang.school.notificationservice.config;

import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.MessageBuilder;
import faang.school.notificationservice.sender.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CollectionsConfig {
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder> messageBuilders;

    @Bean
    public Map<UserDto.PreferredContact, NotificationService> notifications() {
        Map<UserDto.PreferredContact, NotificationService> notifications = new HashMap<>(notificationServices.size());
        for (NotificationService i : notificationServices) {
            notifications.put(i.getPreferredContact(), i);
        }
        return notifications;
    }

    @Bean
    public Map<Class<?>, MessageBuilder> messageBuilders() {
        Map<Class<?>, MessageBuilder> builders = new HashMap<>(messageBuilders.size());
        for (MessageBuilder i : messageBuilders) {
            builders.put(i.getClass(), i);
        }
        return builders;
    }
}
